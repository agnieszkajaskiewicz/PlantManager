import { vi } from "vitest";
import AuthService from "./AuthService";
import axios from 'axios';
import * as env from '../../config/env';

vi.mock('axios');
vi.spyOn(env, 'getServerUrl').mockReturnValue('test_url');

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

describe('Auth Service', () => {
    //constants
    const validUsername = "username";
    const validPassword = "123456789";
    const OLD_ENV = process.env;

    beforeEach(() => {
        vi.clearAllMocks();
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
