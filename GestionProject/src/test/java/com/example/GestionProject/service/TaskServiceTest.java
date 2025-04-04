package com.example.GestionProject.service;

import com.example.GestionProject.dto.TaskDTO;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.TaskRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.implementation.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        userStory = UserStory.builder()
                .id(1L)
                .titre("US 1")
                .sprintBacklog(SprintBacklog.builder().id(1L).build())
                .tasks(new ArrayList<>())
                .build();

        task = Task.builder()
                .id(1L)
                .title("Task 1")
                .description("Description")
                .status(TaskStatus.TO_DO)
                .userStory(userStory)
                .build();

        taskDTO = TaskDTO.builder()
                .id(1L)
                .title("Task 1")
                .description("Description")
                .status(TaskStatus.TO_DO)
                .userStoryId(1L)
                .build();

        userStory.getTasks().add(task);
    }

    // CREATE TESTS
    @Test
    void createTaskForUserStory_ShouldReturnDTO_WhenValid() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTaskForUserStory(1L, taskDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Task 1", result.getTitle()),
                () -> verify(taskRepository, times(1)).save(any())
        );
    }

    @Test
    void createTaskForUserStory_ShouldThrow_WhenUserStoryNotInSprint() {
        userStory.setSprintBacklog(null);
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        assertThrows(IllegalStateException.class, () ->
                taskService.createTaskForUserStory(1L, taskDTO)
        );
    }

    @Test
    void createTaskForUserStory_ShouldThrow_WhenTitleEmpty() {
        taskDTO.setTitle("");
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        assertThrows(IllegalArgumentException.class, () ->
                taskService.createTaskForUserStory(1L, taskDTO)
        );
    }

    // GET TESTS
    @Test
    void getTaskById_ShouldReturnDTO_WhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTaskById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId())
        );
    }

    @Test
    void getTaskById_ShouldThrow_WhenNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                taskService.getTaskById(2L)
        );

        assertEquals("Tâche non trouvée avec l'ID: 2", exception.getMessage());
    }

    @Test
    void getTasksByUserStoryId_ShouldReturnList_WhenExists() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        List<TaskDTO> results = taskService.getTasksByUserStoryId(1L);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(1, results.size()),
                () -> assertEquals("Task 1", results.get(0).getTitle())
        );
    }

    // UPDATE TESTS
    @Test
    void updateTask_ShouldReturnUpdatedDTO_WhenValid() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        TaskDTO updatedDTO = TaskDTO.builder()
                .title("Updated Title")
                .description("Updated Desc")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        TaskDTO result = taskService.updateTask(1L, updatedDTO);

        assertAll(
                () -> assertEquals("Updated Title", result.getTitle()),
                () -> assertEquals("Updated Desc", result.getDescription())
        );
    }

    @Test
    void updateTaskStatus_ShouldChangeStatus_WhenValid() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        TaskDTO result = taskService.updateTaskStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    // DELETE TESTS
    @Test
    void deleteTask_ShouldRemoveFromUserStory_WhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

}