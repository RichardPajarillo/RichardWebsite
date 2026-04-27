package org.example.richardwebsite.test.controller;

import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.OrderRepository;
import org.example.richardwebsite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test") // Uses src/test/resources/application-test.properties
@Transactional // Rolls back database changes after every test
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User adminUser;
    private User otherUser;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Clear database to ensure a clean state
        userRepository.deleteAll();

        // Create actual entities in H2
        adminUser = new User("adminUser", "pass123", "ADMIN");
        userRepository.save(adminUser);

        otherUser = new User("otherUser", "pass456", "USER");
        userRepository.save(otherUser);
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void manageUsers_shouldReturnUserListFromDatabase() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                // Verify the data from H2 is present in the model
                .andExpect(model().attribute("users", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(content().string(containsString("adminUser")))
                .andExpect(content().string(containsString("otherUser")));
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void addUser_shouldSaveToH2Database() throws Exception {
        mockMvc.perform(post("/admin/users/add")
                        .param("username", "databaseUser")
                        .param("password", "secret789")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        // Verify it actually exists in the H2 DB now
        assertTrue(userRepository.findByUsername("databaseUser").isPresent());
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void deleteUser_shouldActuallyRemoveFromH2() throws Exception {
        Long idToDelete = otherUser.getId();

        mockMvc.perform(post("/admin/users/delete/" + idToDelete)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        // Verify it is gone from H2
        assertFalse(userRepository.findById(idToDelete).isPresent());
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void updateRole_shouldUpdateH2Record() throws Exception {
        mockMvc.perform(post("/admin/users/updateRole")
                        .param("userId", otherUser.getId().toString())
                        .param("newRole", "ADMIN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        // Refresh from DB and verify
        User updated = userRepository.findById(otherUser.getId()).get();
        assertEquals("ADMIN", updated.getRole());
    }
}