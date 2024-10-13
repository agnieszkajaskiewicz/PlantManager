import axios from "axios"

const AuthService = {
    authUser: (username, password) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

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

                return axios.post("http://" + backendServerURL + "/sign-in/v2", formData, config)
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
        const backendServerURL = process.env.REACT_APP_SERVER_URL;
    
        const config = {
            withCredentials: true
            }

        return axios.post("http://" + backendServerURL + "/logout", config)
            .then(response => {
                localStorage.removeItem('token');
                return response;
            });
        }    
}

export default AuthService;