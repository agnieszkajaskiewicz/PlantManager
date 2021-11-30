import axios from "axios"

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
                .then(
                    response => {
                        axios.get("http://" + backendServerURL + "/dashboard", {withCredentials: true} )
                        .then(response => {
                            console.log(response);
                        })
                        .catch(error => {
                            console.log(error.response)
                        })
                    }
                ).catch(error => {
                    console.log(error);
                });

        }
    }
};

export default RegisterService;