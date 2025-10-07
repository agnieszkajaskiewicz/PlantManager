import axios from "axios";
import { getServerUrl } from '../../config/env';

const PlantService = {
    fetchPlantsForLoggedUser: () => {
        const backendServerURL = `${getServerUrl()}`;

        return axios.get("http://" + backendServerURL + "/dashboard/v2", {withCredentials: true});
    },
    deletePlantById: (plantId) => {
        const backendServerURL = `${getServerUrl()}`;


        return axios.delete("http://" + backendServerURL + "/dashboard/deletePlant/v2/" + plantId, {withCredentials: true});
    },
    createNewPlant: (plantName, roomName, lastWateringDate, wateringDays) => { //todo image
        const backendServerURL = `${getServerUrl()}`;

        return axios.post("http://" + backendServerURL + "/dashboard/addPlant/v2/", {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateringDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    }
};

export default PlantService;