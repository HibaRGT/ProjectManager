package com.example.GestionProject.service;

import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.*;
import com.example.GestionProject.repository.*;
import com.example.GestionProject.service.implementation.UserStoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private EpicRepository epicRepository;

    @InjectMocks
    private UserStoryService userStoryService;

    private UserStoryDTO userStoryDTO;
    private UserStory userStory;
    private ProductBacklog backlog;
    private Epic epic;
    private SprintBacklog sprintBacklog;
    private Task task;

    @BeforeEach
    void setUp() {
        backlog = ProductBacklog.builder().id(1L).build();

        epic = Epic.builder().id(1L).build();

        sprintBacklog = SprintBacklog.builder()
                .id(1L)
                .userStories(new ArrayList<>())
                .build();

        task = Task.builder().id(1L).build();

        userStory = UserStory.builder()
                .id(1L)
                .titre("UserStory 1")
                .description("UserStory Description 1")
                .priorite(1)
                .statut(StatutEnum.TO_DO)
                .productBacklog(backlog)
                .epic(epic)
                .tasks(List.of(task))
                .build();

        userStoryDTO = UserStoryDTO.builder()
                .id(1L)
                .titre("UserStory 1")
                .description("UserStory Description 1")
                .priorite(1)
                .statut(StatutEnum.TO_DO)
                .epicId(1L)
                .productBacklogId(1L)
                .taskIds(List.of(task.getId()))
                .build();

    }

    //---------------
    @Test
    void testCreateUserStoryInBacklog() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStoryDTO result = userStoryService.createUserStoryInBacklog(userStoryDTO, 1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("UserStory 1", result.getTitre()),
                () -> verify(userStoryRepository, times(1)).save(userStory)
        );

    }

    //Exception test for creating a userStory
    @Test
    void testCreateUserStoryInBacklog_throwsException_WhenUSIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.createUserStoryInBacklog(userStoryDTO,null);
        });

        assertEquals("L'ID du backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testCreateUserStoryInBacklog_throwsException_WhenBacklogNotFound() {
        when(productBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.createUserStoryInBacklog(userStoryDTO,2L)
        );

        assertEquals("ProductBacklog non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testCreateUserStoryInBacklog_ShouldThrowException_WhenTitleEmpty() {
        userStory.setTitre("");
        userStoryDTO.setTitre("");

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.createUserStoryInBacklog(userStoryDTO, 1L);
        });

        assertEquals("Le titre de la user story ne peut pas être vide", exception.getMessage());
    }


    @Test
    void testCreateUserStoryInBacklog_ShouldThrowException_WhenDescriptionEmpty() {
        userStory.setDescription("");
        userStoryDTO.setDescription("");

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.createUserStoryInBacklog(userStoryDTO, 1L);
        });

        assertEquals("La description de la user story ne peut pas être vide", exception.getMessage());
    }

    //---------------
    @Test
    void testDeleteUserStoryById() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userStoryRepository).deleteById(1L);

        assertAll(
                ()-> assertDoesNotThrow(() -> userStoryService.deleteUserStoryById(1L)),
                () -> verify(userStoryRepository, times(1)).deleteById(1L)
        );
    }

    //Exception testing for deleting a userStory
    @Test
    void testDeleteUserStoryById_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                userStoryService.deleteUserStoryById(2L)
        );

        assertEquals("UserStory non trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testDeleteUserStoryById_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.deleteUserStoryById(null);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    //---------------
    @Test
    void testGetUserStoryById() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        UserStoryDTO result = userStoryService.getUserStoryById(1L);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(userStoryDTO.getTitre(), result.getTitre())
        );
    }
    //Exception test for get user by ID
    @Test
    void testGetUserStoryById_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.getUserStoryById(2L)
        );

        assertEquals("UserStory introuvable avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testGetUserStoryById_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.getUserStoryById(null);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    //---------------------------------Get user stories methods by different IDS--------------------------------
    @Test
    void testGetUserStoriesByBacklogId() {
        List<UserStory> userStories = List.of(userStory);
        when(userStoryRepository.findByProductBacklogId(1L)).thenReturn(userStories);

        List<UserStoryDTO> result = userStoryService.getUserStoriesByBacklogId(1L);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                ()->assertEquals(1, result.size()),
                () -> assertEquals(userStoryDTO.getTitre(), result.get(0).getTitre())
        );
    }
    //Exception test
    @Test
    void testGetUserStoriesByBacklogId_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.getUserStoriesByBacklogId(null);
        });

        assertEquals("L'ID du backlog ne peut pas être null", exception.getMessage());
    }
    //---------------------------
    @Test
    void testGetUserStoriesByEpicId() {
        List<UserStory> userStories = List.of(userStory);
        when(userStoryRepository.findByEpicId(1L)).thenReturn(userStories);

        List<UserStoryDTO> result = userStoryService.getUserStoriesByEpicId(1L);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                ()->assertEquals(1, result.size()),
                () -> assertEquals(userStoryDTO.getTitre(), result.get(0).getTitre())
        );
    }
    //Exception test
    @Test
    void testGetUserStoriesByEpicId_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.getUserStoriesByEpicId(null);
        });

        assertEquals("L'ID de l'Epic ne peut pas être null", exception.getMessage());
    }
    //--------------------------------
    @Test
    void testGetUserStoriesBySprintBacklogId() {
        List<UserStory> userStories = List.of(userStory);
        when(userStoryRepository.findBySprintBacklogId(1L)).thenReturn(userStories);

        List<UserStoryDTO> result = userStoryService.getUserStoriesBySprintBacklogId(1L);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                ()->assertEquals(1, result.size()),
                () -> assertEquals(userStoryDTO.getTitre(), result.get(0).getTitre())
        );
    }
    //Exception test
    @Test
    void testGetUserStoriesBySprintBacklogId_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.getUserStoriesBySprintBacklogId(null);
        });

        assertEquals("L'ID du sprint backlog ne peut pas être null", exception.getMessage());
    }
    //----------------------
    @Test
    void testGetUserStoriesByTaskId() {
        List<UserStory> userStories = List.of(userStory);
        when(userStoryRepository.findByTaskId(1L)).thenReturn(userStories);

        List<UserStoryDTO> result = userStoryService.getUserStoriesByTaskId(1L);

        assertAll(
            () -> assertNotNull(result, "Result should not be null"),
            () -> assertEquals(1, result.size()),
            () -> assertEquals(userStoryDTO.getTitre(), result.get(0).getTitre())
        );
    }
    //Exception test
    @Test
    void testGetUserStoriesByTaskId_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.getUserStoriesByTaskId(null);
        });

        assertEquals("L'ID du Task ne peut pas être null", exception.getMessage());
    }

    //--------------------------------------------------
    @Test
    void testUpdateUserStory() {
        UserStory updatedDetails = UserStory.builder()
                .id(1L)
                .titre("US 1 Updated")
                .description("Updated Description")
                .priorite(2)
                .statut(StatutEnum.IN_PROGRESS)
                .build();

        UserStoryDTO updatedDetailsDTO = UserStoryDTO.builder()
                .id(1L)
                .titre("US 1 Updated")
                .description("Updated Description")
                .priorite(2)
                .statut(StatutEnum.IN_PROGRESS)
                .build();

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(updatedDetails);

        UserStoryDTO result = userStoryService.updateUserStory(1L, updatedDetailsDTO);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals("US 1 Updated", result.getTitre()),
                () -> assertEquals(2, result.getPriorite()),
                () -> assertEquals(StatutEnum.IN_PROGRESS, result.getStatut())
        );
    }

    //Exception test
    @Test
    void testUpdateUserStory_ShouldThrowException_WhenIdIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.updateUserStory(null, userStoryDTO);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateUserStoryStatus() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        userStory.setStatut(StatutEnum.DONE);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStoryDTO updatedUserStory = userStoryService.updateUserStoryStatus(1L, StatutEnum.DONE);

        assertEquals(StatutEnum.DONE, updatedUserStory.getStatut());
    }

    //Exception test
    @Test
    void testUpdateUserStoryStatus_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.updateUserStoryStatus(null, StatutEnum.DONE);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateUserStoryPriority() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        userStory.setPriorite(5);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStoryDTO updatedUserStory = userStoryService.updateUserStoryPriority(1L, 5);

        assertEquals(5, updatedUserStory.getPriorite());
    }

    //Exception test
    @Test
    void testUpdateUserStoryPriority_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.updateUserStoryPriority(null, 6);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    //-------------------------------------------
    @Test
    void testRemoveUserStoryFromSprintBacklog(){
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        userStory.setSprintBacklog(sprintBacklog);

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO result = userStoryService.removeUserStoryFromSprintBacklog(1L, 1L);

        assertNotNull(result);
        assertNull(result.getSprintBacklogId());
        verify(sprintBacklogRepository, times(1)).save(sprintBacklog);
    }

    @Test
    void testRemoveUserStoryFromSprintBacklog_ShouldThrow_WhenSprintBacklogNotFound() {
        when(sprintBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.removeUserStoryFromSprintBacklog(2L, 1L)
        );

        assertEquals("SprintBacklog non trouvé ", exception.getMessage());
    }

    @Test
    void testRemoveUserStoryFromSprintBacklog_ShouldThrow_WhenUserStoryNotFound() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.removeUserStoryFromSprintBacklog(1L, 2L)
        );

        assertEquals("User Story non trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testRemoveUserStoryFromSprintBacklog_ShouldThrow_WhenNotAssigned() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userStoryService.removeUserStoryFromSprintBacklog(1L, 1L)
        );

        assertEquals("Cette User Story n'est pas assignée à ce Sprint", exception.getMessage());
    }

    //----------------------------------------------
    @Test
    void testAddUserStoryToSprintBacklogById() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO result = userStoryService.addUserStoryToSprintBacklogById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getSprintBacklogId());
        verify(sprintBacklogRepository, times(1)).save(sprintBacklog);
    }

    @Test
    void testAddUserStoryToSprintBacklogBySprintBacklogId_ShouldThrow_WhenSprintBacklogNotFound() {
        when(sprintBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.addUserStoryToSprintBacklogById(2L, 1L)
        );
        assertEquals("SprintBacklog introuvable", exception.getMessage());
    }

    @Test
    void testAddUserStoryToSprintBacklogBySprintBacklogId_ShouldThrow_WhenUserStoryNotFound() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.addUserStoryToSprintBacklogById(1L, 2L)
        );
        assertEquals("UserStory introuvable", exception.getMessage());
    }

    @Test
    void testAddUserStoryToSprintBacklogBySprintBacklogId_ShouldThrow_WhenSprintBacklogIdIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.addUserStoryToSprintBacklogById(null, 1L);
        });

        assertEquals("L'ID du sprint backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testAddUserStoryToSprintBacklogBySprintBacklogId_ShouldThrow_WhenUserStoryIdIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userStoryService.addUserStoryToSprintBacklogById(1L, null);
        });

        assertEquals("L'ID du userStory ne peut pas être null", exception.getMessage());
    }

    //----------------------------------------------------

    @Test
    void testAddUserStoryToEpic() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO result = userStoryService.addUserStoryToEpic(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getEpicId());
        assertTrue(epic.getUserStories().contains(userStory));
    }

    @Test
    void addUserStoryToEpic_ShouldThrow_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(2L)).thenReturn(Optional.empty());
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.addUserStoryToEpic(1L, 2L)
        );
        assertEquals("UserStory introuvable", exception.getMessage());
    }

    @Test
    void addUserStoryToEpic_ShouldThrow_WhenEpicNotFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                userStoryService.addUserStoryToEpic(2L, 1L)
        );
        assertEquals("Aucune Epic trouvée avec l'ID: 2", exception.getMessage());
    }
}
