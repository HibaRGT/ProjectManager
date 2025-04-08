package com.example.GestionProject.controller;

import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.service.implementation.UserStoryService;
import com.example.GestionProject.model.StatutEnum;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userStory")
public class UserStoryController {
    private final UserStoryService userStoryService;

    @Autowired
    public UserStoryController(UserStoryService userStoryService) {
        this.userStoryService = userStoryService;
    }

    @PostMapping("/backlog/{backlogId}")
    public ResponseEntity<UserStoryDTO> createUserStoryInBacklog(@RequestBody UserStoryDTO userStoryDTO, @PathVariable Long backlogId) {
        UserStoryDTO createdUserStory = userStoryService.createUserStoryInBacklog(userStoryDTO, backlogId);
        return new ResponseEntity<>(createdUserStory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(@PathVariable Long id) {
        UserStoryDTO userStory = userStoryService.getUserStoryById(id);
        return new ResponseEntity<>(userStory, HttpStatus.OK);
    }

    @GetMapping("/backlog/{backlogId}")
    public ResponseEntity<List<UserStoryDTO>> getUserStoriesByBacklogId(@PathVariable Long backlogId) {
        List<UserStoryDTO> userStories = userStoryService.getUserStoriesByBacklogId(backlogId);
        return new ResponseEntity<>(userStories, HttpStatus.OK);
    }

    @GetMapping("/sprintBacklog/{sprintBacklogId}")
    public ResponseEntity<List<UserStoryDTO>> getUserIdBySprintBacklogId(@PathVariable Long sprintBacklogId) {
        List<UserStoryDTO> userStories=userStoryService.getUserStoriesBySprintBacklogId(sprintBacklogId);
        return new ResponseEntity<>(userStories, HttpStatus.OK);
    }

    @GetMapping("epic/{epicId}")
    public  ResponseEntity<List<UserStoryDTO>> getUserStoryByEpicId(@PathVariable Long epicId) {
        List<UserStoryDTO> userStories=userStoryService.getUserStoriesByEpicId(epicId);
        return new ResponseEntity<>(userStories, HttpStatus.OK);
    }

    @GetMapping("task/{taskId}")
    public  ResponseEntity<List<UserStoryDTO>> getUserStoryByTaskId(@PathVariable Long taskId) {
        List<UserStoryDTO> userStories=userStoryService.getUserStoriesByTaskId(taskId);
        return new ResponseEntity<>(userStories, HttpStatus.OK);
    }

    /*@PostMapping("/sprintBacklog/{userStoryId}")
    public ResponseEntity<UserStoryDTO> addUserStoryToSprintBacklogBySprintId(@RequestBody Long userStoryId, @PathVariable Long sprintId ) {
        UserStoryDTO userStoryDTO1=userStoryService.addUserStoryToSprintBacklogBySprintId(sprintId,userStoryId);
        return new ResponseEntity<>(userStoryDTO1,HttpStatus.OK);
    }*/

    @PostMapping("/{sprintBacklogId}/{userStoryId}")
    public ResponseEntity<UserStoryDTO> addUserStoryToSprintBacklogById(
            @PathVariable Long sprintBacklogId,
            @PathVariable Long userStoryId) {

        UserStoryDTO userStoryDTO = userStoryService.addUserStoryToSprintBacklogById(sprintBacklogId, userStoryId);
        return ResponseEntity.ok(userStoryDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(@PathVariable Long id, @RequestBody UserStoryDTO userStoryDetails) {
        UserStoryDTO updatedUserStory = userStoryService.updateUserStory(id, userStoryDetails);
        return new ResponseEntity<>(updatedUserStory, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStoryById(@PathVariable Long id) {
        userStoryService.deleteUserStoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
