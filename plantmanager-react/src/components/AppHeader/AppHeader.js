import React from 'react';
import PropTypes from 'prop-types';
import styles from './AppHeader.module.css';
import logo from '../../logoPlantManager.png';
import {useLocation, useNavigate} from "react-router-dom";

const AppHeader = () => {

    const navigate = useNavigate();
    const { pathname } = useLocation();
    //todo think if trick with pathname is the best idea; however we have to take into consideration that from any other component, we wont have access to AppHeader css
    return (
        <div data-testid="AppHeader">
            <img id="logo" src={logo} className={pathname === '/dashboard' ? styles.AppLogoLeft : styles.AppLogo} alt="logo" onClick={() => navigate('/')}/>
        </div>
    )

}

export default AppHeader;
