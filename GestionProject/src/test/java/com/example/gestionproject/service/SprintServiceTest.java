package com.example.gestionproject.service;

import com.example.gestionproject.dto.SprintDTO;
import com.example.gestionproject.model.Sprint;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.repository.SprintBacklogRepository;
import com.example.gestionproject.repository.SprintRepository;
import com.example.gestionproject.service.implementation.SprintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @InjectMocks
    private SprintService sprintService;

    private Sprint sprint;
    private SprintDTO sprintDTO;
    private SprintDTO updatedSprintDTO;
    private Sprint updatedSprint;

    @BeforeEach
    void setUp() {
        sprint = Sprint.builder()
                .id(1L)
                .name("Sprint 1")
                .description("Sprint Description")
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 3, 30))
                .build();

        sprintDTO = SprintDTO.builder()
                .id(1L)
                .name("Sprint 1")
                .description("Sprint Description")
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 3, 30))
                .build();

        updatedSprintDTO = SprintDTO.builder()
                .id(1L)
                .name("Sprint Updated")
                .description("Updated description")
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 28))
                .build();

        updatedSprint = Sprint.builder()
                .id(1L)
                .name("Sprint Updated")
                .description("Updated description")
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 28))
                .build();
    }

    @Test
    void testCreateSprint() {
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        SprintDTO result = sprintService.createSprint(sprintDTO);

        assertNotNull(result);
        assertEquals("Sprint 1", result.getName());
        verify(sprintRepository, times(1)).save(any(Sprint.class));
    }

    //Exception testing
    @Test
    void testCreateSprint_ShouldThrowException_WhenNameIsNull() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name(null)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("Le nom du sprint ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testCreateSprint_ShouldThrowException_WhenStartDateIsNull() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name("Sprint 1")
                .startDate(null)
                .endDate(LocalDate.now().plusDays(10))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("La date de début du sprint est obligatoire", exception.getMessage());
    }

    @Test
    void testCreateSprint_ShouldThrowException_WhenEndDateIsNull() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name("Sprint 1")
                .startDate(LocalDate.now())
                .endDate(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("La date de fin du sprint est obligatoire", exception.getMessage());
    }

    @Test
    void testCreateSprint_ShouldThrowException_WhenEndDateBeforeStartDate() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name("Sprint 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(1))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("La date de fin ne peut pas être antérieure à la date de début", exception.getMessage());
    }

    @Test
    void testCreateSprint_ShouldThrowException_WhenDurationLessThan7Days() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name("Sprint 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("La durée du sprint ne peut pas être inférieure à 7 jours", exception.getMessage());
    }

    @Test
    void testCreateSprint_ShouldThrowException_WhenDurationMoreThan30Days() {
        SprintDTO sprintDTO = SprintDTO.builder()
                .name("Sprint 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(31))
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.createSprint(sprintDTO);
        });

        assertEquals("La durée du sprint ne peut pas dépasser 30 jours", exception.getMessage());
    }


    @Test
    void testGetSprintById() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        SprintDTO result = sprintService.getSprintById(1L);

        assertNotNull(result);
        assertEquals("Sprint 1", result.getName());
        verify(sprintRepository, times(1)).findById(1L);

    }

    //Exception Testing
    @Test
    void testGetSprintById_ShouldThrow_WhenIdIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.getSprintById(null);
        });

        assertEquals("L'ID du sprint ne peut pas être null", exception.getMessage());
    }

    @Test
    void testGetSprintById_ShouldThrow_WhenNotFound(){
        when(sprintRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            sprintService.getSprintById(2L);
        });

        assertEquals("Sprint non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testDeleteSprint(){
        when(sprintRepository.existsById(1L)).thenReturn(true);
        doNothing().when(sprintRepository).deleteById(1L);

        assertDoesNotThrow(() -> sprintService.deleteSprint(1L));

        verify(sprintRepository, times(1)).deleteById(1L);
    }

    //Exception testing for deleting project
    @Test
    void testDeleteSprint_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.deleteSprint(null);
        });

        assertEquals("L'ID du sprint ne peut pas être null", exception.getMessage());
    }

    @Test
    void testDeleteSprint_ShouldThrowException_WhenNotFoundNull() {
        when(sprintRepository.existsById(2L)).thenReturn(false);


        Exception exception = assertThrows(RuntimeException.class, () -> {
            sprintService.deleteSprint(2L);
        });

        assertEquals("Sprint non trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateSprint() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        when(sprintRepository.save(any(Sprint.class))).thenReturn(updatedSprint);

        SprintDTO result = sprintService.updateSprint(1L, updatedSprintDTO);

        assertNotNull(result);
        assertEquals("Sprint Updated", result.getName());
        assertEquals("Updated description", result.getDescription());
        verify(sprintRepository, times(1)).save(any(Sprint.class));
    }

    //Exception testing for update method
    @Test
    void testUpdateSprint_ShouldThrow_WhenNotFound() {
        when(sprintRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            sprintService.updateSprint(2L, updatedSprintDTO);
        });

        assertEquals("Sprint non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateSprint_ShouldThrow_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.updateSprint(null, updatedSprintDTO);
        });

        assertEquals("L'ID du sprint ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateSprint_NomNull_ShouldThrow() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        SprintDTO invalidDTO = SprintDTO.builder()
                .name(null)
                .description("Description valide")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> sprintService.updateSprint(1L, invalidDTO));

        assertEquals("Le nom du sprint ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testGetSprintsBySprintBacklogId() {
        Long backlogId = 1L;

        Sprint sprint1 = Sprint.builder()
                .id(1L)
                .name("Sprint 1")
                .description("Description 1")
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 14))
                .build();

        Sprint sprint2 = Sprint.builder()
                .id(2L)
                .name("Sprint 2")
                .description("Description 2")
                .startDate(LocalDate.of(2025, 4, 15))
                .endDate(LocalDate.of(2025, 4, 30))
                .build();

        SprintBacklog sprintBacklog1 = SprintBacklog.builder()
                .id(1L)
                .sprint(sprint1)
                .build();

        SprintBacklog sprintBacklog2 = SprintBacklog.builder()
                .id(2L)
                .sprint(sprint2)
                .build();

        List<SprintBacklog> sprintBacklogs = Arrays.asList(sprintBacklog1, sprintBacklog2);

        when(sprintBacklogRepository.findByProductBacklogId(backlogId)).thenReturn(sprintBacklogs);

        List<SprintDTO> result = sprintService.getSprintsByProductBacklogId(backlogId);

        assertEquals(2, result.size());
        assertEquals("Sprint 1", result.get(0).getName());
        assertEquals("Sprint 2", result.get(1).getName());
        verify(sprintBacklogRepository, times(1)).findByProductBacklogId(backlogId);
    }

}
