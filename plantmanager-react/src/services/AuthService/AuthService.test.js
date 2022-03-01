import AuthService from "./AuthService";
import axios from 'axios';


jest.mock('axios');

describe('Auth Service', () => {
    //constants
    const validUsername = "username";
    const validPassword = "123456789";
    const OLD_ENV = process.env;

    beforeEach(() => {
        jest.resetModules();
        jest.resetAllMocks();
        process.env = {REACT_APP_SERVER_URL: 'test_url'};
    });

    test('it should call backend on authenticate user operation', () => {
        //Given
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
        expect(axios.post).toHaveBeenCalledWith("http://test_url/sign-in", expectedFormData, config);    
    })

    test('it should return success message on authenticate user operation with valid data', async () => {
        //given
        axios.post.mockResolvedValueOnce(200);

        //when
        const responsePromise = await AuthService.authUser(validUsername, validPassword);

        //then
        expect(responsePromise).toEqual(200);
    })

    afterAll(() => {
        process.env = OLD_ENV;
    });
});
