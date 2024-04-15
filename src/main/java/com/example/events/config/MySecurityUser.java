package com.example.events.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
public class MySecurityUser extends User {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String emailAddress;


    public MySecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                          String firstName, String lastName, String emailAddress) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "MySecurityUser firstName=" + firstName + ", lastName=" + lastName +  ", emailaddress=" + emailAddress
                + "] " + super.toString();
    }

}
