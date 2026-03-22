import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import Login from './Login';
import { MemoryRouter } from 'react-router-dom';
import { vi } from 'vitest';

const mockedUsedNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockedUsedNavigate,
    }
});


describe('<Login />', () => {
    test('it should mount', () => {
        render(<MemoryRouter>
                <Login />
            </MemoryRouter>);

        const login = screen.getByTestId('Login');

        expect(login).toBeInTheDocument();
    });

    test('it should open \'sing up\' panel on \'sing up\' label click', () => {
            //given
            render(<MemoryRouter>
                <Login />
            </MemoryRouter>);
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
