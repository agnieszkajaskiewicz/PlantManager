package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WateringScheduleServiceImplTest {

    private static final int WATERING_SCHEDULE_ID = 1;
    private WateringScheduleService wateringScheduleService;
    private WateringScheduleRepository wateringScheduleRepository;

    @BeforeEach
    void setUp() {
        wateringScheduleRepository = mock(WateringScheduleRepository.class);

        wateringScheduleService = new WateringScheduleServiceImpl(wateringScheduleRepository);
    }

    @Test
    void shouldFindAllWateringSchedules() {
        //given
        var allSchedules = prepareWateringSchedules();
        when(wateringScheduleRepository.findAll()).thenReturn(allSchedules);

        //when
        var foundSchedules = wateringScheduleService.findAll();

        //then
        assertEquals(allSchedules, foundSchedules);
        verify(wateringScheduleRepository, times(1)).findAll();
    }

    @Test
    void shouldFindWateringScheduleById() throws NotFoundException {
        //given
        var wateringSchedule = new WateringSchedule(WATERING_SCHEDULE_ID, 7, "01-01-2025");
        when(wateringScheduleRepository.findById(WATERING_SCHEDULE_ID)).thenReturn(Optional.of(wateringSchedule));

        //when
        var foundSchedule = wateringScheduleService.find(WATERING_SCHEDULE_ID);

        //then
        assertEquals(wateringSchedule, foundSchedule);
        verify(wateringScheduleRepository, times(1)).findById(WATERING_SCHEDULE_ID);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenWateringScheduleNotFound() {
        //given
        when(wateringScheduleRepository.findById(WATERING_SCHEDULE_ID)).thenReturn(Optional.empty());

        //when && then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> wateringScheduleService.find(WATERING_SCHEDULE_ID));

        assertEquals("Watering schedule with ID " + WATERING_SCHEDULE_ID + " not found", exception.getMessage());
        verify(wateringScheduleRepository, times(1)).findById(WATERING_SCHEDULE_ID);
    }

    @Test
    void shouldSaveWateringSchedule() {
        //given
        var wateringSchedule = new WateringSchedule(5, "13-01-2025");
        when(wateringScheduleRepository.save(wateringSchedule)).thenReturn(wateringSchedule);

        //when
        var savedSchedule = wateringScheduleService.save(wateringSchedule);

        //then
        assertEquals(wateringSchedule, savedSchedule);
        verify(wateringScheduleRepository, times(1)).save(wateringSchedule);
    }

    private List<WateringSchedule> prepareWateringSchedules() {
        var schedule1 = new WateringSchedule(3, "01-01-2025");
        var schedule2 = new WateringSchedule(10, "15-02-2025");
        var schedule3 = new WateringSchedule(20, "31-12-2024");
        return List.of(schedule1, schedule2, schedule3);
    }
}
