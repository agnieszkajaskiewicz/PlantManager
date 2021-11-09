import React from 'react';
import PropTypes from 'prop-types';
import styles from './ForgotPassword.module.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

class ForgotPassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: ''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.id]: event.target.value});
    }

    handleSubmit(event) {
            alert('Podano następujące dane: ' + this.state.email);
            event.preventDefault();
        }

    render() {
        return (
            <div className={styles.ForgotPassword} data-testid="ForgotPassword">
                Forgot Password Component
                    <div>
                        <Form onSubmit={this.handleSubmit}>
                            <div className={styles.forgotPasswordForm}>
                                <Form.Label className={styles.formTitle}>Forgot your password?</Form.Label>
                                <Form.Label className={styles.formLabel}>Please insert your email in the input below and we will send you the link to reset your password.</Form.Label>
                                <Form.Label htmlFor="email" className={styles.emailInput}>Email address</Form.Label>
                                <Form.Control id="email" type="email" value={this.state.email} className={styles.emailInput} onChange={this.handleChange}/>
                                <Button type="submit" className={styles.emailInput}>Send</Button>
                            </div>
                        </Form>
                    </div>
            </div>
        )
    }
}

export default ForgotPassword;
