package com.example.GestionProject.service.implementation;


import com.example.GestionProject.dto.UserDTO;
import com.example.GestionProject.model.User;
import com.example.GestionProject.repository.UserRepository;
import com.example.GestionProject.service.interfaces.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserInterface {

    private final UserRepository userRepository;

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private User convertToEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(userDTO.getRole());

        return convertToDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
