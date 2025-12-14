package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.TaskDTO;
import com.example.gestionproject.model.Task;
import com.example.gestionproject.model.TaskStatus;
import com.example.gestionproject.model.UserStory;

public class TaskMapper {

    private TaskMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userStoryId( task.getUserStory() != null ? task.getUserStory().getId() : null)
                .build();
    }

    public static Task toEntity(TaskDTO dto, UserStory userStory) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TO_DO)
                .userStory(userStory)
                .build();
    }
}
