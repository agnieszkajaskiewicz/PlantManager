import {render, screen, within} from '@testing-library/react';
import '@testing-library/jest-dom';
import PlantCard from './PlantCard';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import userEvent from '@testing-library/user-event';

const mockNavigate = vi.fn();
const mockedPlantService = {
  deletePlantById: vi.fn(() => Promise.resolve({ status: 200 })),
  confirmWatering: vi.fn(() => Promise.resolve({ status: 200 }))
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

  test('it should render edit icon button when plant data is provided', () => {
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
    const editButton = screen.getByTestId('edit-button');
    //then
    expect(editButton).toBeInTheDocument();
    expect(editButton.querySelector('i.bi-pencil')).toBeInTheDocument();
  });

  test('it should render add icon button when no plant data', () => {
    //given

    //when
    render(
      <MemoryRouter>
        <PlantCard setApiError={vi.fn()} />
      </MemoryRouter>
    );
    const addButton = screen.getByTestId('add-button');
    //then
    expect(addButton).toBeInTheDocument();
    expect(addButton.querySelector('i.bi-plus-circle')).toBeInTheDocument();
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

  test('it should navigate to edit page when edit button is clicked', async () => {
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
    
    const editButton = screen.getByTestId('edit-button');
    
    //when
    await user.click(editButton);
    
    //then
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard/edit/123');
  });

  test('it should navigate to add page when add button is clicked', async () => {
    //given
    const user = userEvent.setup();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={null} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const addButton = screen.getByTestId('add-button');
    
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

  test('it should show water button for plant cards with data', () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'Monstera'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    //then
    const waterButton = screen.getByTestId('water-button');
    expect(waterButton).toBeInTheDocument();
    expect(waterButton.querySelector('i.bi-droplet')).toBeInTheDocument();
  });

  test('it should NOT show water button for ADD card', () => {
    //given
    render(
      <MemoryRouter>
        <PlantCard plantData={null} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    //then
    expect(screen.queryByTestId('water-button')).not.toBeInTheDocument();
  });

  test('it should show watering modal when water button is clicked', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Cactus'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    
    //when
    await user.click(waterButton);
    
    //then
    expect(screen.getByText('Confirm watering')).toBeInTheDocument();
    expect(screen.getByText(/Have you watered Cactus today/i)).toBeInTheDocument();
  });

  test('it should hide watering modal when cancel button is clicked', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Fern'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    //when
    const cancelButton = screen.getByRole('button', { name: /cancel/i });
    await user.click(cancelButton);
    
    //then
    expect(screen.queryByText('Confirm watering')).not.toBeInTheDocument();
  });

  test('it should display plant name in watering confirmation modal', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Succulent Plant'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    
    //when
    await user.click(waterButton);
    
    //then
    expect(screen.getByText(/Have you watered Succulent Plant today/i)).toBeInTheDocument();
  });

  test('it should call plantService.confirmWatering with correct plant ID', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 42,
      plantName: 'Orchid'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    const confirmButton = screen.getByTestId('confirm-watering-button');
    
    //when
    await user.click(confirmButton);
    
    //then
    expect(mockedPlantService.confirmWatering).toHaveBeenCalledWith(42);
  });

  test('it should close modal on successful watering confirmation', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Aloe'
    };
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    const confirmButton = screen.getByTestId('confirm-watering-button');
    
    //when
    await user.click(confirmButton);
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(screen.queryByText('Confirm watering')).not.toBeInTheDocument();
  });

  test('it should call onWateringConfirmed callback after successful confirmation', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Snake Plant'
    };
    const onWateringConfirmed = vi.fn();
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={vi.fn()} onWateringConfirmed={onWateringConfirmed} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    const confirmButton = screen.getByTestId('confirm-watering-button');
    
    //when
    await user.click(confirmButton);
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(onWateringConfirmed).toHaveBeenCalled();
  });

  test('it should handle errors from watering confirmation API', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Plant'
    };
    const setApiError = vi.fn();
    const errorMessage = 'Watering confirmation failed';
    
    mockedPlantService.confirmWatering.mockRejectedValueOnce({
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
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    const confirmButton = screen.getByTestId('confirm-watering-button');
    
    //when
    await user.click(confirmButton);
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith(errorMessage);
    expect(screen.queryByText('Confirm watering')).not.toBeInTheDocument();
  });

  test('it should display default error message when backend does not return message on watering error', async () => {
    //given
    const user = userEvent.setup();
    const plantData = {
      id: 1,
      plantName: 'Plant'
    };
    const setApiError = vi.fn();
    
    mockedPlantService.confirmWatering.mockRejectedValueOnce({
      response: {}
    });
    
    render(
      <MemoryRouter>
        <PlantCard plantData={plantData} setApiError={setApiError} />
      </MemoryRouter>
    );
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);
    
    const confirmButton = screen.getByTestId('confirm-watering-button');
    
    //when
    await user.click(confirmButton);
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith('Something went wrong :(');
  });

  test('it should clear error message on successful watering confirmation', async () => {
    //given
    const user = userEvent.setup();
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
    
    const waterButton = screen.getByTestId('water-button');
    await user.click(waterButton);

    const confirmButton = screen.getByTestId('confirm-watering-button');

    //when
    await user.click(confirmButton);
    
    //then
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(setApiError).toHaveBeenCalledWith('');
  });
});