import React from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

const selectedBorderStyle = {
    borderBottom: '2px solid',
    borderBlockColor: '#154030'
};

const unselectedBorderStyle = {
    borderBottom: 'transparent'
};

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            whichSelected: 'choice-1' //todo replace hardcoded id with variable
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
    }

    handleChange(event) {
        this.setState({username: event.target.value});
    }

    handleSubmit(event) {
        alert('Podano następujący username: ' + this.state.username);
        event.preventDefault();
    }

    handleSelect(event) {
        this.setState({whichSelected: event.target.id});
    }

    render() {
        const signUpForm = <>
            <Form.Label htmlFor="username" className={styles.loginInput}>Username</Form.Label>
            <Form.Control id="username" type="text" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Form.Label htmlFor="password" className={styles.loginInput}>Password</Form.Label>
            <Form.Control id="password" type="password" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Form.Label htmlFor="repeatPassword" className={styles.loginInput}>Repeat password</Form.Label>
            <Form.Control id="repeatPassword" type="password" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Form.Label htmlFor="email" className={styles.loginInput}>Email address</Form.Label>
            <Form.Control id="email" type="email" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Button type="submit" className={styles.loginInput}>Sign Up</Button></>;

        const signInForm = <>
            <Form.Label htmlFor="username" className={styles.loginInput}>Username</Form.Label>
            <Form.Control id="username" type="text" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Form.Label htmlFor="password" className={styles.loginInput}>Password</Form.Label>
            <Form.Control id="password" type="password" value={this.state.username} className={styles.loginInput}
                          onChange={this.handleChange}/>
            <Form.Check id="keepSignedIn" type="checkbox" className={styles.keepSignedIn} defaultChecked={true}/>
            <Form.Label htmlFor="keepSignedIn" className={styles.loginInput}>Keep me signed in</Form.Label>
        </>
        return (
            <div className={styles.Login} data-testid="Login">
                Login Component
                <div>
                    <Form onSubmit={this.handleSubmit}>
                        <div className={styles.loginForm}>
                            <Form.Check hidden={true} style={{display: 'none'}} inline id="choice-1" name="choice" type="radio"
                                        defaultChecked={true} onChange={this.handleSelect}/>
                            <Form.Label htmlFor="choice-1"
                                        style={this.state.whichSelected === 'choice-1' ? selectedBorderStyle : unselectedBorderStyle}
                                        className={styles.choice}>Sign In</Form.Label>
                            <Form.Check hidden={true} inline id="choice-2" name="choice" type="radio" onChange={this.handleSelect}/>
                            <Form.Label htmlFor="choice-2"
                                        style={this.state.whichSelected === 'choice-2' ? selectedBorderStyle : unselectedBorderStyle}
                                        className={styles.choice}>Sign Up</Form.Label>
                            {this.state.whichSelected === 'choice-1' ? signInForm : signUpForm}
                        </div>
                    </Form>
                </div>
            </div>
        )
    }
}

Login.propTypes = {};

Login.defaultProps = {};

export default Login;
