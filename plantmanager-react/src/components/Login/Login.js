import React from 'react';
import PropTypes from 'prop-types';
import styles from './Login.module.css';

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
                    <input id="tab-1" type="radio" name="tab" checked/> {/*todo dziwne nazwy, 'tab'*/}
                    <label htmlFor="tab-1" className={styles.tab}>Sign In</label>
                    <input id="tab-2" type="radio" name="tab" />
                    <label htmlFor="tab-2" className={styles.tab}>Sign Up</label>
                </div>

                <form onSubmit={this.handleSubmit}>
                    <div>
                        <label htmlFor="username" className={styles.label}>Username</label>
                        <input type="text" className={styles.input} value={this.state.username} onChange={this.handleChange}/>
                    </div>
                    <input type="submit" value="Wyślij" />
                </form>
            </div>
        )
    }
}

Login.propTypes = {};

Login.defaultProps = {};

export default Login;
