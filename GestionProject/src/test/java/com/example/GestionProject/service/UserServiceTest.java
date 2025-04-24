package com.example.GestionProject.service;


import com.example.GestionProject.dto.UserDTO;
import com.example.GestionProject.model.RoleName;
import com.example.GestionProject.model.User;
import com.example.GestionProject.repository.UserRepository;
import com.example.GestionProject.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .role(RoleName.PRODUCT_OWNER)
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .role(RoleName.PRODUCT_OWNER)
                .build();
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO created = userService.createUser(userDTO);

        assertThat(created).isNotNull();
        assertThat(created.getUsername()).isEqualTo("john_doe");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertThat(result.getUsername()).isEqualTo("john_doe");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with ID: 1");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updated = userService.updateUser(1L, userDTO);

        assertThat(updated.getUsername()).isEqualTo("john_doe");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}

