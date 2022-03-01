import axios from "axios"

const RegistrationService = {
    createUser: (username, password, repeatPassword, email) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

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

            return axios.post("http://" + backendServerURL + "/sign-up/v2", userData, config);
        }
    }
};

export default RegistrationService;