import {render, screen, within} from '@testing-library/react';
import '@testing-library/jest-dom';
import PlantCard from './PlantCard';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import userEvent from '@testing-library/user-event';

const mockNavigate = vi.fn();
const mockedPlantService = {
  deletePlantById: vi.fn(() => Promise.resolve({ status: 200 }))
};

vi.mock('../../DependencyContext', () => ({
  useDependencies: () => ({
    plantService: mockedPlantService
  })
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe('<PlantCard />', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  test('it should mount', () => {
    render(
      <MemoryRouter>
        <PlantCard setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const plantCard = screen.getByTestId('PlantCard');
    expect(plantCard).toBeInTheDocument();
  });

  test('it should display correct plant name from payload', () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'thisIsPlantName'
    }
    //when
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    const plantName = screen.getByTestId('PlantName');
    //then
    expect(plantName.innerHTML).toBe(plantData.plantName);
  });

  test('it should render EDIT button card when plant data is provided', () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'plantName'
    }
    //when
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    const plantCard = screen.getByTestId('PlantCard');
    const button = within(plantCard).getByRole('button');
    //then
    expect(button.innerHTML).toBe('EDIT');
  });

  test('it should render ADD button card when no plant data', () => {
    //given

    //when
    render(
      <MemoryRouter>
        <PlantCard setApiError={vi.fn()} />
      </MemoryRouter>
    );
    const plantCard = screen.getByTestId('PlantCard');
    const button = within(plantCard).getByRole('button');
    //then
    expect(button.innerHTML).toBe('ADD');
  });

  test('it should delete plant on trash click', async () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'plantName'
    }
    const setApiError = vi.fn();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    const removeButton = screen.getByTestId('trashbin');
    
    //when
    removeButton.click();
    
    //then
    expect(mockedPlantService.deletePlantById).toHaveBeenCalledWith(plantData.id);
  });

  test('it should navigate to edit page when EDIT button is clicked', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 123,
      plantName: 'Fern'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const editButton = screen.getByRole('button', { name: /edit/i });
    
    //when
    await user.click(editButton);
    
    //then
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard/edit/123');
  });

  test('it should navigate to add page when ADD button is clicked', async () => {
    //given
    const user = userEvent.setup();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={null} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const addButton = screen.getByRole('button', { name: /add/i });
    
    //when
    await user.click(addButton);
    
    //then
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard/add');
  });

  test('it should hide trash bin icon for ADD card', () => {
    //given
    render(
      <MemoryRouter>
        <PlantCard plantData={null} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    //then
    expect(screen.queryByTestId('trashbin')).not.toBeInTheDocument();
  });

  test('it should show trash bin icon for plant card with data', () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Cactus'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    //then
    expect(screen.getByTestId('trashbin')).toBeInTheDocument();
  });

  test('it should hide card after successful deletion', async () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Succulent'
    };
    const setApiError = vi.fn();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    
    const plantCard = screen.getByTestId('PlantCard');
    const removeButton = screen.getByTestId('trashbin');
    
    //when
    removeButton.click();
    
    //then - wait for deletion to complete
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(plantCard).toHaveStyle({ display: 'none' });
  });

  test('it should display error message on delete failure', async () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Plant'
    };
    const setApiError = vi.fn();
    const errorMessage = 'Delete failed';
    
    mockedPlantService.deletePlantById.mockRejectedValueOnce({
      response: {
        data: {
          message: errorMessage
        }
      }
    });
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    
    const removeButton = screen.getByTestId('trashbin');
    
    //when
    removeButton.click();
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith(errorMessage);
  });

  test('it should display default error message when backend does not return message', async () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Plant'
    };
    const setApiError = vi.fn();
    
    mockedPlantService.deletePlantById.mockRejectedValueOnce({
      response: {}
    });
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    
    const removeButton = screen.getByTestId('trashbin');
    
    //when
    removeButton.click();
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith('Something went wrong :(');
  });

  test('it should clear error message on successful deletion', async () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Plant'
    };
    const setApiError = vi.fn();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    
    const removeButton = screen.getByTestId('trashbin');
    
    //when
    removeButton.click();
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith('');
  });
});