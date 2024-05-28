import axios from 'axios';

export const getAuthToken = () => {
    return window.localStorage.getItem('user');
};

export const setAuthHeader = (token) => {
    if (token !== null) {
        window.localStorage.setItem("user", JSON.stringify(token));
    } else {
        window.localStorage.removeItem("user");
    }
};

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json';

export const request = (method, url, data) => {

    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = {'Authorization': `Bearer ${getAuthToken()}`};
    }

    return axios({
        method: method,
        url: url,
        headers: headers,
        data: data});
};