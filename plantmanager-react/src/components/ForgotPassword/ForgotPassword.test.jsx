import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import ForgotPassword from './ForgotPassword';
import { MemoryRouter } from 'react-router-dom';

describe('<ForgotPassword />', () => {
    test('it should mount', () => {
        render(<MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>);

        const forgotPassword = screen.getByTestId('ForgotPassword');

        expect(forgotPassword).toBeInTheDocument();
    });

});