import axios from "axios"

const PlantService = {
    fetchPlantsForLoggedUser: () => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        return axios.get("http://" + backendServerURL + "/dashboard/v2", {withCredentials: true});
    },
    deletePlantById: (plantId) => {
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        return axios.delete("http://" + backendServerURL + "/dashboard/deletePlant/v2/" + plantId, {withCredentials: true});
    }
};

export default PlantService;