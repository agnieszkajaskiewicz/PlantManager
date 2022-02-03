import React from 'react';
import PropTypes from 'prop-types';
import styles from './PlantCard.module.css';
import {Card} from "react-bootstrap";
import addIcon from "../../img/addIcon.png";
import bin from "../../img/bin.png";

const PlantCard = (props) => (
    <Card className={styles.container} data-testid="PlantCard">
        <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
        <Card.Body className={styles.plantCard}>
            <Card.Title>{props.plantData ? props.plantData.plantName : 'Your new plant'}</Card.Title>
            <button className="appButton" style={ {width: 'auto', display: 'inline'} }>{props.plantData ? 'EDIT' : 'ADD'}</button>
            {(props.plantData) &&
                <img src={bin} alt="Remove plant" className={styles.removeImg}/>
            }
        </Card.Body>
    </Card>
);

PlantCard.propTypes = {};

PlantCard.defaultProps = {};

export default PlantCard;
