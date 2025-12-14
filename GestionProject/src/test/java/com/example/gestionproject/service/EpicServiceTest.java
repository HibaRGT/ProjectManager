package com.example.gestionproject.service;

import com.example.gestionproject.dto.EpicDTO;
import com.example.gestionproject.exception.EpicNotFoundException;
import com.example.gestionproject.model.Epic;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.repository.EpicRepository;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.service.implementation.EpicService;
import com.example.gestionproject.validator.EpicValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    private final EpicValidator epicValidator = new EpicValidator();

    private EpicService epicService;

    private Epic epic;
    private EpicDTO epicDTO;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        epicService = new EpicService(epicRepository, productBacklogRepository, epicValidator);

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

    // -------------------- CREATE --------------------
    @Test
    void testCreateEpic() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        EpicDTO savedEpic = epicService.createEpic(epicDTO);

        assertNotNull(savedEpic);
        assertEquals(epic.getId(), savedEpic.getId());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    // -------------------- READ --------------------
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

    @Test
    void getEpicById_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());

        EpicNotFoundException exception = assertThrows(EpicNotFoundException.class,
                () -> epicService.getEpicById(2L));
        assertEquals("Aucune Epic trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void getEpicById_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> epicService.getEpicById(null));
        assertEquals("Epic ID ne peut pas être null", exception.getMessage());
    }

    @Test
    void getEpicsByProductBacklogId_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> epicService.getEpicsByProductBacklogId(null));
        assertEquals("ProductBacklog ID ne peut pas être null", exception.getMessage());
    }

    // -------------------- UPDATE --------------------
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

    @Test
    void testUpdateEpic_ShouldThrowException_WhenEpicIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> epicService.updateEpic(null, epicDTO));
        assertEquals("Epic ID ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());

        EpicNotFoundException exception = assertThrows(EpicNotFoundException.class,
                () -> epicService.updateEpic(2L, epicDTO));
        assertEquals("Aucune Epic trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenNomIsEmpty() {
        EpicDTO invalidEpicDTO = EpicDTO.builder()
                .id(1L)
                .nom("")
                .description("Description de l'epic")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> epicService.updateEpic(1L, invalidEpicDTO));
    }

    @Test
    void testUpdateEpic_ShouldThrowException_WhenDescriptionIsEmpty() {
        EpicDTO invalidEpicDTO = EpicDTO.builder()
                .id(1L)
                .nom("Nom de l'epic modifié")
                .description("")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> epicService.updateEpic(1L, invalidEpicDTO));
    }

    // -------------------- DELETE --------------------
    @Test
    void testDeleteEpic() {
        when(epicRepository.existsById(1L)).thenReturn(true);
        doNothing().when(epicRepository).deleteById(1L);

        assertDoesNotThrow(() -> epicService.deleteEpic(1L));
        verify(epicRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEpic_ShouldThrowException_WhenEpicIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> epicService.deleteEpic(null));
        assertEquals("Epic ID ne peut pas être null", exception.getMessage());
    }

    @Test
    void deleteEpic_ShouldThrowException_WhenEpicNotFound() {
        when(epicRepository.existsById(2L)).thenReturn(false);

        EpicNotFoundException exception = assertThrows(EpicNotFoundException.class,
                () -> epicService.deleteEpic(2L));
        assertEquals("Epic non trouvée avec l'ID: 2", exception.getMessage());
    }
}
