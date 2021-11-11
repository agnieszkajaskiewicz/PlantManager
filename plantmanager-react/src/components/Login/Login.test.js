import React from 'react';
import {render, screen} from '@testing-library/react';
import renderer from 'react-test-renderer';
import '@testing-library/jest-dom/extend-expect';
import Login from './Login';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedUsedNavigate
}));

describe('<Login />', () => {
    test('it should mount', () => {
        render(<Login/>);

        const login = screen.getByTestId('Login');

        expect(login).toBeInTheDocument();
    });

    test('it should open \'sing up\' panel on \'sing up\' label click', () => {
            //given
            render(<Login/>);
            const signUpLabel = screen.getByText('Sign Up', {selector: 'label'});
            //when
            signUpLabel.click();
            //then
            const signUpButton = screen.getByRole('button');
            expect(signUpButton.textContent).toEqual('Sign Up');
            expect(mockedUsedNavigate).toHaveBeenCalledTimes(1);
            expect(mockedUsedNavigate).toHaveBeenCalledWith('/login/signUp')
        }
    );
});
