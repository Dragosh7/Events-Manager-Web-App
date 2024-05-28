package com.example.events.Service;

import com.example.events.DTOs.CredentialsDto;
import com.example.events.DTOs.*;
import com.example.events.DTOs.UserDto;
import com.example.events.Entity.Event;
import com.example.events.Entity.User;
import com.example.events.Entity.UserRole;
import com.example.events.Exceptions.AppException;
import com.example.events.Mapper.UserMapper;
import com.example.events.MyPasswordEncoder.MyPasswordEncoder;
import com.example.events.Repository.EventRepository;
import com.example.events.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.CharBuffer;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserMapper userMapper;
    @Autowired
    private JavaMailSender mailSender;


    public UserDto registerUser(RegistrationRequest registrationRequest) {
        User user = User.builder()
                .username(registrationRequest.getUsername())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .password(registrationRequest.getPassword())
                .emailAddress(registrationRequest.getEmailAddress())
                .role((UserRole.USER))
                .verificationCode(generateVerificationCode())
                .verified(false)
                .build();
        sendVerificationEmail(user);
        return this.createUser(user);
    }
    private void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmailAddress());
        message.setSubject("Email Verification");
        message.setText("Please click the link below to verify your email: \n" +
                "http://localhost:8080/verify?code=" + user.getVerificationCode());
        mailSender.send(message);
    }
    public String verifyEmail(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user != null) {
            user.setVerified(true);
            userRepository.save(user);
            return "success";
        }
        return "Unable to verify";
    }

    public UserDto registerUserRole(RegistrationRequest registrationRequest,String role) {

        if( role.isEmpty()  )
        {
            role="USER";
        }
        else{
            try{
                UserRole.valueOf(role.toUpperCase());
            }
            catch (IllegalArgumentException e)
            {
                role="USER";
            }
        }
        if(isAdminActive()) {
            User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .password(registrationRequest.getPassword())
                    .emailAddress(registrationRequest.getEmailAddress())
                    .role(UserRole.valueOf(role.toUpperCase()))
                    .build();
            return this.createUser(user);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to add this role: "+role);
    }
    private boolean isAdmin(String role) {
        return "admin".equalsIgnoreCase(role);
    }
    private boolean isOrganizer(String role) {
        return "organizer".equalsIgnoreCase(role);
    }
    public UserDto updateUser(String username, UserUpdateRequest updateRequest) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        if (!existingUser.isActive() || isAdmin(existingUser.getRole().toString())) {
            try {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to update this user");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (updateRequest.getFirstName() != null ) {
            existingUser.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null ) {
            existingUser.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmailAddress() != null ) {
            existingUser.setEmailAddress(updateRequest.getEmailAddress());
            System.out.println(updateRequest.getEmailAddress());
        }
        if (updateRequest.getPassword() != null  ) {
            if(existingUser.isActive()) {
                System.out.println(updateRequest.getPassword());
                existingUser.setPassword(MyPasswordEncoder.encode(updateRequest.getPassword()));
            }
        }

        User updatedUser = userRepository.save(existingUser);

        return userMapper.userEntityToDto(updatedUser);

}
    public UserDto createUser(User user){
        user.setPassword(MyPasswordEncoder.encode(user.getPassword()));
        return userMapper.userEntityToDto(userRepository.save(user));
    }

    public boolean findActive() {
        return getUsers().stream().anyMatch(User::isActive);
    }
    public Optional<User> findWhichUserIsActive() {
        return getUsers().stream()
                .filter(User::isActive)
                .findFirst();
    }
    public List<User> getUsers() {return userRepository.findAll();}
    public boolean setActive(User user) {
        if(!findActive())
        {
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.username())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (MyPasswordEncoder.matches(credentialsDto.password(), user.getPassword())) {
            boolean anyActiveUsers = findActive();

            if (!anyActiveUsers) {
                setActive(userRepository.findByUsername(credentialsDto.username()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND)));

            } else {
                throw new AppException("Log out first", HttpStatus.BAD_REQUEST);
            }
            return userMapper.userEntityToDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }
    public void logOut() {
        List<User> users = getUsers();
        for (User user : users) {
            user.setActive(false);
            userRepository.save(user);
        }
    }
    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
    public void deleteUser(String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        if (!existingUser.isActive() || isAdmin(existingUser.getRole().toString())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this user");
        }

        User nextAdmin = userRepository.findFirstAdmin("ADMIN");
        if (nextAdmin != null && nextAdmin.getUsername().equals("root")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Root can't be deleted");
        }

        if (nextAdmin == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No organizer/admin can take this place");
        }
        List<Event> events = eventRepository.findByOrganizer(existingUser);
        for (Event event : events) {
            event.setOrganizer(nextAdmin);
        }
        eventRepository.saveAll(events);

        userRepository.delete(existingUser);
    }
    public boolean isAdminActive(){return getUsers().stream()
            .anyMatch(user -> UserRole.ADMIN.equals(user.getRole()) && user.isActive());}
    public boolean isOrganizerActive(){return getUsers().stream()
            .anyMatch(user -> UserRole.ORGANIZER.equals(user.getRole()) && user.isActive());}
    public UserDto findByUsername(String login) {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.userEntityToDto(user);
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userEntityToDto)
                .collect(Collectors.toList());

    }
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    public UserDto getUserByUsername(String username) {
        return userMapper.userEntityToDto(Objects.requireNonNull(userRepository.findByUsername(username).orElse(null)));
    }
    public UserDto getUserById(Long Id ) {
        return userMapper.userEntityToDto(userRepository.findById(Id).orElse(null));
    }

    public boolean isOrganizerAssociatedWithEvent(Long organizerId, Long eventId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Organizer not found with ID: " + organizerId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        return event.getOrganizer().equals(organizer);
    }

    public boolean isUserAllowedToUpdateEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        if (isAdminActive() ) {
            return true;
        } else if (user.getRole().equals("organizer") || user.getRole().equals("ORGANIZER")) {
            return isOrganizerAssociatedWithEvent(userId, eventId);
        } else {
            return false;
        }
    }
    public String generateToken(UserDto userDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // Token expiry after 1 hour (adjust as needed)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Jwts.builder()
                .setSubject(userDto.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

}
