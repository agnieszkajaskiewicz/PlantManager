import React from 'react';
import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import AppHeader from './AppHeader';

const mockedUsedNavigate = jest.fn();
const mockedUsedLocation = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
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