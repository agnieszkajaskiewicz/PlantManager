import axios from "axios"

const PlantService = {

    fetchPlantsForLoggedUser: () => { //todo test
        const backendServerURL = process.env.REACT_APP_SERVER_URL;

        return axios.get("http://" + backendServerURL + "/dashboard/v2", {withCredentials: true});
    }
};

export default PlantService;