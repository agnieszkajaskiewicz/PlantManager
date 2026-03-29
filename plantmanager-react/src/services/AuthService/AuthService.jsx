import axios from "axios";
import { getServerUrl } from '../../config/env';

const AuthService = {
    authUser: (username, password) => {
        const backendServerURL = `${getServerUrl()}`;

        if (username && password) {
                const config = {
                    headers: {
                        'content-type': 'multipart/form-data'
                    },
                    withCredentials: true
                }
                let formData = new FormData();
                formData.append('username', username);
                formData.append('password', password);

                return axios.post(backendServerURL + "/sign-in/v2", formData, config)
                    .then(response => {
                        const token = response.headers['authorization'];
                        if (token) {
                            localStorage.setItem('token', token);
                        }
                        return response;
        });
            }
        },

    logoutUser: () => {
        const backendServerURL = `${getServerUrl()}`;
    
        const config = {
            withCredentials: true
            }

        return axios.post(backendServerURL + "/logout", config)
            .then(response => {
                localStorage.removeItem('token');
                return response;
            });
        }    
}

export default AuthService;