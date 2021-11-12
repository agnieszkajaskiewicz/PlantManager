import React from 'react';
import PropTypes from 'prop-types';
import styles from './Homepage.module.css';
import {useNavigate} from "react-router-dom";
import '../../App.css';

const Homepage = () => {
    const navigate = useNavigate();

    const goToLoginPage = (where) => {
        navigate('/login/' + where); //todo add different pages for different params
    }

    return (
        <div className="mainContainer" data-testid="Homepage">
            <div className="formContainer">
                <label className={styles.text}>
                    Have you recently killed the plant<br/>
                    because you actually forgot about it?<br/>
                    It won't happen again with your Plant Manager!<br/>
                    Your plants will be grateful for logging in<br/>
                    (and we will send you notifications).
                    <br/>
                    <br/>
                    What would you like to do now?
                </label>

                <button className="appButton" onClick={() => goToLoginPage("signIn")}>Sign me in</button>
                <button className="appButton" onClick={() => goToLoginPage("signUp")}>I'm new here, sign me up</button>
            </div>
        </div>
    )
}


export default Homepage;
