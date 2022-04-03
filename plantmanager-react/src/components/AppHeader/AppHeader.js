import React from 'react';
import styles from './AppHeader.module.css';
import logo from '../../logoPlantManager.png';
import {useLocation, useNavigate} from "react-router-dom";
import {useDependencies} from '../../DependencyContext';

const AppHeader = () => {

    const navigate = useNavigate();
    const { pathname } = useLocation();
    const { authService } = useDependencies();
    //todo think if trick with pathname is the best idea; however we have to take into consideration that from any other component, we wont have access to AppHeader css

    const handleLogout = () => {
        authService.logoutUser()
            .then(response => {
                if (response.status === 200) {
                    localStorage.removeItem('username');
                    navigate('/login/signIn?logout');
                }
            })             
    }

    var username = localStorage.getItem('username');

    const appHeader = <>
        <div>
            <img id="logo" src={logo} className={styles.appLogo} alt="logo" onClick={() => navigate('/')}/>
        </div>
    </>

    const userHeader = <>
        <div className={styles.userHeader} data-testid="AppHeader">
            <div className={styles.logoPanel}>
                <img id="logo" src={logo} className={styles.appLogoLeft} alt="logo" onClick={() => navigate('/dashboard')}/>
            </div>
            <div className={styles.userPanel}>
                <div>
                    <label className={styles.text}>Username: {username}</label>
                </div>
                <div>
                    <span className={styles.linkElement} onClick={() => handleLogout()}>Logout</span>
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
