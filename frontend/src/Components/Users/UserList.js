import React, { Component } from 'react';
import { request } from '../../axios_helper';
import UserDetails from './UserDetails'; // Import UserDetails component

class UserList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            users: [],
            selectedUserId: null
        };
    }

    componentDidMount() {
        request("GET", "/allUsers")
            .then(response => {
                this.setState({ users: response.data });
            })
            .catch(error => {
                console.error('Error fetching users:', error);
            });
    }

    handleUserClick(userId) {
        this.setState(prevState => ({
            selectedUserId: prevState.selectedUserId === userId ? null : userId
        }));
    }

    render() {
        const { users, selectedUserId } = this.state;
        return (
            <div className="container mt-4">
                <h2>User List</h2>
                <table className="table table-bordered">
                    <thead className="thead-dark">
                    <tr>
                        <th>Last Name</th>
                        <th>First Name</th>
                        <th>Email</th>
                        {/* Add more table headers as needed */}
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(user => (
                        <React.Fragment key={user.emailAddress}>
                            <tr onClick={() => this.handleUserClick(user.emailAddress)}> {/* Using emailAddress as userId */}
                                <td>{user.lastName}</td>
                                <td>{user.firstName}</td>
                                <td>{user.emailAddress}</td>
                            </tr>
                            {selectedUserId === user.emailAddress && (
                                <tr>
                                    <td colSpan="3">
                                        <UserDetails username={user.username} /> {/* Pass emailAddress as userId */}
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default UserList;