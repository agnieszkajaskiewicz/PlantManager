import React, {useState} from 'react';
import styles from './PlantCard.module.css';
import {Card} from "react-bootstrap";
import addIcon from "../../img/addIcon.png";
import bin from "../../img/bin.png";
import {useDependencies} from '../../DependencyContext';
//todo responsywny rozmiar czcionki
//todo responsywna szerokość, wysokość przy nadmiernym skurczeniu

const PlantCard = (props) => {
    const [isDeleted, setIsDeleted] = useState(false);
    const {plantService} = useDependencies();

    const deletePlant = (plantId) => {
        plantService.deletePlantById(plantId)
            .then(response => {
                if (response.status === 200) {
                    props.setApiError('');
                }
                setIsDeleted(true);
            })
            .catch(error => {
                const message = error.response.data.message === ''  ? 'Something went wrong :(' : error.response.data.message;
                props.setApiError(message);
            });
    }

    const containerStyle = {
        boxShadow: '2px 2px 6px 0 rgba(0,0,0,0.3)',
        backgroundColor: 'transparent'
    }
    return (
        <Card data-testid="PlantCard" style={isDeleted ? {display: 'none'} : containerStyle}>
            <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
            <Card.Body className={styles.plantCard}>
                <Card.Title data-testid="PlantName">{props.plantData ? props.plantData.plantName : 'Your new plant'}</Card.Title>
                <button className="appButton"
                        style={{width: 'auto', display: 'inline'}}>{props.plantData ? 'EDIT' : 'ADD'}</button>
                {(props.plantData) &&
                    <img src={bin} alt="Remove plant" className={styles.removeImg}
                         onClick={() => deletePlant(props.plantData.id)} data-testid="trashbin"/>
                }
            </Card.Body>
        </Card>
    )
};

PlantCard.propTypes = {};

PlantCard.defaultProps = {};

export default PlantCard;
