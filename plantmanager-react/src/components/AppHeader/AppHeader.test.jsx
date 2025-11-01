import {render, screen, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom';
import AppHeader from './AppHeader';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';

const mockedUsedNavigate = vi.fn();
const mockedUsedLocation = vi.fn();
const mockAuthService = {
    logoutUser: vi.fn()
};

vi.mock('react-router-dom', () => ({
    ...vi.importActual('react-router-dom'),
    useNavigate: () => mockedUsedNavigate,
    useLocation: () => mockedUsedLocation
}));

vi.mock('../../DependencyContext', () => ({
    useDependencies: () => ({
        authService: mockAuthService
    })
}));

describe('<AppHeader />', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        localStorage.clear();
        mockedUsedLocation.mockReturnValue({ pathname: '/' });
    });

    test('it should mount', () => {
        render(<AppHeader/>);

        const appHeader = screen.getByTestId('AppHeader');

        expect(appHeader).toBeInTheDocument();
    });

    test('it should navigate to homepage when logo clicked and user not logged in', async () => {
        const user = userEvent.setup();
        localStorage.removeItem('username');
        
        render(<AppHeader/>);
        
        const logo = screen.getByAltText('logo');
        await user.click(logo);
        
        expect(mockedUsedNavigate).toHaveBeenCalledWith('/');
    });

    test('it should navigate to dashboard when logo clicked and user is logged in', async () => {
        const user = userEvent.setup();
        localStorage.setItem('username', 'testuser');
        
        render(<AppHeader/>);
        
        const logo = screen.getByAltText('logo');
        await user.click(logo);
        
        expect(mockedUsedNavigate).toHaveBeenCalledWith('/dashboard');
    });

    test('it should display username when on dashboard page', () => {
        localStorage.setItem('username', 'john.doe');
        mockedUsedLocation.mockReturnValue({ pathname: '/dashboard' });
        
        render(<AppHeader/>);
        
        expect(screen.getByText(/Username: john.doe/i)).toBeInTheDocument();
    });

    test('it should display logout link when on dashboard page', () => {
        localStorage.setItem('username', 'testuser');
        mockedUsedLocation.mockReturnValue({ pathname: '/dashboard' });
        
        render(<AppHeader/>);
        
        expect(screen.getByText(/logout/i)).toBeInTheDocument();
    });

    test('it should call logout service when logout clicked', async () => {
        const user = userEvent.setup();
        localStorage.setItem('username', 'testuser');
        mockedUsedLocation.mockReturnValue({ pathname: '/dashboard' });
        mockAuthService.logoutUser.mockResolvedValue({ status: 200 });
        
        render(<AppHeader/>);
        
        const logoutLink = screen.getByText(/logout/i);
        await user.click(logoutLink);
        
        await waitFor(() => {
            expect(mockAuthService.logoutUser).toHaveBeenCalled();
        });
    });

    test('it should navigate to login after successful logout', async () => {
        const user = userEvent.setup();
        localStorage.setItem('username', 'testuser');
        mockedUsedLocation.mockReturnValue({ pathname: '/dashboard' });
        mockAuthService.logoutUser.mockResolvedValue({ status: 200 });
        
        render(<AppHeader/>);
        
        const logoutLink = screen.getByText(/logout/i);
        await user.click(logoutLink);
        
        await waitFor(() => {
            expect(mockedUsedNavigate).toHaveBeenCalledWith('/login/signIn?logout');
        });
    });

    test('it should show centered logo on non-dashboard pages', () => {
        mockedUsedLocation.mockReturnValue({ pathname: '/login/signIn' });
        
        render(<AppHeader/>);
        
        const logo = screen.getByAltText('logo');
        expect(logo).toBeInTheDocument();
    });
});
