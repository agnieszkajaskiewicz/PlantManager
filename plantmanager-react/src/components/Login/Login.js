import React from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({username: event.target.value});
    }

    handleSubmit(event) {
        alert('Podano następujący username: ' + this.state.username);
        event.preventDefault();
    }

    render() {
        return (
            <div className={styles.Login} data-testid="Login">
                Login Component
                <div>
                    <Form onSubmit={this.handleSubmit}>
                        <div className={styles.loginForm}>
                            <Form.Check inline id="choice-1" name="choice" type="radio" defaultChecked={true}/>
                            <Form.Label htmlFor="choice-1" className={styles.choice}>Sign In</Form.Label>
                            <Form.Check inline id="choice-2" name="choice" type="radio"/>
                            <Form.Label htmlFor="choice-2" className={styles.choice}>Sign Up</Form.Label>
                            <label htmlFor="username" >Username</label>
                            <input type="text" value={this.state.username} onChange={this.handleChange}/>
                            <Button type="submit" variant="success">Wyślij</Button>
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
