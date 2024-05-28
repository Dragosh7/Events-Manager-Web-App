import jwt from 'jsonwebtoken';

const verify = (token, secretKey) => {
    try {
        const decoded = jwt.verify(token, secretKey);
        return true; // Token is valid
    } catch (error) {
        return false; // Token verification failed
    }
};

export default verify;