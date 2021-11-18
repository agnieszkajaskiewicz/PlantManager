import axios from "axios"

const AuthService = {
    authUser: (username, password) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        const getHealthCheck = () => {
            return axios.get("http://" + backendServerURL + "/actuator/health");
        }

        if (username && password) {
            const loginUser = () => {
                const config = {
                    headers: {
                        'content-type': 'multipart/form-data'
                    }
                }
                let formData = new FormData();
                formData.append('username', username);
                formData.append('password', password);

                return axios.post("http://" + backendServerURL + "/sign-in", formData, config);

            }

            loginUser()
                .then(
                response => {
                    console.log(response);
                }
            ).catch(error => {
                console.log(error);
            });
        }
        getHealthCheck()
            .then(response => {
                console.log(response);
            })
            .catch(error => {
                console.log(error.response);
            });
    }
};

export default AuthService;