import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import Homepage from './Homepage';
import { MemoryRouter } from 'react-router-dom';

describe('<Homepage />', () => {
    test('it should mount', () => {
        render(<MemoryRouter>
            <Homepage />
        </MemoryRouter>);

        const homepage = screen.getByTestId('Homepage');

        expect(homepage).toBeInTheDocument();
    });

});
