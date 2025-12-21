import {useState} from 'react';
import styles from './PlantCard.module.css';
import {Card, Modal, Button} from "react-bootstrap";
import addIcon from "../../img/addIcon.png";
import bin from "../../img/bin.png";
import {useDependencies} from '../../DependencyContext';
import {useNavigate} from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
//todo responsywny rozmiar czcionki
//todo responsywna szerokość, wysokość przy nadmiernym skurczeniu

const PlantCard = (props) => {
    const [isDeleted, setIsDeleted] = useState(false);
    const [showWateringModal, setShowWateringModal] = useState(false);
    const {plantService} = useDependencies();
    const navigate = useNavigate();

    const deletePlant = (plantId) => {
        plantService.deletePlantById(plantId)
            .then(response => {
                if (response.status === 200) {
                    props.setApiError('');
                }
                setIsDeleted(true);
            })
            .catch(error => {
                console.error('Error deleting plant:', error);
                const message = error.response?.data?.message || 'Something went wrong :(';
                props.setApiError(message);
            });
    }

    const handleEditOrAdd = () => {
        if (props.plantData) {
            navigate(`/dashboard/edit/${props.plantData.id}`);
        } else {
            navigate('/dashboard/add');
        }
    }

    const handleWateringClick = () => {
        setShowWateringModal(true);
    }

    const handleCloseModal = () => {
        setShowWateringModal(false);
    }

    const handleConfirmWatering = () => {
        plantService.confirmWatering(props.plantData.id)
            .then(response => {
                if (response.status === 200) {
                    props.setApiError('');
                    setShowWateringModal(false);
                    // Notify parent to refresh the list
                    if (props.onWateringConfirmed) {
                        props.onWateringConfirmed();
                    }
                }
            })
            .catch(error => {
                console.error('Error confirming watering:', error);
                const message = error.response?.data?.message || 'Something went wrong :(';
                props.setApiError(message);
                setShowWateringModal(false);
            });
    }

    const containerStyle = {
        boxShadow: '2px 2px 6px 0 rgba(0,0,0,0.3)',
        backgroundColor: 'transparent'
    }
    return (
        <>
            <Card data-testid="PlantCard" style={isDeleted ? {display: 'none'} : containerStyle}>
                <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
                <Card.Body className={styles.plantCard}>
                    <Card.Title data-testid="PlantName">{props.plantData ? props.plantData.plantName : 'Your new plant'}</Card.Title>
                    {props.plantData ? (
                        <div className={styles.buttonContainer}>
                            <button 
                                className={styles.iconButton}
                                onClick={handleEditOrAdd}
                                title="Edit plant"
                                data-testid="edit-button">
                                <i className="bi bi-pencil"></i>
                            </button>
                            <button 
                                className={styles.iconButton}
                                onClick={handleWateringClick}
                                title="Confirm watering"
                                data-testid="water-button">
                                <i className="bi bi-droplet"></i>
                            </button>
                            <button 
                                className={styles.iconButton}
                                onClick={() => deletePlant(props.plantData.id)}
                                title="Delete plant"
                                data-testid="trashbin">
                                <i className="bi bi-trash3"></i>
                            </button>
                        </div>
                    ) : (
                        <div className={styles.buttonContainer}>
                            <button 
                                className={styles.iconButton}
                                onClick={handleEditOrAdd}
                                title="Add new plant"
                                data-testid="add-button">
                                <i className="bi bi-plus-circle"></i>
                            </button>
                        </div>
                    )}
                </Card.Body>
            </Card>

            {/* Watering Confirmation Modal */}
            {props.plantData && (
                <Modal show={showWateringModal} onHide={handleCloseModal} centered className={styles.wateringModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>Confirm watering</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        Have you watered {props.plantData.plantName} today?
                    </Modal.Body>
                    <Modal.Footer>
                        <Button data-testid="cancel-button" className={styles.cancelButton} onClick={handleCloseModal}>
                            Cancel
                        </Button>
                        <Button data-testid="confirm-watering-button" className={styles.confirmButton} onClick={handleConfirmWatering}>
                            Confirm
                        </Button>
                    </Modal.Footer>
                </Modal>
            )}
        </>
    )
};

PlantCard.propTypes = {};

PlantCard.defaultProps = {};

export default PlantCard;
