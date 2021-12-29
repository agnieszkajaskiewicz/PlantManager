import React, {useState} from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import '../../App.css';
import Form from 'react-bootstrap/Form';
import {useParams, useNavigate} from "react-router-dom";
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

    const signIn = 'signIn';
    const signUp = 'signUp';

    const handleSubmit = (event) => { //ta logika obecnie "obsługuje" tylko sign up form
        alert('Podano następujące dane: ' + username + ' ' + email + ' ' + password + ' ' + repeatPassword);
        event.preventDefault();
    }

    const goToSubpage = (event) => {
        navigate('/login/' + event.target.id);
        setWhichSelected(event.target.id);
    }

    const loginUser = (username, password) => {
        authService.authUser(username, password);
    }

    const registerUser = (username, password, repeatPassword, email) => {
        registrationService.createUser(username, password, repeatPassword, email)
            .then(response => {
                console.log(response);
            })
            .catch(error => {
                console.log("wrócił błąd");
                console.log(error.response);

                error.response.data.fieldErrors.forEach(fieldError => { //todo dokończyć
                    if (fieldError.field === "username") {
                        setUsernameError(fieldError.message)
                    }

                    if (fieldError.field === "password") {
                        setPasswordError(fieldError.message)
                    }
                    
                    if (fieldError.field === "email") {
                        setEmailError(fieldError.message)
                    }
                })
            })
    }

    const processUsernameInput = (event) => {
        const providedUsername = event.target.value;
        const errorMessage = validationService.validateUsername(providedUsername);
        if (errorMessage) {
            setUsernameError(errorMessage.message);
        } else {
            setUsernameError('');
        }
        setUsername(providedUsername);
    }

    const processPasswordInput = (event) => {
        const providedPassword = event.target.value;
        const errorMessage = validationService.validatePassword(providedPassword);
        if (errorMessage) {
            setPasswordError(errorMessage.message);
        } else {
            setPasswordError('');
        }
        setPassword(providedPassword);
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
        const errorMessage = validationService.validateEmail(email);
        if (errorMessage) {
            setEmailError(errorMessage.message);
        } else {
            setEmailError('');
        }
        setEmail(providedEmail);
    }

    const invalidData = !username || !password || !repeatPassword || !email || usernameError || passwordError || repeatPasswordError || emailError;

    const signUpForm = <>
        <label htmlFor="username" className="formLabel">Username</label>
        <input id="username" type="text" value={username} className="formInput"
                      onChange={event => processUsernameInput(event)} required/>
        {
            usernameError ? <span className="errorMessage">{usernameError}</span> : ''
        }               
        <label htmlFor="password" className="formLabel">Password</label>
        <input id="password" type="password" value={password} className="formInput" 
                      onChange={event => processPasswordInput(event)} required/>
        {
            passwordError ? <span className="errorMessage">{passwordError}</span> : ''
        }               
        <label htmlFor="repeatPassword" className="formLabel">Repeat password</label>
        <input id="repeatPassword" type="password" value={repeatPassword}
                      className="formInput"
                      onChange={event => processRepeatPasswordInput(event)} required/>
        {
            repeatPasswordError ? <span className="errorMessage">{repeatPasswordError}</span> : ''
        }               
        <label htmlFor="email" className="formLabel">Email address</label>
        <input id="email" type="email" value={email} className="formInput"
                      onChange={event => processEmailInput(event)} required/>
        {
            emailError ? <span className="errorMessage">{emailError}</span> : ''
        }
        <button type="submit" className="appButton" disabled={ invalidData } onClick={() => registerUser(username, password, repeatPassword, email)}>Sign Up</button>
    </>;

    const signInForm = <>
        <label htmlFor="username" className="formLabel">Username</label>
        <input id="username" type="text" value={username} className="formInput"
                      onChange={event => setUsername(event.target.value)}/>
        <label htmlFor="password" className="formLabel">Password</label>
        <input id="password" type="password" value={password} className="formInput"
                      onChange={event => setPassword(event.target.value)}/>
        <Form.Check id="keepSignedIn">
            <Form.Check.Input type="checkbox" defaultChecked={true}/>
            <label className={styles.checkboxLabel}>Keep me signed in</label>
        </Form.Check>
        <button type="submit" className="appButton" onClick={() =>loginUser(username, password)}>Sign In</button>

        <div className={styles.linkContainer}>
            <span className={styles.linkElement} onClick={() => navigate('/forgotPassword')}>Forgot password?</span>
        </div>
    </>


    return (
        <div className="mainContainer" data-testid="Login">
            <div>
                {/*<form onSubmit={() => loginUser(username, password)}>*/}
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
                {/*</form>*/}
            </div>
        </div>
    )
}

Login.propTypes = {};

Login.defaultProps = {};

export default Login;
