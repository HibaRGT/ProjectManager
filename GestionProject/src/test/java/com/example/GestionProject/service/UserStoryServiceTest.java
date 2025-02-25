package com.example.GestionProject.service;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private UserStoryService userStoryService;

    private UserStory userStory;
    private ProductBacklog backlog;

    @BeforeEach
    void setUp() {
        backlog = new ProductBacklog(1L, "Backlog", "Description du backlog", null, null);
        userStory = new UserStory(1L, "US 1", "Description", 1, StatutEnum.TO_DO, null, backlog);
    }

    @Test
    void testCreateUserStoryInBacklog() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStory createdUserStory = userStoryService.createUserStoryInBacklog(userStory, 1L);

        assertNotNull(createdUserStory);
        assertEquals("US 1", createdUserStory.getTitre());
        verify(userStoryRepository, times(1)).save(userStory);
    }

    @Test
    void testDeleteUserStoryById() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userStoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> userStoryService.deleteUserStoryById(1L));
        verify(userStoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetUserStoryById() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        UserStory foundUserStory = userStoryService.getUserStoryById(1L);

        assertNotNull(foundUserStory);
        assertEquals("US 1", foundUserStory.getTitre());
    }

    @Test
    void testDeleteUserStoryEntity() {
        doNothing().when(userStoryRepository).delete(userStory);

        userStoryService.deleteUserStoryEntity(userStory);

        verify(userStoryRepository, times(1)).delete(userStory);
    }

    @Test
    void testGetUserStoriesByBacklogId() {
        List<UserStory> userStories = Collections.singletonList(userStory);
        when(userStoryRepository.findByProductBacklogId(1L)).thenReturn(userStories);

        List<UserStory> result = userStoryService.getUserStoriesByBacklogId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("US 1", result.get(0).getTitre());
    }

    @Test
    void testUpdateUserStory() {
        UserStory updatedDetails = new UserStory();
        updatedDetails.setTitre("US 1 Updated");
        updatedDetails.setDescription("Updated Description");
        updatedDetails.setPriorite(2);
        updatedDetails.setStatut(StatutEnum.IN_PROGRESS);

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(updatedDetails);

        UserStory updatedUserStory = userStoryService.updateUserStory(1L, updatedDetails);

        assertNotNull(updatedUserStory);
        assertEquals("US 1 Updated", updatedUserStory.getTitre());
        assertEquals(2, updatedUserStory.getPriorite());
        assertEquals(StatutEnum.IN_PROGRESS, updatedUserStory.getStatut());
    }

    @Test
    void testUpdateUserStoryStatus() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        userStory.setStatut(StatutEnum.DONE);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStory updatedUserStory = userStoryService.updateUserStoryStatus(1L, StatutEnum.DONE);

        assertEquals(StatutEnum.DONE, updatedUserStory.getStatut());
    }

    @Test
    void testUpdateUserStoryPriority() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        userStory.setPriorite(5);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStory updatedUserStory = userStoryService.updateUserStoryPriority(1L, 5);

        assertEquals(5, updatedUserStory.getPriorite());
    }
}
