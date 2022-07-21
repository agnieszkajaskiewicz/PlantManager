import React, {useState} from 'react';
import styles from './Login.module.css';
import '../../App.css';
import Form from 'react-bootstrap/Form';
import {useParams, useNavigate, useSearchParams} from "react-router-dom";
import {useDependencies} from '../../DependencyContext';


const selectedBorderStyle = {
    borderBottom: '2px solid',
    borderBlockColor: '#154030'
};

const unselectedBorderStyle = {
    borderBottom: 'transparent'
};


const Login = () => {
    const {authService, registrationService, validationService} = useDependencies();

    const {where} = useParams();
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [email, setEmail] = useState('');
    const [whichSelected, setWhichSelected] = useState(where);
    const [usernameError, setUsernameError] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [repeatPasswordError, setRepeatPasswordError] = useState('');
    const [emailError, setEmailError] = useState('');
    const [searchParams] = useSearchParams();

    const signIn = 'signIn';
    const signUp = 'signUp';

    const goToSubpage = (event) => {
        navigate('/login/' + event.target.id);
        setWhichSelected(event.target.id);
    }

    const loginUser = (username, password) => {
        authService.authUser(username, password)
            .then(response => {
                if (response.status === 200) {
                    localStorage.setItem('username', response.headers.username);
                    navigate('/dashboard');
                }
            })             
            .catch(error => {
                if (error.response.status === 401) {
                    setPasswordError('Invalid username or password');
                }
            });
    }

    const registerUser = (username, password, repeatPassword, email) => {
        registrationService.createUser(username, password, repeatPassword, email)
            .then(response => {
                if (response.status === 200) {
                    localStorage.setItem('username', username);
                    navigate('/dashboard');
                }
            })
            .catch(error => {
                error.response.data.map(fieldError => {
                    if (fieldError.field === "username") {
                        setUsernameError(fieldError.defaultMessage)
                    }

                    if (fieldError.field === "password") {
                        setPasswordError(fieldError.defaultMessage)
                    }

                    if (fieldError.field === "repeatPassword") {
                        setRepeatPasswordError(fieldError.defaultMessage)
                    }
                    
                    if (fieldError.field === "email") {
                        setEmailError(fieldError.defaultMessage)
                    }
                })
            })
    }

    const processUsernameInput = (event) => {
        const providedUsername = event.target.value;
        processFieldInput(providedUsername, validationService.validateUsername, setUsername, setUsernameError);
    }

    const processPasswordInput = (event) => {
        const providedPassword = event.target.value;
        processFieldInput(providedPassword, validationService.validatePassword, setPassword, setPasswordError);
    }

    const processRepeatPasswordInput = (event) => {
        const providedRepeatPassword = event.target.value;
        const errorMessage = validationService.validateRepeatPassword(password, providedRepeatPassword);
        if (errorMessage) {
            setRepeatPasswordError(errorMessage.message);
        } else {
            setRepeatPasswordError('');
        }
        setRepeatPassword(providedRepeatPassword);
    }

    const processEmailInput = (event) => {
        const providedEmail = event.target.value;
        processFieldInput(providedEmail, validationService.validateEmail, setEmail, setEmailError);
    }

    const processSignInUsernameInput = (event) => {
        const providedUsername = event.target.value;
        processFieldInput(providedUsername, validationService.validateSignInUsername, setUsername, setUsernameError);
    }

    const processSignInPasswordInput = (event) => {
        const providedPassword = event.target.value;
        processFieldInput(providedPassword, validationService.validateSignInPassword, setPassword, setPasswordError);
    }

    const processFieldInput = (fieldValue, validationMethod, setFieldValueMethod, setFieldErrorMethod) => {
        const errorMessage = validationMethod(fieldValue);
        if (errorMessage) {
            setFieldErrorMethod(errorMessage.message);
        } else {
            setFieldErrorMethod('');
        }

        setFieldValueMethod(fieldValue);
    }

    const invalidSignUpData = !username || !password || !repeatPassword || !email || usernameError || passwordError || repeatPasswordError || emailError;

    const invalidSignInData = !username || !password || usernameError || passwordError;

    const logoutInfo = "User logged out successfully.";

    const signUpForm = <>
        <label htmlFor="username" className="formLabel">Username</label>
        <input id="username" type="text" value={username} className="formInput"
                      onChange={event => processUsernameInput(event)} required/>
        {
            usernameError ? <span className="text-danger">{usernameError}</span> : ''
        }               
        <label htmlFor="password" className="formLabel">Password</label>
        <input id="password" type="password" value={password} className="formInput" 
                      onChange={event => processPasswordInput(event)} required/>
        {
            passwordError ? <span className="text-danger">{passwordError}</span> : ''
        }               
        <label htmlFor="repeatPassword" className="formLabel">Repeat password</label>
        <input id="repeatPassword" type="password" value={repeatPassword}
                      className="formInput"
                      onChange={event => processRepeatPasswordInput(event)} required/>
        {
            repeatPasswordError ? <span className="text-danger">{repeatPasswordError}</span> : ''
        }               
        <label htmlFor="email" className="formLabel">Email address</label>
        <input id="email" type="email" value={email} className="formInput"
                      onChange={event => processEmailInput(event)} required/>
        {
            emailError ? <span className="text-danger">{emailError}</span> : ''
        }
        <button type="submit" className="appButton" disabled={invalidSignUpData} onClick={() => registerUser(username, password, repeatPassword, email)}>Sign Up</button>
    </>;

    const signInForm = <>
        <label htmlFor="username" className="formLabel">Username</label>
        <input id="username" type="text" value={username} className="formInput"
                      onChange={event => processSignInUsernameInput(event)}/>
        {
            usernameError ? <span className="text-danger">{usernameError}</span> : ''
        }               
        <label htmlFor="password" className="formLabel">Password</label>
        <input id="password" type="password" value={password} className="formInput"
                      onChange={event => processSignInPasswordInput(event)}/>
        {
            passwordError ? <span className="text-danger">{passwordError}</span> : ''
        }  
        {
            searchParams.get("logout") != null ? <span className="text-warning">{logoutInfo}</span> : ''
        }  
        <Form.Check id="keepSignedIn">
            <Form.Check.Input type="checkbox" defaultChecked={true}/>
            <label className={styles.checkboxLabel}>Keep me signed in</label>
        </Form.Check>
        <button type="submit" className="appButton" disabled={invalidSignInData} onClick={() => loginUser(username, password)}>Sign In</button>

        <div className={styles.linkContainer}>
            <span className={styles.linkElement} onClick={() => navigate('/forgotPassword')}>Forgot password?</span>
        </div>
    </>


    return (
        <div className="mainContainer" data-testid="Login">
            <div>
                <div className="formContainer">
                    <Form.Check hidden={true} style={{display: 'none'}} inline id={signIn} name="choice"
                                type="radio"
                                defaultChecked={whichSelected === signIn} onChange={event => goToSubpage(event)}/>
                    <label htmlFor={signIn}
                                style={whichSelected === signIn ? selectedBorderStyle : unselectedBorderStyle}
                                className={styles.choice}>Sign In</label>
                    <Form.Check hidden={true} inline id={signUp} name="choice" type="radio"
                                defaultChecked={whichSelected === signUp} onChange={event => goToSubpage(event)}/>
                    <label htmlFor={signUp}
                                style={whichSelected === signUp ? selectedBorderStyle : unselectedBorderStyle}
                                className={styles.choice}>Sign Up</label>
                        {whichSelected === signIn ? signInForm : signUpForm}
                </div>
            </div>
        </div>
    )
}

Login.propTypes = {};

Login.defaultProps = {};

export default Login;
