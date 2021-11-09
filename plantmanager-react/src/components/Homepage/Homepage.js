import React from 'react';
import PropTypes from 'prop-types';
import styles from './Homepage.module.css';
import Button from 'react-bootstrap/Button';

class Homepage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div className={styles.Homepage} data-testid="Homepage">
                Homepage Component
                    <div className={styles.homepageView}>
                        <label>
                            Have you recently killed the plant<br />
                            because you actually forgot about it?<br />
                            It won't happen again with your Plant Manager!<br />
                            Your plants will be grateful for logging in<br />
                            (and we will send you notifications).
                            <br />
                            <br />
                            What would you like to do now?
                        </label>
                        <Button className={styles.buttonGoTo}>Sign me in</Button>
                        <Button className={styles.buttonGoTo}>I'm new here, sign me up</Button>
                    </div>
            </div>
        )
    }
}

export default Homepage;
