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

                return axios.post("http://" + backendServerURL + "/sign-in", formData, config);
            }
        }
};

export default AuthService;