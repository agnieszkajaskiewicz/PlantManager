import React from 'react';
import {render, screen, within} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import PlantCard from './PlantCard';

const mockedPlantService = {
  deletePlantById: (plantId) => new Promise((resolve => jest.fn()))
};

jest.mock('../../DependencyContext', () => ({
  useDependencies: () => ({
    plantService: mockedPlantService
  })
}));


describe('<PlantCard />', () => {
  test('it should mount', () => {
    render(<PlantCard />);
    
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
    render(<PlantCard plantData={plantData}/>);
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
    render(<PlantCard plantData={plantData}/>);
    const plantCard = screen.getByTestId('PlantCard');
    const button = within(plantCard).getByRole('button');
    //then
    expect(button.innerHTML).toBe('EDIT');
  });

  test('it should render ADD button card when no plant data', () => {
    //given

    //when
    render(<PlantCard />);
    const plantCard = screen.getByTestId('PlantCard');
    const button = within(plantCard).getByRole('button');
    //then
    expect(button.innerHTML).toBe('ADD');
  });

  test('it should delete plant on trash click', () => {
    //given
    const plantData = {
      id: 1,
      plantName: 'plantName'
    }
    render(<PlantCard plantData={plantData} />)
    const removeButton = screen.getByTestId('trashbin');
    jest.spyOn(mockedPlantService, 'deletePlantById');
    //when
    removeButton.click();
    //then
    expect(mockedPlantService.deletePlantById).toHaveBeenCalledWith(plantData.id);
  });
});