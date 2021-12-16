import ValidationService from "./ValidationService";

describe('Validation Service', () => {
    //constants
    const validUsername = "username";
    const validPassword = "123456789";
    const validEmail = "valid@email.com";

    test('it should validate empty username', () => {
        //when
        const errorMessage = ValidationService.validateUsername("");
        //then
        expect(errorMessage).toBeTruthy();
        expect(errorMessage.field).toEqual('username');
        expect(errorMessage.message).toEqual('This field is required.');
    })

    test('it should validate too short username', () => {
        //when
        const errorMessage = ValidationService.validateUsername("user");
        //then
        expect(errorMessage).toBeTruthy();
        expect(errorMessage.field).toEqual('username');
        expect(errorMessage.message).toEqual('Please use between 6 and 32 characters.');
    })

    test('it should validate too long username', () => {
        //when
        const errorMessage = ValidationService.validateUsername("usernameusernameusernameusernameusernameusernameusername");
        //then
        expect(errorMessage).toBeTruthy();
        expect(errorMessage.field).toEqual('username');
        expect(errorMessage.message).toEqual('Please use between 6 and 32 characters.');
    })

    test('it should return no errors for valid username', () => {
        //when
        const errorMessage = ValidationService.validateUsername(validUsername);
        //then
        expect(errorMessage).toBeUndefined();
    })
})