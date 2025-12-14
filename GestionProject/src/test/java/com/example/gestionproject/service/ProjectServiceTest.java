package com.example.gestionproject.service;

import com.example.gestionproject.dto.ProjectDTO;
import com.example.gestionproject.model.Project;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.implementation.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .nom("Project 1")
                .description("Project Description")
                .build();

        projectDTO = ProjectDTO.builder()
                .id(1L)
                .nom("Project 1")
                .description("Project Description")
                .build();
    }

    @Test
    void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDTO result = projectService.createProject(projectDTO);

        assertNotNull(result);
        assertEquals("Project 1", result.getNom());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testGetAllProject() {
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectDTO> result = projectService.getAllProject();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDTO result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Project 1", result.getNom());
        verify(projectRepository, times(1)).findById(1L);
    }

    //Exception Testing
    @Test
    void testGetProjectById_ShouldThrow_WhenIdIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            projectService.getProjectById(null)
        );

        assertEquals("L'ID du projet ne peut pas être null", exception.getMessage());
    }

    @Test
    void testGetProjectById_ShouldThrow_WhenNotFound(){
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
            projectService.getProjectById(2L)
        );

        assertEquals("Aucun projet trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testDeleteProject(){
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        assertDoesNotThrow(() -> projectService.deleteProject(1L));

        verify(projectRepository, times(1)).deleteById(1L);
    }

    //Exception testing for deleting project
    @Test
    void testDeleteProject_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            projectService.deleteProject(null)
        );

        assertEquals("L'ID du projet ne peut pas être null", exception.getMessage());
    }

    @Test
    void testDeleteProject_ShouldThrowException_WhenNotFoundNull() {
        when(projectRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
            projectService.deleteProject(2L)
        );

        assertEquals("Project non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project updatedProject1 = Project.builder()
                .id(1L)
                .nom("Project Updated")
                .description("Updated description")
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject1);

        ProjectDTO updatedProject = ProjectDTO.builder()
                .id(1L)
                .nom("Project Updated")
                .description("Updated description")
                .build();

        ProjectDTO result = projectService.updateProject(1L, updatedProject);

        assertNotNull(result);
        assertEquals("Project Updated", result.getNom());
        assertEquals("Updated description", result.getDescription());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    //Exception testing for update method
    @Test
    void testUpdateProject_ShouldThrow_WhenNotFound() {
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
            projectService.updateProject(2L, projectDTO)
        );

        assertEquals("Project non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateProject_ShouldThrow_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            projectService.updateProject(null, projectDTO)
        );

        assertEquals("L'ID du projet ne peut pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "null, Description valide, Le titre du projet ne peut pas être vide",
            "'', Description valide, Le titre du projet ne peut pas être vide",
            "nom modifié, null, La description du projet ne peut pas être vide",
            "nom modifié, '', La description du projet ne peut pas être vide"
    })
    void updateProject_InvalidDTO_ShouldThrowIllegalArgumentException(
            String nom, String description, String expectedMessage) {

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        if ("null".equals(nom)) nom = null;
        if ("null".equals(description)) description = null;

        ProjectDTO invalidDTO = ProjectDTO.builder()
                .nom(nom)
                .description(description)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.updateProject(1L, invalidDTO));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
