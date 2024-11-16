package com.medic.system.services;

import com.medic.system.dtos.user.BaseUserRequestDto;
import com.medic.system.dtos.user.EditBaseUserRequestDto;
import com.medic.system.entities.User;
import com.medic.system.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private User createSampleUser(Long id, String username, String password, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    @Test
    void create_ValidDto_UserCreated() {
        BaseUserRequestDto dto = new BaseUserRequestDto();
        dto.setUsername("admin");
        dto.setPassword("password");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User createdUser = adminService.create(dto, bindingResult);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        assertEquals("admin", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(bindingResult);
    }

    @Test
    void create_NullDto_BindingError() {
        User createdUser = adminService.create(null, bindingResult);

        assertNull(createdUser);
        verify(bindingResult, times(1)).rejectValue("username", "error.user", "Грешка при създаване на администратор");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void create_DataIntegrityViolation_BindingError() {
        BaseUserRequestDto dto = new BaseUserRequestDto();
        dto.setUsername("admin");
        dto.setPassword("password");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        User createdUser = adminService.create(dto, bindingResult);

        assertNull(createdUser);
        verify(userRepository, times(1)).save(any(User.class));
        verify(bindingResult, times(1)).rejectValue("username", "error.user", "Грешка при създаване на администратор");
    }

    @Test
    void update_ValidDto_UserUpdated() {
        Long userId = 1L;
        EditBaseUserRequestDto dto = new EditBaseUserRequestDto();
        dto.setUsername("updatedAdmin");
        dto.setPassword("newPassword");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        User existingUser = createSampleUser(userId, "admin", "oldPassword", "John", "Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User currentUser = createSampleUser(userId, "admin", "oldPassword", "John", "Doe");
        when(authentication.getPrincipal()).thenReturn(currentUser);

        User updatedUser = adminService.update(userId, dto, bindingResult);

        assertNotNull(updatedUser);
        assertEquals("updatedAdmin", updatedUser.getUsername());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(bindingResult);
    }

    @Test
    void update_NullDto_BindingError() {
        Long userId = 1L;

        User updatedUser = adminService.update(userId, null, bindingResult);

        assertNull(updatedUser);
        verify(bindingResult, times(1)).rejectValue("username", "error.user", "Грешка при редакция на потребител");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_UserNotFound_BindingError() {
        Long userId = 1L;
        EditBaseUserRequestDto dto = new EditBaseUserRequestDto();
        dto.setUsername("updatedAdmin");
        dto.setPassword("newPassword");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User updatedUser = adminService.update(userId, dto, bindingResult);

        assertNull(updatedUser);
        verify(userRepository, times(1)).findById(userId);
        verify(bindingResult, times(1)).rejectValue("username", "error.user", "Администраторът не съществува");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void update_DataIntegrityViolation_BindingError() {
        Long userId = 1L;
        EditBaseUserRequestDto dto = new EditBaseUserRequestDto();
        dto.setUsername("admin");
        dto.setPassword("newPassword");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        User existingUser = createSampleUser(userId, "admin", "oldPassword", "John", "Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        User updatedUser = adminService.update(userId, dto, bindingResult);

        assertNull(updatedUser);
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(any(User.class));
        verify(bindingResult, times(1)).rejectValue("username", "error.user", "Грешка при редактиране на администратор");
    }

    @Test
    void findById_UserExists_ReturnsUser() {
        Long userId = 1L;
        User user = createSampleUser(userId, "admin", "pass", "John", "Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = adminService.findById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_UserDoesNotExist_ThrowsException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adminService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}
