import { vi } from "vitest";
import RegistrationService from "./RegistrationService";
import axios from 'axios';
import * as env from '../../config/env';

vi.mock('axios');
vi.spyOn(env, 'getServerUrl').mockReturnValue('http://test_url');

const localStorageMock = (() => {
    let store = {};
    return {
        getItem: vi.fn((key) => store[key] || null),
        setItem: vi.fn((key, value) => { store[key] = value.toString(); }),
        removeItem: vi.fn((key) => { delete store[key]; }),
        clear: vi.fn(() => { store = {}; })
    };
})();

Object.defineProperty(window, 'localStorage', {
    value: localStorageMock
});

describe('Registration Service', () => {
    //constants
    const validUsername = "username";
    const validPassword = "123456789";
    const validEmail = "valid@email.com";
    const OLD_ENV = process.env;

    const validUserData = {
        username: validUsername,
        password: validPassword, 
        repeatPassword: validPassword, 
        email: validEmail
    }

    beforeEach(() => {
        vi.clearAllMocks();
    });

    test('it should call backend on create user operation', () => {
        //given
        const mockResponse = {
            headers: { 
                'authorization': 'Bearer test-token',
                'username': validUsername 
            },
            data: {},
            status: 200
        };
        axios.post.mockResolvedValueOnce(mockResponse);

        //when
        RegistrationService.createUser(validUsername, validPassword, validPassword, validEmail);
       
        //then
        expect(axios.post).toHaveBeenCalledWith("http://test_url/sign-up/v2", validUserData, { withCredentials: true });    
    })

    test('it should return success message on create user operation with valid data', async () => {
        //given
        const mockResponse = {
            headers: { 
                'authorization': 'Bearer test-token',
                'username': validUsername 
            },
            data: {},
            status: 200
        };
        axios.post.mockResolvedValueOnce(mockResponse);

        //when
        const responsePromise = await RegistrationService.createUser(validUsername, validPassword, validPassword, validEmail);

        //then
        expect(responsePromise).toEqual(mockResponse);
        expect(localStorageMock.setItem).toHaveBeenCalledWith('token', 'Bearer test-token');
    })

    afterAll(() => {
        process.env = OLD_ENV;
    });
});
