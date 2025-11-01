import { vi } from "vitest";
import PlantService from "./PlantService";
import axios from 'axios';
import * as env from '../../config/env';

vi.mock('axios');
vi.spyOn(env, 'getServerUrl').mockReturnValue('test_url');

describe('Plant Service', () => {
    const OLD_ENV = process.env;

    beforeEach(() => {
        vi.resetModules();
        vi.clearAllMocks();
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

    test('it should call backend to fetch plants to be watered soon', () => {
        //when
        PlantService.fetchPlantsToBeWateredSoon();
        //then
        expect(axios.get).toHaveBeenCalledWith("http://test_url/dashboard/v2/toBeWateredSoon", {withCredentials: true});
    });

    test('it should return plants to be watered soon', async () => {
        //given
        const mockPlantsToBeWatered = [
            { id: 1, plantName: 'Plant 1' },
            { id: 2, plantName: 'Plant 2' }
        ];
        axios.get.mockResolvedValueOnce({ data: mockPlantsToBeWatered });
        //when
        const response = await PlantService.fetchPlantsToBeWateredSoon();
        //then
        expect(response.data).toEqual(mockPlantsToBeWatered);
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
        expect(axios.post).toHaveBeenCalledWith("http://test_url/dashboard/addPlant/v2", {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateredDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    });

    test('it should call backend to fetch single plant by id', () => {
        //given
        const plantId = 456;
        //when
        PlantService.fetchPlantById(plantId);
        //then
        expect(axios.get).toHaveBeenCalledWith("http://test_url/dashboard/v2/456", {withCredentials: true});
    });

    test('it should return plant data when fetching by id', async () => {
        //given
        const mockPlantData = {
            id: 1,
            plantName: 'Monstera',
            roomName: 'Living Room',
            wateringDays: 7,
            lastWateringDate: '2024-01-15'
        };
        axios.get.mockResolvedValueOnce({ data: mockPlantData });
        //when
        const response = await PlantService.fetchPlantById(1);
        //then
        expect(response.data).toEqual(mockPlantData);
    });

    test('it should call backend on plant update with correct payload', () => {
        //given
        const plantId = 789;
        const plantName = 'Updated Plant';
        const roomName = 'Updated Room';
        const lastWateredDate = '2024-01-20';
        const wateringDays = 5;

        //when
        PlantService.updatePlant(plantId, plantName, roomName, lastWateredDate, wateringDays);

        //then
        expect(axios.put).toHaveBeenCalledWith("http://test_url/dashboard/updatePlant/v2/789", {
            plantName: plantName,
            roomName: roomName,
            lastWateringDate: lastWateredDate,
            wateringDays: wateringDays
        }, {withCredentials: true});
    });

    test('it should return updated plant data on successful update', async () => {
        //given
        const mockUpdatedPlant = {
            id: 1,
            plantName: 'Updated Name',
            roomName: 'Updated Room',
            wateringDays: 10,
            lastWateringDate: '2024-01-25'
        };
        axios.put.mockResolvedValueOnce({ data: mockUpdatedPlant });
        
        //when
        const response = await PlantService.updatePlant(1, 'Updated Name', 'Updated Room', '2024-01-25', 10);
        
        //then
        expect(response.data).toEqual(mockUpdatedPlant);
    });

    test('it should handle errors when fetching plant by id fails', async () => {
        //given
        axios.get.mockRejectedValueOnce(new Error('Not found'));
        
        //when & then
        await expect(PlantService.fetchPlantById(999)).rejects.toThrow('Not found');
    });

    test('it should handle errors when updating plant fails', async () => {
        //given
        axios.put.mockRejectedValueOnce(new Error('Update failed'));
        
        //when & then
        await expect(PlantService.updatePlant(1, 'Name', 'Room', '2024-01-20', 5)).rejects.toThrow('Update failed');
    });

    afterAll(() => {
        process.env = OLD_ENV;
    });
});