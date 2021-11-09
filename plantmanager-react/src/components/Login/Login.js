import React from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';
import '../../App.css';
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
            repeatPassword: '',
            email: '',
            whichSelected: 'choice-1' //todo replace hardcoded id with variable
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.id]: event.target.value});
    }

    handleSubmit(event) { //ta logika obecnie "obsługuje" tylko sign up form
        alert('Podano następujące dane: ' + this.state.username + ' ' + this.state.email + ' ' + this.state.password + ' ' + this.state.repeatPassword);
        event.preventDefault();
    }

    handleSelect(event) {
        this.setState({whichSelected: event.target.id});
    }

    render() {
        const signUpForm = <>
            <label htmlFor="username" className="formLabel">Username</label>
            <input id="username" type="text" value={this.state.username} className="formInput"
                          onChange={this.handleChange}/>
            <label htmlFor="password" className="formLabel">Password</label>
            <input id="password" type="password" value={this.state.password} className="formInput"
                          onChange={this.handleChange}/>
            <label htmlFor="repeatPassword" className="formLabel">Repeat password</label>
            <input id="repeatPassword" type="password" value={this.state.repeatPassword}
                          className="formInput"
                          onChange={this.handleChange}/>
            <label htmlFor="email" className="formLabel">Email address</label>
            <input id="email" type="email" value={this.state.email} className="formInput"
                          onChange={this.handleChange}/>
            <button type="submit" className="App-button">Sign Up</button>
        </>;

        const signInForm = <>
            <label htmlFor="username" className="formLabel">Username</label>
            <input id="username" type="text" value={this.state.username} className="formInput"
                          onChange={this.handleChange}/>
            <label htmlFor="password" className="formLabel">Password</label>
            <input id="password" type="password" value={this.state.username} className="formInput"
                          onChange={this.handleChange}/>
            <Form.Check id="keepSignedIn">
                <Form.Check.Input type="checkbox" defaultChecked={true}/>
                <label className="formLabel" style={{display: "inline-block", marginTop: "1.2em"}}>Keep me
                    signed in</label>
            </Form.Check>
            <button type="submit" className="App-button" style={{marginTop: '15%'}}>Sign In</button>
        </>
        return (
            <div className="mainContainer" data-testid="Login">
                Login Component
                <div>
                    <Form onSubmit={this.handleSubmit}>
                        <div className="formContainer">
                            <Form.Check hidden={true} style={{display: 'none'}} inline id="choice-1" name="choice"
                                        type="radio"
                                        defaultChecked={true} onChange={this.handleSelect}/>
                            <label htmlFor="choice-1"
                                        style={this.state.whichSelected === 'choice-1' ? selectedBorderStyle : unselectedBorderStyle}
                                        className={styles.choice}>Sign In</label>
                            <Form.Check hidden={true} inline id="choice-2" name="choice" type="radio"
                                        onChange={this.handleSelect}/>
                            <label htmlFor="choice-2"
                                        style={this.state.whichSelected === 'choice-2' ? selectedBorderStyle : unselectedBorderStyle}
                                        className={styles.choice}>Sign Up</label>
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
