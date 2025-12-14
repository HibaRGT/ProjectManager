package com.example.gestionproject.service;

import com.example.gestionproject.dto.SprintBacklogDTO;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.Sprint;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.repository.SprintBacklogRepository;
import com.example.gestionproject.repository.SprintRepository;
import com.example.gestionproject.service.implementation.SprintBacklogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogTest {
    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private SprintBacklogService sprintBacklogService;

    private SprintBacklog sprintBacklog;
    private SprintBacklogDTO sprintBacklogDTO;
    private Sprint sprint;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        sprint = Sprint.builder()
                .id(1L)
                .name("Test Sprint")
                .build();

        productBacklog = ProductBacklog.builder()
                .id(1L)
                .nom("Test Product Backlog")
                .build();

        sprintBacklog = SprintBacklog.builder()
                .id(1L)
                .sprint(sprint)
                .productBacklog(productBacklog)
                .userStories(new ArrayList<>())
                .build();

        sprintBacklogDTO = SprintBacklogDTO.builder()
                .id(1L)
                .sprintId(1L)
                .productBacklogId(1L)
                .userStoryIds(new ArrayList<>())
                .build();
    }

    @Test
    void createSprintBacklog() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklogDTO result = sprintBacklogService.createSprintBacklog(sprintBacklogDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sprintBacklogRepository, times(1)).save(any());
    }

    @Test
    void createSprintBacklog_ShouldThrow_WhenSprintNotFound() {
        sprintBacklogDTO.setSprintId(2L);
        when(sprintRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.createSprintBacklog(sprintBacklogDTO)
        );

        assertEquals("Sprint introuvable", exception.getMessage());
    }

    @Test
    void createSprintBacklog_ShouldThrow_WhenProductBacklogNotFound() {
        sprintBacklogDTO.setProductBacklogId(2L);
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(productBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.createSprintBacklog(sprintBacklogDTO)
        );

        assertEquals("ProductBacklog introuvable", exception.getMessage());
    }

    @Test
    void createSprintBacklog_ShouldThrow_WhenMissingSprintId() {
        sprintBacklogDTO.setSprintId(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> sprintBacklogService.createSprintBacklog(sprintBacklogDTO)
        );

        assertEquals("Le sprintBacklog n'est lié à aucun sprint", exception.getMessage());
    }

    @Test
    void createSprintBacklog_ShouldThrow_WhenMissingProductBacklogId() {
        sprintBacklogDTO.setProductBacklogId(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> sprintBacklogService.createSprintBacklog(sprintBacklogDTO)
        );

        assertEquals("Le sprintBacklog n'est lié à aucun ProductBacklog", exception.getMessage());
    }

    @Test
    void getSprintBacklogById() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));

        SprintBacklogDTO result = sprintBacklogService.getSprintBacklogById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sprintBacklogRepository, times(1)).findById(1L);
    }

    @Test
    void getSprintBacklogById_ShouldThrow_WhenIdIsNull() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.getSprintBacklogById(null)
        );

        assertEquals("L'ID du sprint backlog ne peut pas etre null", exception.getMessage());
    }

    @Test
    void getSprintBacklogById_ShouldThrow_WhenNotFound() {
        when(sprintBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.getSprintBacklogById(2L)
        );

        assertEquals("SprintBacklog introuvable", exception.getMessage());
    }

    @Test
    void getSprintBacklogBySprintId() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        sprint.setSprintBacklog(sprintBacklog);

        SprintBacklogDTO result = sprintBacklogService.getSprintBacklogBySprintId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sprintRepository, times(1)).findById(1L);
    }

    @Test
    void getSprintBacklogBySprintId_ShouldThrow_WhenIdIsNull() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.getSprintBacklogBySprintId(null)
        );

        assertEquals("L'ID du sprint ne peut pas etre null", exception.getMessage());
    }

    @Test
    void getSprintBacklogBySprintId_ShouldThrow_WhenSprintNotFound() {
        when(sprintRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.getSprintBacklogBySprintId(2L)
        );

        assertEquals("Sprint introuvable", exception.getMessage());
    }

    @Test
    void deleteSprintBacklogById_ShouldSucceed_WhenExists() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));

        assertDoesNotThrow(() -> sprintBacklogService.deleteSprintBacklogById(1L));
        verify(sprintBacklogRepository, times(1)).delete(sprintBacklog);
    }

    @Test
    void deleteSprintBacklogById_ShouldThrow_WhenNotFound() {
        when(sprintBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.deleteSprintBacklogById(2L)
        );

        assertEquals("SprintBacklog introuvable", exception.getMessage());
    }

    @Test
    void deleteSprintBacklogById_ShouldThrow_WhenIdIsNull() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> sprintBacklogService.deleteSprintBacklogById(null)
        );

        assertEquals("L'ID du sprint backlog ne peut pas etre null", exception.getMessage());
    }
}
