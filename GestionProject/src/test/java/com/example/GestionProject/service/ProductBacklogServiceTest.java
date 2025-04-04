package com.example.GestionProject.service;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.dto.ProjectDTO;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.ProjectRepository;
import com.example.GestionProject.service.implementation.ProductBacklogService;
import com.example.GestionProject.service.implementation.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private ProductBacklog backlog;
    private ProductBacklogDTO backlogDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        project = Project.builder().id(1L).build();

        backlog = ProductBacklog.builder()
                .id(1L)
                .nom("Backlog 1")
                .description("Description backlog 1")
                .project(project)
                .build();

        project.setProductBacklog(backlog);

        backlogDTO = ProductBacklogDTO.builder()
                .id(1L)
                .nom("Backlog 1")
                .description("Description backlog 1")
                .projectId(1L)
                .build();
    }

    @Test
    void testCreateProductBacklog() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(backlog);

        ProductBacklogDTO result = productBacklogService.createProductBacklog(backlogDTO);

        assertNotNull(result);
        assertEquals("Backlog 1", result.getNom());
        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    //Exception Testing for createProductBacklog method
    @Test
    void createProductBacklog_NomNull_ShouldThrowIllegalArgumentException() {
        ProductBacklogDTO backlog = new ProductBacklogDTO();
        backlog.setNom(null);
        backlog.setDescription("Description");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productBacklogService.createProductBacklog(backlog));
        assertEquals("Le nom du backlog ne peut pas être vide", exception.getMessage());
    }

    @Test
    void createProductBacklog_NomEmpty_ShouldThrowIllegalArgumentException() {
        ProductBacklogDTO backlog = new ProductBacklogDTO();
        backlog.setNom("");
        backlog.setDescription("Description");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productBacklogService.createProductBacklog(backlog));
        assertEquals("Le nom du backlog ne peut pas être vide", exception.getMessage());
    }
    @Test
    void createProductBacklog_DescriptionNull_ShouldThrowIllegalArgumentException() {
        ProductBacklogDTO backlog = new ProductBacklogDTO();
        backlog.setNom("Nom");
        backlog.setDescription(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productBacklogService.createProductBacklog(backlog));
        assertEquals("La description du backlog ne peut pas être vide", exception.getMessage());
    }

    @Test
    void createProductBacklog_DescriptionEmpty_ShouldThrowIllegalArgumentException() {
        ProductBacklogDTO backlog = new ProductBacklogDTO();
        backlog.setNom("Nom");
        backlog.setDescription("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productBacklogService.createProductBacklog(backlog));
        assertEquals("La description du backlog ne peut pas être vide", exception.getMessage());
    }

    //---------------
    @Test
    void testGetProductBacklogById() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        ProductBacklogDTO foundBacklog = productBacklogService.getProductBacklogById(1L);

        assertNotNull(foundBacklog);
        assertEquals("Backlog 1", foundBacklog.getNom());
    }

    //Exception testing for testGetProductBacklogById method
    @Test
    void getProductBacklogById_NotFound_ShouldThrowRuntimeException() {
        when(productBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> productBacklogService.getProductBacklogById(2L));
        assertEquals("ProductBacklog non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void getProductBacklogById_IdIsNull_ShouldThrowRuntimeException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productBacklogService.getProductBacklogById(null);
        });

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }
    //---------------
    @Test
    void testGetProductBacklogByProjectId() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProductBacklogDTO result = productBacklogService.getProductBacklogByProjectId(1L);

        assertNotNull(result);
        assertEquals("Backlog 1", result.getNom());
    }

    //Exception testing for get product Backlog by project ID
    @Test
    void getProductBacklogByProjectId_IdIsNull_ShouldThrowRuntimeException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productBacklogService.getProductBacklogByProjectId(null);
        });

        assertEquals("L'ID du projet ne peut pas être null", exception.getMessage());
    }

    @Test
    void getProductBacklogByProjectId_ShouldThrowRuntimeException_WhenProjectNotFound() {
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                productBacklogService.getProductBacklogByProjectId(2L)
        );
        assertEquals("Projet non trouvé avec l'ID: 2", exception.getMessage());
    }


    //---------------
    @Test
    void testGetAllProductBacklogs() {
        when(productBacklogRepository.findAll()).thenReturn(List.of(backlog));

        List<ProductBacklogDTO> result = productBacklogService.getAllProductBacklogs();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productBacklogRepository, times(1)).findAll();
    }

    //---------------
    @Test
    void testDeleteProductBacklog() {
        when(productBacklogRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productBacklogRepository).deleteById(1L);

        assertDoesNotThrow(() -> productBacklogService.deleteProductBacklog(1L));

        verify(productBacklogRepository, times(1)).deleteById(1L);
    }

    //Exception testing for deleting product backlogs
    @Test
    void testDeleteProductBacklog_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productBacklogService.deleteProductBacklog(null);
        });

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testDeleteProductBacklog_ShouldThrowException_WhenBacklogNotFound() {
        when(productBacklogRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> productBacklogService.deleteProductBacklog(2L));
        assertEquals("ProductBacklog non trouvée avec l'ID: 2", exception.getMessage());
    }

    //---------------
    @Test
    void testUpdateProductBacklog() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(backlog);

        ProductBacklogDTO updatedBacklog = ProductBacklogDTO.builder()
                .id(1L)
                .nom("Backlog Updated")
                .description("Updated description")
                .projectId(1L)
                .build();

        ProductBacklogDTO result = productBacklogService.updateProductBacklog(1L, updatedBacklog);

        assertNotNull(result);
        assertEquals("Backlog Updated", result.getNom());
        assertEquals("Updated description", result.getDescription());
        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    //Exception testing for update method
    @Test
    void testUpdateProductBacklog_ShouldThrowException_WhenBacklogNotFound() {
        when(productBacklogRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productBacklogService.updateProductBacklog(2L, backlogDTO);
        });

        assertEquals("ProductBacklog non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateProductBacklog_ShouldThrowException_WhenBacklogIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productBacklogService.updateProductBacklog(null, backlogDTO);
        });

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testUpdateProductBacklog_NomNull_ShouldThrowIllegalArgumentException() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        ProductBacklogDTO invalidDTO = ProductBacklogDTO.builder()
                .nom(null)
                .description("Description valide")
                .build();


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.updateProductBacklog(1L, invalidDTO));
        assertEquals("Le nom du backlog ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateProductBacklog_NomEmpty_ShouldThrowIllegalArgumentException() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        ProductBacklogDTO invalidDTO = ProductBacklogDTO.builder()
                .nom("")
                .description("Description valide")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.updateProductBacklog(1L, invalidDTO));

        assertEquals("Le nom du backlog ne peut pas être vide", exception.getMessage());
    }
    @Test
    void testUpdateProductBacklog_DescriptionNull_ShouldThrowIllegalArgumentException() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        ProductBacklogDTO invalidDTO = ProductBacklogDTO.builder()
                .nom("nom modifié")
                .description(null)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.updateProductBacklog(1L, invalidDTO));
        assertEquals("La description du backlog ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateProductBacklog_DescriptionEmpty_ShouldThrowIllegalArgumentException() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        ProductBacklogDTO invalidDTO = ProductBacklogDTO.builder()
                .nom("nom modifié")
                .description(null)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.updateProductBacklog(1L, invalidDTO));
        assertEquals("La description du backlog ne peut pas être vide", exception.getMessage());
    }



}
