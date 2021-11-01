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
        return (
            <div className={styles.Login} data-testid="Login">
                Login Component
                <div>
                    <Form onSubmit={this.handleSubmit}>
                        <div className={styles.loginForm}>
                            <Form.Check hidden={true} inline id="choice-1" name="choice" type="radio" defaultChecked={true} onChange={this.handleSelect}/>
                            <Form.Label htmlFor="choice-1" style={this.state.whichSelected === 'choice-1' ? selectedBorderStyle : unselectedBorderStyle } className={styles.choice}>Sign In</Form.Label>
                            <Form.Check hidden={true} inline id="choice-2" name="choice" type="radio" onChange={this.handleSelect}/>
                            <Form.Label htmlFor="choice-2" style={this.state.whichSelected === 'choice-2' ? selectedBorderStyle : unselectedBorderStyle } className={styles.choice}>Sign Up</Form.Label>
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
