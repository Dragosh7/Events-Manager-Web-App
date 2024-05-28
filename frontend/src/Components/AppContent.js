import * as React from 'react';

import { request, setAuthHeader } from '../axios_helper';

import Buttons from './Buttons';
import AuthContent from './AuthContent';
import LoginForm from './Login/LoginForm';
import WelcomeContent from './contents/WelcomeContent'
import UserList from "./Users/UserList";

export default class AppContent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            componentToShow: "welcome"
        }
    };

    login = () => {
        this.setState({componentToShow: "login"})
    };


    onLogin = (e, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/login",
            {
                username: username,
                password: password
            }).then(
            (response) => {
                setAuthHeader(response.data);
                this.setState({componentToShow: "messages"});
            }).catch(
            (error) => {
                setAuthHeader(null);
                this.setState({componentToShow: "welcome"})
            }
        );
    };
    onLogout = (e) => {
        e.preventDefault();
        request(
            "GET",
            "/logout",
            ).then(
            (response) => {
                setAuthHeader(response.data.token);
                this.setState({componentToShow: "login"});
            }).catch(
            (error) => {
                setAuthHeader(null);
                this.setState({componentToShow: "welcome"})
            }
        );
    };
    onRegister = (event, firstName, lastName, email, username, password) => {
        event.preventDefault();
        request(
            "POST",
            "/register",
            {
                firstName: firstName,
                lastName: lastName,
                emailAddress: email,
                username: username,
                password: password
            }).then(
            (response) => {
                setAuthHeader(response.data.token);
                this.setState({componentToShow: "messages"});
            }).catch(
            (error) => {
                setAuthHeader(null);
                this.setState({componentToShow: "welcome"})
            }
        );
    };

    render() {
        return (
            <>
                <Buttons
                    login={this.login}
                    logout={this.onLogout}
                />

                {this.state.componentToShow === "welcome" && <WelcomeContent /> }
                {this.state.componentToShow === "login" && <LoginForm onLogin={this.onLogin} onRegister={this.onRegister} />}
                {this.state.componentToShow === "messages" && <UserList />}

            </>
        );
    };
}