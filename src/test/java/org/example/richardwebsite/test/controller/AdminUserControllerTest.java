package org.example.richardwebsite.test.controller;

import static org.hamcrest.Matchers.hasSize;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import jakarta.servlet.ServletException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserRepository userRepository;

    private User adminUser;
    private User otherUser;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // 1. INITIALIZE the users to prevent NullPointerException
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("adminUser");
        adminUser.setRole("ADMIN");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otherUser");
        otherUser.setRole("USER");

        // 2. Setup default pagination behavior for the 'manageUsers' page
        List<User> userList = List.of(adminUser, otherUser);
        Page<User> userPage = new PageImpl<>(userList, PageRequest.of(0, 10), userList.size());

        // Use any(PageRequest.class) to ensure it always matches
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void manageUsers_shouldReturnUserList() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("userPage"))
                .andExpect(model().attribute("users", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void updateRole_shouldPreventSelfRoleChange() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(post("/admin/users/updateRole")
                        .param("userId", "1")
                        .param("newRole", "USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users?error=selfRoleChange"));
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void updateRole_shouldSuccessForOtherUser() throws Exception {
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));

        mockMvc.perform(post("/admin/users/updateRole")
                        .param("userId", "2")
                        .param("newRole", "ADMIN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void deleteUser_shouldSuccessForOtherUser() throws Exception {
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));

        mockMvc.perform(post("/admin/users/delete/2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        // CHANGE THIS LINE: Verify 'delete' instead of 'deleteById'
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void addUser_shouldRedirectToManageUsers() throws Exception {
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/users/add")
                        .flashAttr("user", new User())
                        .param("username", "newUser")
                        .param("password", "securePassword123") // ADD THIS LINE
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }


    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void updateRole_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/admin/users/updateRole")
                    .param("userId", "99")
                    .param("newRole", "ADMIN")
                    .with(csrf()));
        });
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void showAddUserForm_shouldReturnView() throws Exception {
        mockMvc.perform(get("/admin/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-add-user"))
                .andExpect(model().attributeExists("user"));
    }

    // Updated test for the rejectValue approach
    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void addUser_shouldShowErrorIfUsernameExists() throws Exception {
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/admin/users/add")
                        .param("username", "existingUser")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().isOk()) // Expect 200 OK now
                .andExpect(view().name("admin-add-user")) // Stays on the form
                .andExpect(model().hasErrors()); // Check that errors exist
    }
}