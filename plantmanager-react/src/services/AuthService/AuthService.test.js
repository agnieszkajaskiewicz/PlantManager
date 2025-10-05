import AuthService from "./AuthService";
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

describe('Auth Service', () => {
    //constants
    const validUsername = "username";
    const validPassword = "123456789";
    const OLD_ENV = process.env;

    beforeEach(() => {
        jest.clearAllMocks();
        process.env = { REACT_APP_SERVER_URL: 'test_url'};
    });

    test('it should call backend on authenticate user operation', () => {
        //given
        const mockResponse = {
            headers: { 'authorization': 'Bearer test-token' },
            data: {},
            status: 200
        };
        axios.post.mockResolvedValueOnce(mockResponse);
        const expectedFormData = new FormData();
        expectedFormData.append('username', validUsername);
        expectedFormData.append('password', validPassword);

        const config = {
            headers: {
                'content-type': 'multipart/form-data'
            },
            withCredentials: true
        }

        //when
        AuthService.authUser(validUsername, validPassword);

        //then
        expect(axios.post).toHaveBeenCalledWith("http://test_url/sign-in/v2", expectedFormData, config);    
    })

    test('it should return success message on authenticate user operation with valid data', async () => {
        //given
        const mockResponse = {
            headers: { 'authorization': 'Bearer test-token' },
            data: { message: 'success' },
            status: 200
        };
        axios.post.mockResolvedValueOnce(mockResponse);

        //when
        const responsePromise = await AuthService.authUser(validUsername, validPassword);

        //then
        expect(responsePromise).toEqual(mockResponse);
        expect(localStorageMock.setItem).toHaveBeenCalledWith('token', 'Bearer test-token');
    })

    afterAll(() => {
        process.env = OLD_ENV;
    });
});
