import React, { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import PasswordService from '../../services/PasswordService/PasswordService';
import styles from './ResetPassword.module.css';
import '../../App.css';

function ResetPassword() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const token = searchParams.get('token');

    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');
    const [loading, setLoading] = useState(false);
    const [validatingToken, setValidatingToken] = useState(true);
    const [tokenValid, setTokenValid] = useState(false);
    const [passwordReset, setPasswordReset] = useState(false);

    useEffect(() => {
        validateToken();
    }, [token]);

    const validateToken = async () => {
        if (!token) {
            setError('No reset token provided');
            setValidatingToken(false);
            return;
        }

        setValidatingToken(true);
        const result = await PasswordService.validateResetToken(token);
        
        setTokenValid(result.valid);
        if (!result.valid) {
            setError(result.message);
        }

        setValidatingToken(false);
    };

    const handlePasswordChange = (event) => {
        const value = event.target.value;
        setPassword(value);
        setPasswordError('');
        
        if (value && value.length < 6) {
            setPasswordError('Password must be at least 6 characters');
        }
        
        if (confirmPassword && value !== confirmPassword) {
            setConfirmPasswordError('Passwords do not match');
        } else if (confirmPassword) {
            setConfirmPasswordError('');
        }
    };

    const handleConfirmPasswordChange = (event) => {
        const value = event.target.value;
        setConfirmPassword(value);
        setConfirmPasswordError('');
        
        if (value && password !== value) {
            setConfirmPasswordError('Passwords do not match');
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        setError('');
        setPasswordError('');
        setConfirmPasswordError('');
        
        if (!password || !confirmPassword) {
            setError('Please fill in all fields');
            return;
        }

        if (password.length < 6) {
            setPasswordError('Password must be at least 6 characters');
            return;
        }

        if (password !== confirmPassword) {
            setConfirmPasswordError('Passwords do not match');
            return;
        }

        setLoading(true);

        try {
            const result = await PasswordService.resetPassword(token, password);
            
            if (result.success) {
                setMessage(result.message);
                setPasswordReset(true);
                setPassword('');
                setConfirmPassword('');
                
                setTimeout(() => {
                    navigate('/login/signIn');
                }, 3000);
            } else {
                setError(result.message);
            }
        } catch (error) {
            setError('An unexpected error occurred. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    if (validatingToken) {
        return (
            <div className="mainContainer">
                <div className="formContainer">
                    <p>Validating reset link...</p>
                </div>
            </div>
        );
    }

    if (!tokenValid) {
        return (
            <div className="mainContainer">
                <div className="formContainer">
                    {
                        error ? <span className="text-danger">{error}</span> : ''
                    }
                    <button 
                        className="appButton"
                        onClick={() => navigate('/forgotPassword')}
                    >
                        Request New Link
                    </button>
                </div>
            </div>
        );
    }

    if (passwordReset) {
        return (
            <div className="mainContainer">
                <div className="formContainer">
                    {
                        message ? <span className="text-success">{message}</span> : ''
                    }
                    <p>Redirecting to login...</p>
                </div>
            </div>
        );
    }

    const invalidData = !password || !confirmPassword || passwordError || confirmPasswordError;

    return (
        <div className="mainContainer" data-testid="ResetPassword">
            <div>
                <Form onSubmit={handleSubmit}>
                    <div className="formContainer">
                        <label className={styles.title}>Reset Your Password</label>
                        <label className={styles.text}>
                            Please enter your new password below.
                        </label>
                        
                        {
                            error ? <span className="text-danger">{error}</span> : ''
                        }
                        
                        <label htmlFor="password" className="formLabel">New Password</label>
                        <input 
                            id="password" 
                            type="password" 
                            value={password} 
                            className="formInput" 
                            onChange={handlePasswordChange}
                            disabled={loading}
                            required
                            minLength="6"
                        />
                        {
                            passwordError ? <span className="text-danger">{passwordError}</span> : ''
                        }
                        
                        <label htmlFor="confirmPassword" className="formLabel">Confirm New Password</label>
                        <input 
                            id="confirmPassword" 
                            type="password" 
                            value={confirmPassword} 
                            className="formInput" 
                            onChange={handleConfirmPasswordChange}
                            disabled={loading}
                            required
                            minLength="6"
                        />
                        {
                            confirmPasswordError ? <span className="text-danger">{confirmPasswordError}</span> : ''
                        }
                        
                        <button 
                            type="submit" 
                            className="appButton"
                            disabled={loading || invalidData}
                        >
                            {loading ? 'Resetting...' : 'Reset Password'}
                        </button>
                    </div>
                </Form>
            </div>
        </div>
    );
}

export default ResetPassword;
