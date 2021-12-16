const usernameMinLength = 6;
const usernameMaxLength = 32;

const emptyFieldMessage = "This field is required."
const wrongLengthMessage = "Please use between 6 and 32 characters."

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
    }
}

export default ValidationService;