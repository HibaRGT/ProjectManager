package com.example.gestionproject.controller;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.service.implementation.ProductBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/productBacklog")
public class ProductBacklogController {
    private final ProductBacklogService productBacklogService;

    @Autowired
    public ProductBacklogController(ProductBacklogService productBacklogService) {
        this.productBacklogService = productBacklogService;
    }

    @PostMapping
    public ResponseEntity<ProductBacklogDTO> createProductBacklog(@RequestBody ProductBacklogDTO backlogDto) {
        ProductBacklogDTO createdBacklog = productBacklogService.createProductBacklog(backlogDto);
        return new ResponseEntity<>(createdBacklog, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> getProductBacklogById(@PathVariable Long id) {
        ProductBacklogDTO backlog = productBacklogService.getProductBacklogById(id);
        return new ResponseEntity<>(backlog, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductBacklogDTO>> getAllProductBacklogs() {
        List<ProductBacklogDTO> backlogs = productBacklogService.getAllProductBacklogs();
        return new ResponseEntity<>(backlogs, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProductBacklogDTO> getProductBacklogByProjectId(@PathVariable Long projectId) {
        ProductBacklogDTO backlog = productBacklogService.getProductBacklogByProjectId(projectId);
        return new ResponseEntity<>(backlog, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBacklog(@PathVariable Long id) {
        productBacklogService.deleteProductBacklog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> updateProductBacklog(@PathVariable Long id, @RequestBody ProductBacklogDTO backlogDto) {
        ProductBacklogDTO updatedBacklog = productBacklogService.updateProductBacklog(id, backlogDto);
        return new ResponseEntity<>(updatedBacklog, HttpStatus.OK);
    }
}
