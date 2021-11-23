import React from 'react';
import PropTypes from 'prop-types';
import styles from './Dashboard.module.css';

const Dashboard = () => (
  <div className={styles.Dashboard} data-testid="Dashboard">
    Dashboard Component
      Można użyć "card" z Boostrapa
      <div className={styles.toBeWatered}>
          <span>See plants that should be watered in next 3 days</span>
      </div>
      <br/>
      <div className={styles.toBeWatered}> {/*this should be collapsible*/}
          <span>Your plants:</span>
      </div>
  </div>
);

Dashboard.propTypes = {};

Dashboard.defaultProps = {};

export default Dashboard;
