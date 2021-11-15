import React from 'react';
import {render, screen} from '@testing-library/react';
import renderer from 'react-test-renderer';
import '@testing-library/jest-dom/extend-expect';
import Homepage from './Homepage';

const mockedUsedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedUsedNavigate
}));

describe('<Homepage />', () => {
    test('it should mount', () => {
        render(<Homepage/>);

        const homepage = screen.getByTestId('Homepage');

        expect(homepage).toBeInTheDocument();
    });

});
