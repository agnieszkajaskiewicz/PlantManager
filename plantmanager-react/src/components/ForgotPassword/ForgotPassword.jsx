import React from 'react';
import styles from './ForgotPassword.module.css';
import '../../App.css';
import Form from 'react-bootstrap/Form';
import PasswordService from '../../services/PasswordService/PasswordService';

class ForgotPassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            message: '',
            error: '',
            loading: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({
            [event.target.id]: event.target.value,
            error: '',
            message: ''
        });
    }

    async handleSubmit(event) {
        event.preventDefault();
        
        const { email } = this.state;
        
        if (!email) {
            this.setState({ error: 'Please enter your email address' });
            return;
        }

        this.setState({ loading: true, error: '', message: '' });

        try {
            const result = await PasswordService.requestPasswordReset(email);
            
            if (result.success) {
                this.setState({
                    message: result.message,
                    loading: false,
                    email: '' // Clear email
                });
            } else {
                this.setState({
                    error: result.message,
                    loading: false
                });
            }
        } catch (error) {
            this.setState({
                error: 'An unexpected error occurred. Please try again.',
                loading: false
            });
        }
    }

    render() {
        const { email, message, error, loading } = this.state;

        return (
            <div className="mainContainer" data-testid="ForgotPassword">
                <div>
                    <Form onSubmit={this.handleSubmit}>
                        <div className="formContainer">
                            <label className={styles.title}>Forgot your password?</label>
                            <label className={styles.text}>
                                Please insert your email in the input below and we will send you the link to reset your password.
                            </label>
                            
                            <label htmlFor="email" className="formLabel">Email address</label>
                            <input 
                                id="email" 
                                type="email" 
                                value={email} 
                                className="formInput" 
                                onChange={this.handleChange}
                                disabled={loading}
                                required
                            />
                            {
                                error ? <span className="text-danger">{error}</span> : ''
                            }
                            {
                                message ? <span className="text-warning">{message}</span> : ''
                            }
                            <button 
                                type="submit" 
                                className="appButton"
                                disabled={loading}
                            >
                                {loading ? 'Sending...' : 'Send'}
                            </button>
                        </div>
                    </Form>
                </div>
            </div>
        );
    }
}

export default ForgotPassword;
