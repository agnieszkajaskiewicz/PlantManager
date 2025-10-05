import RegistrationService from "./RegistrationService";
import axios from 'axios';

jest.mock('axios');

const localStorageMock = (() => {
    let store = {};
    return {
        getItem: jest.fn((key) => store[key] || null),
        setItem: jest.fn((key, value) => { store[key] = value.toString(); }),
        removeItem: jest.fn((key) => { delete store[key]; }),
        clear: jest.fn(() => { store = {}; })
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
        jest.clearAllMocks();
        process.env = {REACT_APP_SERVER_URL: 'test_url'};
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
