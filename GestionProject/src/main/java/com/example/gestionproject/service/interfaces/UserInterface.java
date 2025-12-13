package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.UserDTO;
import java.util.List;

public interface UserInterface {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
