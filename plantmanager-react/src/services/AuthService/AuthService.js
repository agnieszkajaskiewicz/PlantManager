import axios from "axios"

const AuthService = {
    authUser: () => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        const getHealthCheck = () => {
            return axios.get("http://" + backendServerURL + "/actuator/health");
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