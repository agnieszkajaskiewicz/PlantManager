import React from 'react';
import PropTypes from 'prop-types';
import styles from './AppHeader.module.css';
import logo from '../../logoPlantManager.png';

class AppHeader extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div data-testid="AppHeader">
                <img src={logo} className={styles.AppLogo} alt="logo" />
            </div>
        )
    }
}

export default AppHeader;
