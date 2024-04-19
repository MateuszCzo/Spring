package mc.project1.restapi.controller;

import jakarta.transaction.Transactional;
import mc.project1.restapi.model.User;
import mc.project1.restapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.InetAddress;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void shouldLoginAndGetContent() throws Exception
    {
        User user = new User();
        user.setUsername("test@test.com");
        user.setPassword("passwordtest");
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);

        String content = "{\"username\": \"" + user.getUsername() + "\", \"password\": \"" + user.getPassword() + "\"}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length()));
        httpHeaders.add(HttpHeaders.HOST, InetAddress.getLocalHost().getHostAddress());
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        MvcResult login = mockMvc.perform(post("/auth/authenticate")
                        .headers(httpHeaders)
                        .content(content))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        String token = login.getResponse().getContentAsString();

        System.out.println("token: " + token);

        mockMvc.perform(get("/auth/secured")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("secured"));

        mockMvc.perform(get("/auth/secured"))
                .andDo(print())
                .andExpect(status().is(403));
    }
}