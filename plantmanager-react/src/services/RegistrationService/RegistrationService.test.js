import RegistrationService from "./RegistrationService";
import axios from 'axios';


jest.mock('axios');

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
        jest.resetModules();
        jest.resetAllMocks();
        process.env = {REACT_APP_SERVER_URL: 'test_url'};
    });

    test('it should call backend on create user operation', () => {
        //when
        RegistrationService.createUser(validUsername, validPassword, validPassword, validEmail);
       
        //then
        expect(axios.post).toHaveBeenCalledWith("http://test_url/sign-up/v2", validUserData, { withCredentials: true });    
    })

    test('it should return success message on create user operation with valid data', async () => {
        //given
        axios.post.mockResolvedValueOnce(200);

        //when
        const responsePromise = await RegistrationService.createUser(validUsername, validPassword, validPassword, validEmail);

        //then
        expect(responsePromise).toEqual(200);
    })

    afterAll(() => {
        process.env = OLD_ENV;
    });
});
