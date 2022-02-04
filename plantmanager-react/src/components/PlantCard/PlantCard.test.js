import React from 'react';
import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import PlantCard from './PlantCard';

const mockedPlantService = {
  deletePlantById: (plantId) => jest.fn()
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

  test('it should display correct values from payload', () => {

  });

  test('it should render add card when no data', () => {

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