import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import styles from './Dashboard.module.css';
import {Card, Collapse} from "react-bootstrap";

import addIcon from '../../img/addIcon.png';
import bin from '../../img/bin.png';

import {useDependencies} from '../../DependencyContext';

const Dashboard = () => {
    const [open, setOpen] = useState(true);
    const [data, setData] = useState({
        plants: [],
        isFetching: false
    });
    const {plantService} = useDependencies();

    useEffect(() => {
        const fetchPlantsForLoggedUser = async () => {
            try {
                setData({plants: data.plants, isFetching: true});
                const response = await plantService.fetchPlantsForLoggedUser();
                //debugger;
                setData({plants: response.data, isFetching: false});
            } catch (exception) {
                console.log(exception);
                //todo obsługa błędów
            }
        };
        fetchPlantsForLoggedUser();
    }, []);


    return (


        <div className={styles.Dashboard} data-testid="Dashboard">
            Dashboard Component
            <div className={styles.toBeWatered}>
                <span>See plants that should be watered in next 3 days</span>
            </div>
            <br/>
            <div className={styles.toBeWatered} data-toggle="collapse" data-target="#plants">
                <input id="collapseCheck" className={styles.toggle} hidden={true} type="checkbox"
                       defaultChecked={false}/>
                <label htmlFor="collapseCheck" className={styles.lblToggle} onClick={() => setOpen(!open)}>Your
                    plants:</label>
            </div>
            <Collapse in={open}>
                <div className={styles.container} id="plants">
                    <Card className={styles.cardContainer}>
                        <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
                        <Card.Body className={styles.plantCard}>
                            <Card.Title>Pan Tadeusz</Card.Title>
                            <Card.Text>
                                Litwo
                                <br/>
                                Ojczyzno moja
                                <br/>
                                Ty jesteś jak zdrowie
                                <br/>
                                Ile trzeba cenić
                            </Card.Text>
                            <button className="appButton" style={ {width: 'auto', display: 'inline'} }>ADD</button>
                        </Card.Body>
                    </Card>

                    <Card className={styles.cardContainer}>
                        <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
                        <Card.Body className={styles.plantCard}>
                            <Card.Title>Pan Tadeusz</Card.Title>
                            <Card.Text>
                                Litwo
                                <br/>
                                Ojczyzno moja
                                <br/>
                                Ty jesteś jak zdrowie
                                <br/>
                                Ile trzeba cenić
                            </Card.Text>
                            <button className="appButton" style={ {width: 'auto', display: 'inline'} }>ADD</button>
                        </Card.Body>
                    </Card>

                    <Card className={styles.cardContainer}>
                        <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
                        <Card.Body className={styles.plantCard}>
                            <Card.Title>Pan Tadeusz</Card.Title>
                            <Card.Text>
                                Litwo
                                <br/>
                                Ojczyzno moja
                                <br/>
                                Ty jesteś jak zdrowie
                                <br/>
                                Ile trzeba cenić
                            </Card.Text>
                            <button className="appButton" style={ {width: 'auto', display: 'inline'} }>EDIT</button>
                            <img src={bin} alt="Remove plant" className={styles.removeImg}/>
                        </Card.Body>
                    </Card>
                </div>
            </Collapse>

        </div>
    )
};

Dashboard.propTypes = {};

Dashboard.defaultProps = {};

export default Dashboard;
