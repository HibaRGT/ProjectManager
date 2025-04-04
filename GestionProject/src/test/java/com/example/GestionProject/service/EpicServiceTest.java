package com.example.GestionProject.service;

import com.example.GestionProject.dto.EpicDTO;
import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.EpicRepository;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.implementation.EpicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private EpicService epicService;

    private Epic epic;
    private EpicDTO epicDTO;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        productBacklog = ProductBacklog.builder().id(1L).build();

        epic = Epic.builder()
                .id(1L)
                .nom("Epic 1")
                .description("Description de l'epic")
                .productBacklog(productBacklog)
                .build();


        epicDTO = EpicDTO.builder()
                .id(1L)
                .nom("Epic Test")
                .description("Description Test")
                .productBacklogId(1L)
                .userStoryIds(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateEpic() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        EpicDTO savedEpic = epicService.createEpic(epicDTO);

        assertNotNull(savedEpic);
        assertEquals(epicDTO.getId(), savedEpic.getId());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void testGetEpicById() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        EpicDTO foundEpic = epicService.getEpicById(1L);

        assertNotNull(foundEpic);
        assertEquals("Epic 1", foundEpic.getNom());
        verify(epicRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEpicsByProductBacklogId() {
        when(epicRepository.findByProductBacklogId(1L)).thenReturn(List.of(epic));

        List<EpicDTO> epics = epicService.getEpicsByProductBacklogId(1L);

        assertFalse(epics.isEmpty());
        assertEquals(1, epics.size());
        assertEquals("Epic 1", epics.get(0).getNom());
    }

    //Exception Tests for getting Epics either by Id or by ProductBacklog Id
    @Test
    void getEpicById_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> epicService.getEpicById(2L));
        assertEquals("Aucune Epic trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void getEpicById_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.getEpicById(null);
        });

        assertEquals("L'ID de l'epic ne peut pas être null", exception.getMessage());
    }

    @Test
    void getEpicsByProductBacklogId_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.getEpicsByProductBacklogId(null);
        });

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateEpic() {
        Epic updatedEpic = Epic.builder()
                .id(1L)
                .nom("Epic Updated")
                .description("New Description")
                .productBacklog(null)
                .userStories(null)
                .build();

        EpicDTO updatedEpicDTO = EpicDTO.builder()
                .id(1L)
                .nom("Epic Updated")
                .description("New Description")
                .productBacklogId(null)
                .userStoryIds(new ArrayList<>())
                .build();

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicRepository.save(any(Epic.class))).thenReturn(updatedEpic);

        EpicDTO result = epicService.updateEpic(1L, updatedEpicDTO);

        assertEquals(updatedEpic.getNom(), result.getNom());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }
    //Exception tests for modify method
    @Test
    void testUpdateEpic_ShouldThrowException_WhenEpicIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.updateEpic(null, epicDTO);
        });

        assertEquals("L'ID de l'epic ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            epicService.updateEpic(2L, epicDTO);
        });

        assertEquals("Aucune Epic trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenNomIsEmpty() {
        EpicDTO invalidEpicDTO = new EpicDTO(1L, "", "Description de l'epic", 1L, null);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.updateEpic(1L, invalidEpicDTO);
        });

        assertEquals("Le titre de l'epic ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenDescriptionIsEmpty() {
        EpicDTO invalidEpicDTO = new EpicDTO(1L, "Nom de l'epic modifié", "", 1L, null);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.updateEpic(1L, invalidEpicDTO);
        });

        assertEquals("La description de l'epic ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testDeleteEpic() {
        when(epicRepository.existsById(1L)).thenReturn(true);
        doNothing().when(epicRepository).deleteById(1L);

        assertDoesNotThrow(() -> epicService.deleteEpic(1L));
        verify(epicRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEpic_ShouldThrowException_WhenEpicIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            epicService.deleteEpic(null);
        });

        assertEquals("L'ID de l'epic ne peut pas être null", exception.getMessage());
    }

    @Test
    void deleteEpic_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> epicService.deleteEpic(2L));
        assertEquals("Epic non trouvée avec l'ID: 2", exception.getMessage());

    }
}
