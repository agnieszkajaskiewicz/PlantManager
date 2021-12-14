import axios from "axios"
import Login from '../../components/Login/Login';

const RegisterService = {

    createUser: (username, password, repeatPassword, email) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        if (username && password && repeatPassword && email) {
            const registerUser = () => {
                const config = {
                    withCredentials: true
                }
                const userData = {
                    username,
                    password,
                    repeatPassword,
                    email
                }

                return axios.post("http://" + backendServerURL + "/sign-up/v2", userData, config);
            }

            registerUser()
                .then((response) => response.json())
                .then((data) => {
                    if (data.fieldErrors) {
                        data.fieldErrors.forEach(fieldError => {

                            if (fieldError.field === 'username') {
                                Login.setUsernameError(fieldError.message);
                            }     

                            if (fieldError.field === 'password') {
                                Login.setPasswordError(fieldError.message);
                            }

                            if (fieldError.field === 'repeatPassword') {
                                Login.setRepeatPasswordError(fieldError.message);
                            }

                            if (fieldError.field === 'email') {
                                Login.setEmailError(fieldError.message);
                            }
                    });
                } else {
                    axios.get("http://" + backendServerURL + "/dashboard");
                }    
                })
                .catch((error) => error);    
        }
    }
};

export default RegisterService;