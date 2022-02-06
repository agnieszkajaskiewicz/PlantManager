import React from 'react';
import PropTypes from 'prop-types';
import styles from './AppHeader.module.css';
import logo from '../../logoPlantManager.png';
import {useLocation, useNavigate} from "react-router-dom";

const AppHeader = () => {

    const navigate = useNavigate();
    const { pathname } = useLocation();
    //todo think if trick with pathname is the best idea; however we have to take into consideration that from any other component, we wont have access to AppHeader css

    const appHeader = <>
        <div>
            <img id="logo" src={logo} className={styles.appLogo} alt="logo" onClick={() => navigate('/')}/>
        </div>
    </>

    const userHeader = <>
        <div className={styles.userHeader} data-testid="AppHeader">
            <div className={styles.logoPanel}>
                <img id="logo" src={logo} className={styles.appLogoLeft} alt="logo" onClick={() => navigate('/')}/>
            </div>
            <div className={styles.userPanel}>
                <div>
                    <label className={styles.text}>Username: ajaskiewicz</label>
                </div>
                <div>
                    <label className={styles.text}>Logout</label>
                </div>
            </div>
        </div>
    </>

    return (
        <div data-testid="AppHeader">
            {pathname === '/dashboard' ? userHeader : appHeader}
        </div>
    )

}

export default AppHeader;
