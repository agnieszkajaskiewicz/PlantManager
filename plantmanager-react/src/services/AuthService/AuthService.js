import axios from "axios"

const AuthService = {
    authUser: (username, password) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        const getHealthCheck = () => {
            return axios.get("http://" + backendServerURL + "/actuator/health");
        }

        if (username && password) {
            const loginUser = () => {
                //tu się wywala z jakiegoś powodu
                return axios.post("http://" + backendServerURL + "/sign-in", {
                    username: username,
                    password: password
                });
            }

            loginUser().then(
                response => {
                    debugger;
                }
            ).catch(error => {
                debugger;
            })
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