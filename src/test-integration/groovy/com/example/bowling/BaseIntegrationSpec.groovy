package com.example.bowling

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(["integration-test"])
class BaseIntegrationSpec extends Specification {
    @Autowired
    ObjectMapper objectMapper

    @Autowired
    private MockMvc mvc

    protected String baseUri() {
        return String.format("http://%s:%d%s", "localhost", 8080, "/bowling")
    }

    protected <T> T responseToClass(MockHttpServletResponse response, Class<T> clazz) {
        return objectMapper.readValue(response.getContentAsByteArray(), clazz)
    }

    protected MockHttpServletResponse createGame(List<String> players, ResultMatcher expectedStatus = status().isCreated()) {
        return mvc.perform(
                post(baseUri() + "/game")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(players)))
                .andDo(print()).andExpect(expectedStatus).andReturn().getResponse()
    }

    protected MockHttpServletResponse findGame(UUID gameId, ResultMatcher expectedStatus = status().isOk()) {
        return mvc.perform(
                get(baseUri() + "/game/${gameId}")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andDo(print()).andExpect(expectedStatus).andReturn().getResponse()
    }
}
