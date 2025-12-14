package com.example.gestionproject.service;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.Project;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.implementation.ProductBacklogService;
import com.example.gestionproject.validator.ProductBacklogValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private ProjectRepository projectRepository;

    private final ProductBacklogValidator validator = new ProductBacklogValidator();

    private ProductBacklogService productBacklogService;

    private ProductBacklog backlog;
    private ProductBacklogDTO backlogDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        productBacklogService = new ProductBacklogService(productBacklogRepository, projectRepository, validator);
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
    @ParameterizedTest
    @CsvSource({
            "null, Description, Le nom du backlog ne peut pas être vide",
            "'', Description, Le nom du backlog ne peut pas être vide",
            "Nom, null, La description du backlog ne peut pas être vide",
            "Nom, '', La description du backlog ne peut pas être vide"
    })
    void createProductBacklog_InvalidDTO_ShouldThrowIllegalArgumentException(
            String nom, String description, String expectedMessage) {

        if ("null".equals(nom)) nom = null;
        if ("null".equals(description)) description = null;

        ProductBacklogDTO backlog1 = new ProductBacklogDTO();
        backlog1.setNom(nom);
        backlog1.setDescription(description);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.createProductBacklog(backlog1));

        assertEquals(expectedMessage, exception.getMessage());
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
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productBacklogService.getProductBacklogById(null)
        );

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
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productBacklogService.getProductBacklogByProjectId(null)
    );

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
        when(productBacklogRepository.findById(1L))
                .thenReturn(Optional.of(backlog));

        assertDoesNotThrow(() -> productBacklogService.deleteProductBacklog(1L));
        verify(productBacklogRepository).delete(backlog);
    }

    //Exception testing for deleting product backlogs
    @Test
    void testDeleteProductBacklog_ShouldThrowException_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productBacklogService.deleteProductBacklog(null)
        );

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }

    @Test
    void testDeleteProductBacklog_ShouldThrowException_WhenBacklogNotFound() {
        when(productBacklogRepository.findById(2L))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> productBacklogService.deleteProductBacklog(2L));
        assertEquals("ProductBacklog non trouvé avec l'ID: 2", exception.getMessage());
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

        Exception exception = assertThrows(RuntimeException.class, () ->
            productBacklogService.updateProductBacklog(2L, backlogDTO)
        );

        assertEquals("ProductBacklog non trouvé avec l'ID: 2", exception.getMessage());
    }

    @Test
    void testUpdateProductBacklog_ShouldThrowException_WhenBacklogIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productBacklogService.updateProductBacklog(null, backlogDTO)
        );

        assertEquals("L'ID du produit backlog ne peut pas être null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "null, Description valide, Le nom du backlog ne peut pas être vide",
                "'', Description valide, Le nom du backlog ne peut pas être vide",
                "nom modifié, null, La description du backlog ne peut pas être vide",
                "nom modifié, '', La description du backlog ne peut pas être vide"
    })
    void testUpdateProductBacklog_InvalidDTO_ShouldThrowIllegalArgumentException(
            String nom, String description, String expectedMessage) {

        if ("null".equals(nom)) nom = null;
        if ("null".equals(description)) description = null;

        ProductBacklogDTO invalidDTO = ProductBacklogDTO.builder()
                .nom(nom)
                .description(description)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> productBacklogService.updateProductBacklog(1L, invalidDTO));

        assertEquals(expectedMessage, exception.getMessage());
    }


}
