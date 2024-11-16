package com.medic.system.controllers.web;

import com.medic.system.entities.Doctor;
import com.medic.system.helpers.ThymeleafHelper;
import com.medic.system.services.DoctorService;
import com.medic.system.services.SpecialityService;
import com.medic.system.validators.UniqueValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateDoctorBindingErrors() throws Exception {
        mockMvc.perform(post("/doctors/create")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "password")
                        .param("isGeneralPractitioner", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("doctor", "username"))
                .andExpect(view().name("doctors/create"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testEditDoctorBindingErrors() throws Exception {
        when(doctorService.findById(anyLong())).thenReturn(new Doctor());
        mockMvc.perform(post("/doctors/edit/1")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "password")
                        .param("isGeneralPractitioner", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("doctor", "username"))
                .andExpect(view().name("doctors/edit"));
    }

    @Test
    @WithUserDetails("patient")
    public void testEditDoctorAccessDeniedForPatient() throws Exception {
        mockMvc.perform(get("/doctors/edit/1"))
                .andExpect(status().isForbidden());
    }
}