import React, {useState, useEffect} from 'react';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Form from 'react-bootstrap/Form';
import styles from './PlantEditor.module.css';
import { useRef } from 'react';

import {Dropdown} from "react-bootstrap";
import addIcon from "../../img/addIcon.png";
import {useNavigate, useParams} from "react-router-dom";
import {useDependencies} from '../../DependencyContext';

const PlantEditor = () => {
        const [plantName, setPlantName] = useState('');
        const [roomName, setRoomName] = useState('');
        const [startDate, setStartDate] = useState(new Date());
        const [wateringDays, setWateringDays] = useState(0);
        const [isLoading, setIsLoading] = useState(false);
        const [error, setError] = useState('');

        const navigate = useNavigate();
        const {id} = useParams();
        const {plantService} = useDependencies();
        const isEditMode = !!id;

        const [selectedFile, setSelectedFile] = useState(null);
        const [imagePreview, setImagePreview] = useState(null);
        const fileInputRef = useRef(null);

        useEffect(() => {
            if (isEditMode) {
                const fetchPlantData = async () => {
                    try {
                        setIsLoading(true);
                        const response = await plantService.fetchPlantById(id);
                        const plant = response.data;
                        setPlantName(plant.plantName || '');
                        setRoomName(plant.roomName || '');
                        setWateringDays(plant.wateringDays || 0);
                        if (plant.lastWateringDate) {
                            setStartDate(new Date(plant.lastWateringDate));
                        }
                        setIsLoading(false);
                    } catch (exception) {
                        console.error('Error fetching plant:', exception);
                        setError('Failed to load plant data');
                        setIsLoading(false);
                    }
                };
                fetchPlantData();
            }
        }, [id, isEditMode]);

        const handleSubmit = async () => {
            try {
                setIsLoading(true);
                setError('');

                const lastWateringDate = startDate.toISOString().split('T')[0];
                let savedPlantId;

                if (isEditMode) {
                    await plantService.updatePlant(id, plantName, roomName, lastWateringDate, wateringDays);
                    savedPlantId = id;
                } else {
                    const response = await plantService.createNewPlant(plantName, roomName, lastWateringDate, wateringDays);
                    savedPlantId = response.data.id; // need the new id to upload image
                }

                if (selectedFile) {
                    await uploadImage(savedPlantId, selectedFile);
                }

                setIsLoading(false);
                navigate('/dashboard');
            } catch (exception) {
                console.error('Error saving plant:', exception);
                setError('Failed to save plant');
                setIsLoading(false);
            }
        };

        const handleFileSelect = (e) => {
            const file = e.target.files[0];
            if (!file) return;
            setSelectedFile(file);
            setImagePreview(URL.createObjectURL(file));
        };

        const uploadImage = async (plantId, file) => {
            const formData = new FormData();
            formData.append('image', file);
            await fetch(`/dashboard/v2/${plantId}/image`, {
                method: 'POST',
                body: formData,
                credentials: 'include'
            });
        };

        const generateRange = (size, startAt = 0) => {
            return [...Array(size).keys()].map(i => i + startAt);
        }

        const wateringDaysDropdown = <Dropdown onSelect={(value) => {
            setWateringDays(parseInt(value));
        }} style={{display: "inline", marginLeft: "5px", marginRight: "5px"}}>
            <Dropdown.Toggle id="dropdown-basic" variant="light" className={styles.wateringDaysDropdown}>
                {wateringDays}
            </Dropdown.Toggle>
            <Dropdown.Menu>
                {generateRange(30, 1).map(dayIndex => {
                    return <Dropdown.Item eventKey={dayIndex} key={dayIndex}>{dayIndex}</Dropdown.Item>;
                })}
            </Dropdown.Menu>
        </Dropdown>;

        return (
            <div className="mainContainer" data-testid="PlantEditor">
                <div>
                    <div className={`formContainer ${styles.formContainerTall}`}>
                        {error && <div className="alert alert-danger">{error}</div>}
                        <Form.Group controlId="plantBasics">
                            <Form.Label htmlFor="plantName" className="formLabel">Plant name</Form.Label>
                            <Form.Control 
                                className="formInput" 
                                id="plantName"
                                type="text" 
                                placeholder="Enter plant name"
                                value={plantName}
                                onChange={(e) => setPlantName(e.target.value)}
                                disabled={isLoading}
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label htmlFor="roomName" className="formLabel">Room</Form.Label>
                            <Form.Control 
                                className="formInput" 
                                id="roomName"
                                type="text" 
                                placeholder="Enter plant room"
                                value={roomName}
                                onChange={(e) => setRoomName(e.target.value)}
                                disabled={isLoading}
                            />
                        </Form.Group>
                        
                        <Form.Label className="formLabel" style={{marginTop: '2em'}}>
                            Care schedule
                        </Form.Label>
                        <Form.Group className={styles.wateringScheduleGroup}>
                            <Form.Label style={{marginBottom: '0.5em', marginTop: '0.5em'}}>Water every {wateringDaysDropdown} days
                            </Form.Label>
                        </Form.Group>
                        <Form.Group className={styles.wateringScheduleGroup}>
                            <Form.Label style={{marginBottom: '0.5em', marginRight: '0.5em'}}>Last watered</Form.Label>
                            <DatePicker className={styles.datepicker}
                                        selected={startDate}
                                        onChange={(date) => setStartDate(date)}
                                        disabled={isLoading}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label className="formLabel">Picture</Form.Label>
                            {imagePreview
                                ? <img src={imagePreview} alt="Plant" className={styles.plantImg} />
                                : <img
                                    src={id ? `/dashboard/v2/${id}/image` : addIcon}
                                    alt="Plant"
                                    className={styles.plantImg}
                                    onError={(e) => { e.target.src = addIcon; }}
                                />
                            }
                            <div>
                                <input
                                    type="file"
                                    accept="image/*"
                                    style={{ display: 'none' }}
                                    ref={fileInputRef}
                                    onChange={handleFileSelect}
                                />
                                <button
                                    className="appButton"
                                    disabled={isLoading}
                                    onClick={() => fileInputRef.current.click()}
                                >
                                    UPLOAD IMAGE
                                </button>
                            </div>
                        </Form.Group> 
                        
                        <div className={styles.footer}>
                            <button 
                                className="appButton"
                                onClick={handleSubmit}
                                disabled={isLoading || !plantName || !roomName || wateringDays === 0}
                            >
                                {isLoading ? 'SAVING...' : (isEditMode ? 'UPDATE PLANT' : 'SAVE CHANGES')}
                            </button>
                            <button className="appButton" onClick={() => navigate('/dashboard')} disabled={isLoading}>
                                CANCEL
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
;

PlantEditor.propTypes = {};

PlantEditor.defaultProps = {};

export default PlantEditor;
