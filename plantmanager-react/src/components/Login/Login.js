import React, {useState} from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import Button from 'react-bootstrap/Button';
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
        <Form.Label htmlFor="username" className={styles.loginInput}>Username</Form.Label>
        <Form.Control id="username" type="text" value={username} className={styles.loginInput}
                      onChange={event => setUsername(event.target.value)}/>
        <Form.Label htmlFor="password" className={styles.loginInput}>Password</Form.Label>
        <Form.Control id="password" type="password" value={password} className={styles.loginInput}
                      onChange={event => setPassword(event.target.value)}/>
        <Form.Label htmlFor="repeatPassword" className={styles.loginInput}>Repeat password</Form.Label>
        <Form.Control id="repeatPassword" type="password" value={repeatPassword}
                      className={styles.loginInput}
                      onChange={event => setRepeatPassword(event.target.value)}/>
        <Form.Label htmlFor="email" className={styles.loginInput}>Email address</Form.Label>
        <Form.Control id="email" type="email" value={email} className={styles.loginInput}
                      onChange={event => setEmail(event.target.value)}/>
        <Button type="submit" className={styles.loginInput}>Sign Up</Button>
    </>;

    const signInForm = <>
        <Form.Label htmlFor="username" className={styles.loginInput}>Username</Form.Label>
        <Form.Control id="username" type="text" value={username} className={styles.loginInput}
                      onChange={event => setUsername(event.target.value)}/>
        <Form.Label htmlFor="password" className={styles.loginInput}>Password</Form.Label>
        <Form.Control id="password" type="password" value={password} className={styles.loginInput}
                      onChange={event => setPassword(event.target.value)}/>
        <Form.Check id="keepSignedIn">
            <Form.Check.Input type="checkbox" defaultChecked={true}/>
            <Form.Label className={styles.loginInput} style={{display: "inline-block", marginTop: "1.2em"}}>Keep me
                signed in</Form.Label>
        </Form.Check>
        <Button type="submit" className={styles.loginInput} style={{marginTop: '15%'}}>Sign In</Button>
    </>


    return (
        <div className={styles.Login} data-testid="Login">
            Login Component
            <div>
                <Form onSubmit={event => handleSubmit(event)}>
                    <div className={styles.loginForm}>
                        <Form.Check hidden={true} style={{display: 'none'}} inline id={signIn} name="choice"
                                    type="radio"
                                    defaultChecked={whichSelected === signIn} onChange={event => goToSubpage(event)}/>
                        <Form.Label htmlFor={signIn}
                                    style={whichSelected === signIn ? selectedBorderStyle : unselectedBorderStyle}
                                    className={styles.choice}>Sign In</Form.Label>
                        <Form.Check hidden={true} inline id={signUp} name="choice" type="radio"
                                    defaultChecked={whichSelected === signUp} onChange={event => goToSubpage(event)}/>
                        <Form.Label htmlFor={signUp}
                                    style={whichSelected === signUp ? selectedBorderStyle : unselectedBorderStyle}
                                    className={styles.choice}>Sign Up</Form.Label>
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
