import React, {useEffect, useState} from 'react';
import styles from './Dashboard.module.css';
import {Collapse} from "react-bootstrap";
import PlantCard from '../PlantCard/PlantCard';
import ErrorHandler from "../ErrorHandler/ErrorHandler";

import {useDependencies} from '../../DependencyContext';

const Dashboard = () => {
    const [open, setOpen] = useState(true);
    const [data, setData] = useState({
        plants: [],
        isFetching: false
    });
    const [apiError, setApiError] = useState('');
    const {plantService} = useDependencies();

    useEffect(() => {
        const fetchPlantsForLoggedUser = async () => {
            try {
                setData({plants: data.plants, isFetching: true});
                const response = await plantService.fetchPlantsForLoggedUser();
                setData({plants: response.data, isFetching: false});
            } catch (exception) {
                console.log(exception);
                //todo obsługa błędów
            }
        };
        fetchPlantsForLoggedUser();
    }, []);

    const plantCards = data.plants.map((plant, index) => <PlantCard key={index} plantData={plant}
                                                                    setApiError={setApiError}/>);

    return (
        <div className={styles.Dashboard} data-testid="Dashboard">
            {apiError !== '' && <ErrorHandler message={apiError} />}
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
                    {plantCards}
                    <PlantCard plantData={{
                        plantName: 'Pejotl'
                    }} setApiError={setApiError} />
                    <PlantCard plantData={null}/>
                </div>
            </Collapse>

        </div>
    )
};

Dashboard.propTypes = {};

Dashboard.defaultProps = {};

export default Dashboard;
