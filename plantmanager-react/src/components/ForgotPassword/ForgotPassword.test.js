import React from 'react';
import {render, screen} from '@testing-library/react';
import renderer from 'react-test-renderer';
import '@testing-library/jest-dom/extend-expect';
import ForgotPassword from './ForgotPassword';

describe('<Forgot Password />', () => {
    test('it should mount', () => {
        render(<ForgotPassword/>);

        const forgotPassword = screen.getByTestId('ForgotPassword');

        expect(forgotPassword).toBeInTheDocument();
    });

});