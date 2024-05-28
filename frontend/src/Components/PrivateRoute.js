import React from 'react';
import { Route, Navigate } from 'react-router-dom';
import {getAuthToken} from "../axios_helper";
export const PrivateRoute = ({ component: Component, ...rest }) => {
    const isLoggedIn = getAuthToken() !== null && getAuthToken() !== "null";
    return (
        <Route
            {...rest}
            render={(props) =>
                isLoggedIn ? (
                    <Component {...props} />
                ) : (
                    <Navigate to="/home" replace />
                )
            }
        />
    );
};