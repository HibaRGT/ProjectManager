package com.example.GestionProject.service;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.repository.ProductBacklogRepository;
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

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private ProductBacklog backlog;

    @BeforeEach
    void setUp() {
        backlog = new ProductBacklog(1L, "Backlog 1", "Description du backlog", null, null);
    }

    @Test
    void testCreateProductBacklog() {
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(backlog);

        ProductBacklog createdBacklog = productBacklogService.createProductBacklog(backlog);

        assertNotNull(createdBacklog);
        assertEquals("Backlog 1", createdBacklog.getNom());
        verify(productBacklogRepository, times(1)).save(backlog);
    }

    @Test
    void testGetProductBacklogById() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        ProductBacklog foundBacklog = productBacklogService.getProductBacklogById(1L);

        assertNotNull(foundBacklog);
        assertEquals(1L, foundBacklog.getId());
    }
    @Test
    void testGetAllProductBacklogs() {
        List<ProductBacklog> backlogs = Arrays.asList(
                new ProductBacklog(1L, "Backlog 1", "Description 1", null, null),
                new ProductBacklog(2L, "Backlog 2", "Description 2", null, null)
        );

        when(productBacklogRepository.findAll()).thenReturn(backlogs);

        List<ProductBacklog> result = productBacklogService.getAllProductBacklogs();

        assertEquals(2, result.size());
        verify(productBacklogRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProductBacklog() {
        doNothing().when(productBacklogRepository).deleteById(1L);

        assertDoesNotThrow(() -> productBacklogService.deleteProductBacklog(1L));

        verify(productBacklogRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateProductBacklog() {
        ProductBacklog updatedBacklog = new ProductBacklog(1L, "Backlog Updated", "Updated description", null, null);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(updatedBacklog);

        ProductBacklog result = productBacklogService.updateProductBacklog(1L, updatedBacklog);

        assertNotNull(result);
        assertEquals("Backlog Updated", result.getNom());
        assertEquals("Updated description", result.getDescription());
        verify(productBacklogRepository, times(1)).save(backlog);
    }

}
