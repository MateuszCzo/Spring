package mc.project.online_store.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class SecurityConfigTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenUserRoleIsAdmin_whenSecurityFilterChain_thenAccessToAdminEndpointsShouldBeAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenUserRoleIsUser_whenSecurityFilterChain_thenAccessToAdminEndpointsShouldBeDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));
    }

    @Test
    public void givenUserRoleIsAnonymous_whenSecurityFilterChain_thenAccessToAdminEndpointsShouldBeDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void givenUserRoleIsUser_whenSecurityFilterChain_thenAccessToUserEndpointsShouldBeAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cart/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));

        mockMvc.perform(MockMvcRequestBuilders.get("/address/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));

        mockMvc.perform(MockMvcRequestBuilders.get("/contact/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));
    }

    @Test
    public void givenUserRoleIsAnonymous_whenSecurityFilterChain_thenAccessToUserEndpointsShouldBeDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cart/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));

        mockMvc.perform(MockMvcRequestBuilders.get("/address/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));

        mockMvc.perform(MockMvcRequestBuilders.get("/contact/address"))
                .andExpect(status().is(anyOf(is(401), is(403))));
    }

    @Test
    public void givenUserRoleIsAnonymous_whenSecurityFilterChain_thenAccessToPublicEndpointsShouldBeAllowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/address"))
                .andExpect(status().is(not(anyOf(is(401), is(403)))));
    }
}
