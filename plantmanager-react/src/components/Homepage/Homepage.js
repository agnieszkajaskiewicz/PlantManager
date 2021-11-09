import React from 'react';
import PropTypes from 'prop-types';
import styles from './Homepage.module.css';
import '../../App.css';

class Homepage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="mainContainer" data-testid="Homepage">
                Homepage Component
                    <div className="formContainer">
                        <label className={styles.text}>
                            Have you recently killed the plant<br />
                            because you actually forgot about it?<br />
                            It won't happen again with your Plant Manager!<br />
                            Your plants will be grateful for logging in<br />
                            (and we will send you notifications).
                            <br />
                            <br />
                            What would you like to do now?
                        </label>
                        <button className="App-button">Sign me in</button>
                        <button className="App-button">I'm new here, sign me up</button>
                    </div>
            </div>
        )
    }
}

export default Homepage;
