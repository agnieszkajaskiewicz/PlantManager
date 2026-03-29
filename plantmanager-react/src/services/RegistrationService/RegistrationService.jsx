import axios from "axios";
import { getServerUrl } from '../../config/env';

const RegistrationService = {
    createUser: (username, password, repeatPassword, email) => {
        const backendServerURL = `${getServerUrl()}`;

        if (username && password && repeatPassword && email) {
            const config = {
                withCredentials: true
            }
            const userData = {
                username: username,
                password: password,
                repeatPassword: repeatPassword,
                email: email
            }

            return axios.post(backendServerURL + "/sign-up/v2", userData, config)
                .then(response => {
                    const token = response.headers['authorization'];
                    if (token) {
                        localStorage.setItem('token', token);
                    }
                    return response;
            });
        }
    }
};

export default RegistrationService;