import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import AppHeader from './AppHeader';

const mockedUsedNavigate = vi.fn();
const mockedUsedLocation = vi.fn();

vi.mock('react-router-dom', () => ({
    ...vi.importActual('react-router-dom'),
    useNavigate: () => mockedUsedNavigate,
    useLocation: () => mockedUsedLocation
}));

describe('<AppHeader />', () => {
    test('it should mount', () => {
        render(<AppHeader/>);

        const appHeader = screen.getByTestId('AppHeader');

        expect(appHeader).toBeInTheDocument();
    });

});