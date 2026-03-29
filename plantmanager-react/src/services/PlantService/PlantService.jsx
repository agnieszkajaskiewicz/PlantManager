import axios from "axios";
import { getServerUrl } from '../../config/env';

const PlantService = {
    fetchPlantsForLoggedUser: () => {
        const backendServerURL = `${getServerUrl()}`;

        return axios.get(backendServerURL + "/dashboard/v2", {withCredentials: true});
    },
    fetchPlantsToBeWateredSoon: () => {
        const backendServerURL = `${getServerUrl()}`;

        return axios.get(backendServerURL + "/dashboard/v2/toBeWateredSoon", {withCredentials: true});
    },
    deletePlantById: (plantId) => {
        const backendServerURL = `${getServerUrl()}`;


        return axios.delete(backendServerURL + "/dashboard/deletePlant/v2/" + plantId, {withCredentials: true});
    },
    createNewPlant: (plantName, roomName, lastWateringDate, wateringDays) => { //todo image
        const backendServerURL = `${getServerUrl()}`;

        return axios.post(backendServerURL + "/dashboard/addPlant/v2", {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateringDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    },
    fetchPlantById: (plantId) => {
        const backendServerURL = `${getServerUrl()}`;

        return axios.get(backendServerURL + "/dashboard/v2/" + plantId, {withCredentials: true});
    },
    updatePlant: (plantId, plantName, roomName, lastWateringDate, wateringDays) => { //todo image
        const backendServerURL = `${getServerUrl()}`;

        return axios.put(backendServerURL + "/dashboard/updatePlant/v2/" + plantId, {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateringDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    },
    confirmWatering: (plantId) => {
        const backendServerURL = `${getServerUrl()}`;

        return axios.post(backendServerURL + "/toBeWateredSoon/confirmWatering/v2/" + plantId, {}, {withCredentials: true});
    }
};

export default PlantService;