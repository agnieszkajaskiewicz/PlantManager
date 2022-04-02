import React from 'react';
import PropTypes from 'prop-types';
import styles from './ForgotPassword.module.css';
import '../../App.css';
import Form from 'react-bootstrap/Form';

class ForgotPassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: ''
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.id]: event.target.value});
    }

    render() {
        return (
            <div className="mainContainer" data-testid="ForgotPassword">
                    <div>
                        <Form>
                            <div className="formContainer">
                                <label className={styles.title}>Forgot your password?</label>
                                <label className={styles.text}>Please insert your email in the input below and we will send you the link to reset your password.</label>
                                <label htmlFor="email" className="formLabel">Email address</label>
                                <input id="email" type="email" value={this.state.email} className="formInput" onChange={this.handleChange}/>
                                <button type="submit" className="appButton">Send</button>
                            </div>
                        </Form>
                    </div>
            </div>
        )
    }
}

export default ForgotPassword;
