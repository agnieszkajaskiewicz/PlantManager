import React from 'react';
import {render, screen} from '@testing-library/react';
import renderer from 'react-test-renderer';
import '@testing-library/jest-dom/extend-expect';
import Login from './Login';

describe('<Login />', () => {
    test('it should mount', () => {
        render(<Login/>);

        const login = screen.getByTestId('Login');

        expect(login).toBeInTheDocument();
    });

    test('it should open \'sing up\' panel on click', () => {
            //given
            render(<Login/>);
            const signUpLabel = screen.getByText('Sign Up');
            //when
            signUpLabel.click();
            //then
            const signUpButton = screen.getByRole('button');
            expect(signUpButton.textContent).toEqual('Sign Up');
        }
    );
});
