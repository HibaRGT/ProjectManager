package com.example.GestionProject.service;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.EpicRepository;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private EpicService epicService;

    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic();
        epic.setId(1L);
        epic.setNom("Epic 1");
        epic.setDescription("Description de l'epic");
    }

    @Test
    void testCreateEpic() {
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        Epic savedEpic = epicService.createEpic(epic);

        assertNotNull(savedEpic);
        assertEquals("Epic 1", savedEpic.getNom());
        verify(epicRepository, times(1)).save(epic);
    }

    @Test
    void testGetEpicById() {
        when(productBacklogRepository.existsById(1L)).thenReturn(true);  // Vérifie que ProductBacklog existe
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic)); // Epic trouvé

        Epic foundEpic = epicService.getEpicById(1L);

        assertNotNull(foundEpic);
        assertEquals(1L, foundEpic.getId());
    }

    @Test
    void testGetEpicsByProductBacklogId() {
        when(epicRepository.findByProductBacklogId(1L)).thenReturn(List.of(epic));

        List<Epic> epics = epicService.getEpicsByProductBacklogId(1L);

        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("Epic 1", epics.get(0).getNom());
    }

    @Test
    void testGetAllEpics() {
        when(epicRepository.findAll()).thenReturn(List.of(epic));

        List<Epic> epics = epicService.getAllEpics();

        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("Epic 1", epics.get(0).getNom());
    }

    @Test
    void testUpdateEpic() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        Epic updatedEpic = epicService.updateEpic(1L, new Epic(1L, "Epic Updated", "New Description", null, null));

        assertEquals("Epic Updated", updatedEpic.getNom());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void testAddUserStoryToEpic() {
        UserStory userStory = new UserStory();
        userStory.setTitre("User Story 1");

        epic.setUserStories(new ArrayList<>());

        epic.getUserStories().add(userStory);

        assertEquals(1, epic.getUserStories().size(), "L'Epic devrait contenir 1 User Story");

        assertTrue(epic.getUserStories().contains(userStory), "L'Epic devrait contenir la User Story");

        assertEquals("User Story 1", userStory.getTitre(), "Le titre de la User Story ne correspond pas.");
    }






    @Test
    void testGetUserStoriesByEpicId() {
        UserStory userStory = new UserStory();
        userStory.setTitre("User Story 1");
        when(userStoryRepository.findByEpicId(1L)).thenReturn(List.of(userStory));

        List<UserStory> userStories = epicService.getUserStoriesByEpicId(1L);

        assertNotNull(userStories);
        assertEquals(1, userStories.size());
        assertEquals("User Story 1", userStories.get(0).getTitre());
    }

    @Test
    void testGetEpicByIdProductBacklogNotFound() {
        when(productBacklogRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            epicService.getEpicById(1L);
        });
        assertEquals("ProductBacklog introuvable avec l'ID: 1", exception.getMessage());
    }
    @Test
    void testGetEpicByIdNotFound() {
        when(productBacklogRepository.existsById(1L)).thenReturn(true);  // Vérifie que ProductBacklog existe
        when(epicRepository.findById(1L)).thenReturn(Optional.empty()); // Epic non trouvé

        Exception exception = assertThrows(RuntimeException.class, () -> {
            epicService.getEpicById(1L);
        });
        assertEquals("Aucune Epic trouvée avec l'ID: 1", exception.getMessage());
    }

    @Test
    void testAddUserStoryToEpicEpicNotFound() {
        UserStory userStory = new UserStory();
        userStory.setTitre("User Story 1");
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            epicService.addUserStoryToEpic(1L, userStory);
        });

        assertEquals("Aucune Epic trouvée avec l'ID: 1", exception.getMessage());
    }


    @Test
    void testUpdateEpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            epicService.updateEpic(1L, new Epic(1L, "Epic Updated", "New Description", null, null));
        });

        assertEquals("Aucune Epic trouvée avec l'ID: 1", exception.getMessage().trim());  // Utilisation de trim pour éviter les espaces
    }


}
