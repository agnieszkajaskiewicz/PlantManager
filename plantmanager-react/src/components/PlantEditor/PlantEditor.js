import React, {useState} from 'react';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Form from 'react-bootstrap/Form';
import styles from './PlantEditor.module.css';

import {Button, Dropdown, Modal} from "react-bootstrap";
import addIcon from "../../img/addIcon.png";

const PlantEditor = () => {
        const [startDate, setStartDate] = useState(new Date());
        const [wateringDays, setWateringDays] = useState(0);

        const generateRange = (size, startAt = 0) => {
            return [...Array(size).keys()].map(i => i + startAt);
        }

        const wateringDaysDropdown = <Dropdown onSelect={(value) => {
            setWateringDays(parseInt(value));
        }

        } style={{display: "inline"}}>
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
            <div className={styles.PlantEditor} data-testid="PlantEditor">
                <Form className="formContainer">
                    <Form.Group controlId="plantBasics">
                        <Form.Label>Plant name</Form.Label>
                        <Form.Control className={styles.formControl} type="text" placeholder="Enter plant name"/>
                        <Form.Text>
                            Get your name right, but don't worry, you can always edit it later
                        </Form.Text>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Room</Form.Label>
                        <Form.Control type="text" placeholder="Enter plant room"/>
                        <Form.Text>
                            Enter the room, in which you take care of your lovely plant
                        </Form.Text>
                    </Form.Group>
                    <br/>
                    <Form.Label>
                        Care schedule
                    </Form.Label>
                    <Form.Group>
                        <Form.Label style={{marginBottom: '0'}}>Water every {wateringDaysDropdown} days
                        </Form.Label>
                        <br/>
                        <Form.Text>
                            How often should you water your lovely plant
                        </Form.Text>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Last watered </Form.Label> <div><DatePicker className={styles.datepicker}
                                                                           selected={startDate}
                                                                           onChange={(date) => setStartDate(date)}/>
                    </div>
                        <br/>
                        <Form.Text>
                            When was the last time you watered the plant
                        </Form.Text>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Picture </Form.Label>
                        <br/>
                        <Button variant="light">UPLOAD IMAGE</Button>
                        <img src={addIcon} alt="Add Plant" className={styles.plantImg}/>
                    </Form.Group>
                    <br/>
                    <Modal.Footer>
                        <Button variant="light" onClick={() => {}}>
                            SAVE CHANGES
                        </Button>
                        <Button variant="secondary" onClick={()=>{}}>
                            CANCEL
                        </Button>
                    </Modal.Footer>
                </Form>
            </div>
        );
    }
;

PlantEditor.propTypes = {};

PlantEditor.defaultProps = {};

export default PlantEditor;
