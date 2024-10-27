package com.medic.system.services;

import com.medic.system.entities.Doctor;
import com.medic.system.entities.User;
import com.medic.system.repositories.DoctorRepository;
import com.medic.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean isCurrentUser(Long userId, String authenticatedUsername) {
        return userRepository.findById(userId)
            .map(user -> user.getUsername().equals(authenticatedUsername))
            .orElse(false);
    }

    public List<Doctor> findAllGeneralPractitioners() {
        return doctorRepository.findAllByIsGeneralPractitioner(true);
    }
}
