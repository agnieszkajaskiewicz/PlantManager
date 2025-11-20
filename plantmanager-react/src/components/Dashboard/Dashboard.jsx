import React, {useEffect, useState} from 'react';
import styles from './Dashboard.module.css';
import {Collapse} from "react-bootstrap";
import PlantCard from '../PlantCard/PlantCard';
import ErrorHandler from "../ErrorHandler/ErrorHandler";

import {useDependencies} from '../../DependencyContext';

const Dashboard = () => {
    const [openYourPlants, setOpenYourPlants] = useState(true);
    const [openToBeWatered, setOpenToBeWatered] = useState(true);
    const [data, setData] = useState({
        plants: [],
        isFetching: false
    });
    const [plantsToBeWatered, setPlantsToBeWatered] = useState({
        plants: [],
        isFetching: false
    });
    const [apiError, setApiError] = useState('');
    const {plantService} = useDependencies();

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
    
    const fetchPlantsToBeWatered = async () => {
        try {
            setPlantsToBeWatered({plants: plantsToBeWatered.plants, isFetching: true});
            const response = await plantService.fetchPlantsToBeWateredSoon();
            setPlantsToBeWatered({plants: response.data, isFetching: false});
        } catch (exception) {
            console.log(exception);
            //todo obsługa błędów
        }
    };

    const handleWateringConfirmed = () => {
        fetchPlantsForLoggedUser();
        fetchPlantsToBeWatered();
    };

    useEffect(() => {
        fetchPlantsForLoggedUser();
        fetchPlantsToBeWatered();
    }, []);

    const plantCards = data.plants.map((plant, index) => <PlantCard key={index} plantData={plant}
                                                                    setApiError={setApiError}
                                                                    onWateringConfirmed={handleWateringConfirmed}/>);
    
    const plantsToBeWateredCards = plantsToBeWatered.plants.map((plant, index) => 
        <PlantCard key={index} plantData={plant} setApiError={setApiError} onWateringConfirmed={handleWateringConfirmed}/>);

    return (
        <div className={styles.Dashboard} data-testid="Dashboard">
            {apiError !== '' && <ErrorHandler message={apiError} />}
            
            <div className={styles.toBeWatered} data-toggle="collapse" data-target="#plantsToBeWatered">
                <input id="collapseCheckToBeWatered" className={styles.toggle} hidden={true} type="checkbox"
                       defaultChecked={true}/>
                <label htmlFor="collapseCheckToBeWatered" className={styles.lblToggle} 
                       onClick={() => setOpenToBeWatered(!openToBeWatered)}>
                    Plants to be watered soon (next 3 days):
                </label>
            </div>
            <Collapse in={openToBeWatered}>
                <div className={styles.container} id="plantsToBeWatered">
                    {plantsToBeWateredCards.length > 0 ? plantsToBeWateredCards : 
                        <div className={styles.emptyMessage}>No plants need watering in the next 3 days</div>}
                </div>
            </Collapse>
            
            <br/>
            <div className={styles.toBeWatered} data-toggle="collapse" data-target="#plants">
                <input id="collapseCheck" className={styles.toggle} hidden={true} type="checkbox"
                       defaultChecked={true}/>
                <label htmlFor="collapseCheck" className={styles.lblToggle} 
                       onClick={() => setOpenYourPlants(!openYourPlants)}>Your plants:</label>
            </div>
            <Collapse in={openYourPlants}>
                <div className={styles.container} id="plants">
                    {plantCards}
                    <PlantCard plantData={null} setApiError={setApiError}/>
                </div>
            </Collapse>

        </div>
    )
};

Dashboard.propTypes = {};

Dashboard.defaultProps = {};

export default Dashboard;
