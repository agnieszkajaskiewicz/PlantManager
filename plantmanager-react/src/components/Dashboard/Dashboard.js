import React, {useState} from 'react';
import PropTypes from 'prop-types';
import styles from './Dashboard.module.css';
import {Collapse} from "react-bootstrap";

const Dashboard = () => {
    const [open, setOpen] = useState(true);
    return (


  <div className={styles.Dashboard} data-testid="Dashboard">
    Dashboard Component
      Można użyć "card" z Boostrapa
      <div className={styles.toBeWatered}>
          <span>See plants that should be watered in next 3 days</span>
      </div>
      <br/>
      <div className={styles.toBeWatered} data-toggle="collapse"  data-target="#plants">
          <input id="collapseCheck" className={styles.toggle} hidden={true} type="checkbox" defaultChecked={false}/>
          <label htmlFor="collapseCheck" className={styles.lblToggle} onClick={() => setOpen(!open)}>Your plants:</label>
      </div>
      <Collapse in={open}>
          <div className={styles.container} id="plants">
              Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. Nihil anim
              keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident.
          </div>
      </Collapse>
  </div>
)};

Dashboard.propTypes = {};

Dashboard.defaultProps = {};

export default Dashboard;
