import PlantService from "./PlantService";
import axios from 'axios';


jest.mock('axios');

describe('Plant Service', () => {
    const OLD_ENV = process.env;

    beforeEach(() => {
        jest.resetModules();
        jest.resetAllMocks();
        process.env = {REACT_APP_SERVER_URL: 'test_url'};
    });

    test('it should call backend on plant fetch operation', () => {
        //when
        PlantService.fetchPlantsForLoggedUser();
        //then
        expect(axios.get).toHaveBeenCalledWith("http://test_url/dashboard/v2", {withCredentials: true});
    });

    test('it should return correct value on plant fetch operation', async () => {
        //given
        axios.get.mockResolvedValueOnce('test');
        //when
        const responsePromise = await PlantService.fetchPlantsForLoggedUser();
        //then
        expect(responsePromise).toEqual('test');
    });

    test('it should call backend with plant id for plant removal operation', async () => {
        //given
        const plantId = 123;
        //when
        PlantService.deletePlantById(plantId);
        //then
        expect(axios.delete).toHaveBeenCalledWith("http://test_url/dashboard/deletePlant/v2/123", {withCredentials: true});
    });

    test('it should call backend on plant creation with correct payload', () => {
        //given
        const plantName = 'plant';
        const roomName = 'room';
        const lastWateredDate = Date.now();
        const wateringDays = 3;

        //when
        PlantService.createNewPlant(plantName, roomName, lastWateredDate, wateringDays);

        //then
        expect(axios.post).toHaveBeenCalledWith("http://test_url/dashboard/addPlant/v2/", {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateredDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    });

    afterAll(() => {
        process.env = OLD_ENV;
    });
});