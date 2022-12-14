package com.evgKuznetsov.expert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT, printOnlyOnFailure = false)
@ActiveProfiles("dev")
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static Stream<Locale> getLocale() {
        return Stream.of(new Locale("ru", "RU"));
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @Configuration
    static class ConfigTests {
        @Autowired
        void injectObjectMapper(ObjectMapper objectMapper) {
            JsonHelper.setObjectMapper(objectMapper);
        }
    }
}
