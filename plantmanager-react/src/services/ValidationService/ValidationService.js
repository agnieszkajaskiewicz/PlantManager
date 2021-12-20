const usernameMinLength = 6;
const usernameMaxLength = 32;
const passwordMinLength = 6;
const passwordMaxLength = 32;

const emptyFieldMessage = "This field is required."
const wrongLengthMessage = "Please use between 6 and 32 characters."
const passwordsMismatch = "Passwords don't match."

const ValidationService = {
    validateUsername: (username) => {
        if (username === "") {
            return {
                field: 'username',
                message: emptyFieldMessage
            };
        }

        if (username !== "" && (username.length < usernameMinLength || username.length > usernameMaxLength)) {
            return {
                field: 'username',
                message: wrongLengthMessage
            };
        }
    },

    validatePassword: (password) => {
        if (password === "") {
            return {
                field: 'password',
                message: emptyFieldMessage
            };
        }

        if (password !== "" && (password.length < passwordMinLength || password.length > passwordMaxLength)) {
            return {
                field: 'password',
                message: wrongLengthMessage
            };
        }
    },

    validateRepeatPassword: (password, repeatPassword) => {
        if (password !== "" && repeatPassword !== "" && password !== repeatPassword) {
            console.log("password:" + password + " repeatPassword: " + repeatPassword);
            return {
                field: 'repeatPassword',
                message: passwordsMismatch
            };
        }
    }
}

export default ValidationService;
