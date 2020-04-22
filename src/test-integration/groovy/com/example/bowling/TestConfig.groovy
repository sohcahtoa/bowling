package com.example.bowling

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Configuration
class TestConfig {
    @Autowired
    WebApplicationContext context

    @Bean
    MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(context).build()
    }
}
