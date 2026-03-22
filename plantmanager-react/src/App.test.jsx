import {render, screen} from '@testing-library/react';
import App from './App';

test('dummy test', () => { //todo replace with something relevant to the project
    render(<App/>);
    const app = screen.getByTestId('App');

    expect(app).toBeInTheDocument();
});
