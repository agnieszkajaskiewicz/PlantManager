import React from 'react';
import styles from './ErrorHandler.module.css';

const ErrorHandler = (props) => (
    <div className={styles.ErrorHandler} data-testid="ErrorHandler">
        Niestety, wystąpił błąd: {props.message}
    </div>
);

ErrorHandler.propTypes = {};

ErrorHandler.defaultProps = {};

export default ErrorHandler;
