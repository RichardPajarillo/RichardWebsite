package org.example.richardwebsite.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // 🌐 PUBLIC
    @Test
    void login_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    // 🔐 requires login
    @Test
    void cart_shouldRedirect_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void cart_shouldWork_whenLoggedIn() throws Exception {
        mockMvc.perform(get("/cart")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }

    // 👑 ADMIN ONLY
    @Test
    void admin_shouldFail_forUser() throws Exception {
        mockMvc.perform(get("/admin/dashboard")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_shouldPass_forAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    // 🔒 everything else locked
    @Test
    void home_shouldRequireLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}