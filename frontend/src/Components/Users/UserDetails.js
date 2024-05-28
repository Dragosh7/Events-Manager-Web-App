import React, { Component } from 'react';
import { request } from '../../axios_helper';

class UserDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userDetails: null
        };
    }

    componentDidMount() {
        // Fetch user details based on user username from props
        const { username } = this.props;
        request("GET", `/user/${username}`)
            .then(response => {
                this.setState({ userDetails: response.data });
            })
            .catch(error => {
                console.error('Error fetching user details:', error);
            });
    }

    render() {
        const { userDetails } = this.state;
        if (!userDetails) {
            return null;
        }
        return (
            <div className="user-details">
                <h3>User Information</h3>
                <p><strong>Username:</strong> {userDetails.username}</p>
                <p><strong>Role:</strong> {userDetails.role}</p>
                {/* Add more user details as needed */}
            </div>
        );
    }
}

export default UserDetails;
