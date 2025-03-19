package com.example.GestionProject.controller;

import com.example.GestionProject.dto.TaskDTO;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;
import com.example.GestionProject.service.implementation.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/userStory/{userStoryId}")
    public ResponseEntity<TaskDTO> createTaskForUserStory(@PathVariable Long userStoryId, @RequestBody TaskDTO taskDto) {
        TaskDTO createdTask = taskService.createTaskForUserStory(userStoryId, taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/userStory/{userStoryId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserStoryId(@PathVariable Long userStoryId) {
        List<TaskDTO> tasks = taskService.getTasksByUserStoryId(userStoryId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO updatedTask) {
        TaskDTO task = taskService.updateTask(taskId, updatedTask);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
