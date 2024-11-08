package com.springboot.integration;

import com.springboot.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginJson = String.format("{\"user\":\"%s\", \"password\":\"%s\"}", username, password);

        return mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAdminCanCreatePerson() throws Exception {
        var token = loginAndGetToken("admin", "password");
        String personJson = "{\"name\": \"John Doe\", \"age\": 30}";
        var previousCount = personRepository.count();

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Verify that the person was created in the repository
        assertEquals(previousCount + 1, personRepository.count());
    }

    @Test
    public void testUserCannotDeletePerson() throws Exception {
        var token = loginAndGetToken("user", "password");
        String personJson = "{\"name\": \"Unauthorized Person\", \"age\": 40}";
        var previousCount = personRepository.count();

        mockMvc.perform(delete("/v1/persons/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        // Verify that no person was created in the repository
        assertEquals(previousCount, personRepository.count());
    }

    @Test
    public void testUnauthenticatedUserCannotCreatePerson() throws Exception {
        String personJson = "{\"name\": \"Unauthorized Person\", \"age\": 40}";
        var previousCount = personRepository.count();

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isUnauthorized());

        // Verify that no person was created in the repository
        assertEquals(previousCount, personRepository.count());
    }
}