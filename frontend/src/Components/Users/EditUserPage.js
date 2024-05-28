import React, { useState, useEffect } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import { request } from '../../axios_helper';

const EditUserPage = () => {
    const { username } = useParams(); // Get the username from the URL params
    const history = useHistory(); // Used for navigation
    const [userData, setUserData] = useState({
        username: '',
        firstName: '',
        lastName: '',
        emailAddress: ''
        // Add more fields as needed
    });

    // Fetch user data based on username
    useEffect(() => {
        request("GET", `/user/${username}`)
            .then(response => {
                setUserData(response.data);
            })
            .catch(error => {
                console.error('Error fetching user details:', error);
            });
    }, [username]);

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();
        // Send updated user data to the backend
        request("PUT", `/user/${username}`, userData)
            .then(response => {
                // Redirect to user list page after successful update
                history.push('/user-list');
            })
            .catch(error => {
                console.error('Error updating user:', error);
            });
    };

    // Handle input changes
    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    return (
        <div className="container mt-4">
            <h2>Edit User</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="firstName">First Name:</label>
                    <input type="text" className="form-control" id="firstName" name="firstName" value={userData.firstName} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last Name:</label>
                    <input type="text" className="form-control" id="lastName" name="lastName" value={userData.lastName} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="emailAddress">Email Address:</label>
                    <input type="email" className="form-control" id="emailAddress" name="emailAddress" value={userData.emailAddress} onChange={handleChange} />
                </div>
                {/* Add more form fields for additional user data */}
                <button type="submit" className="btn btn-primary">Submit</button>
            </form>
        </div>
    );
};

export default EditUserPage;
