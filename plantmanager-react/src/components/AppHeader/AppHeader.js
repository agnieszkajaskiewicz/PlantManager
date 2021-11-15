import React from 'react';
import PropTypes from 'prop-types';
import styles from './AppHeader.module.css';
import logo from '../../logoPlantManager.png';
import {useNavigate} from "react-router-dom";

const AppHeader = () => {

    const navigate = useNavigate();

    return (
        <div data-testid="AppHeader">
            <img src={logo} className={styles.AppLogo} alt="logo" onClick={() => navigate('/')}/>
        </div>
    )

}

export default AppHeader;
