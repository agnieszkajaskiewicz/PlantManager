import React, {useState} from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import '../../App.css';
import Form from 'react-bootstrap/Form';
import {useParams, useNavigate} from "react-router-dom";


const selectedBorderStyle = {
    borderBottom: '2px solid',
    borderBlockColor: '#154030'
};

const unselectedBorderStyle = {
    borderBottom: 'transparent'
};


const Login = () => {
    const {where} = useParams();
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [email, setEmail] = useState('');
    const [whichSelected, setWhichSelected] = useState(where);

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

    const signUpForm = <>
        <label htmlFor="username" className="formLabel">Username</label>
        <input id="username" type="text" value={username} className="formInput"
                      onChange={event => setUsername(event.target.value)}/>
        <label htmlFor="password" className="formLabel">Password</label>
        <input id="password" type="password" value={password} className="formInput"
                      onChange={event => setPassword(event.target.value)}/>
        <label htmlFor="repeatPassword" className="formLabel">Repeat password</label>
        <input id="repeatPassword" type="password" value={repeatPassword}
                      className="formInput"
                      onChange={event => setRepeatPassword(event.target.value)}/>
        <label htmlFor="email" className="formLabel">Email address</label>
        <input id="email" type="email" value={email} className="formInput"
                      onChange={event => setEmail(event.target.value)}/>
        <button type="submit" className="appButton">Sign Up</button>
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
        <button type="submit" className="appButton">Sign In</button>

        <div className={styles.linkContainer}>
            <span className={styles.linkElement} onClick={() => navigate('/forgotPassword')}>Forgot password?</span>
        </div>
    </>


    return (
        <div className="mainContainer" data-testid="Login">
            <div>
                <Form onSubmit={event => handleSubmit(event)}>
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
                </Form>
            </div>
        </div>
    )
}

Login.propTypes = {};

Login.defaultProps = {};

export default Login;
