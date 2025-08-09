package com.server_ns.nomad_score;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloShouldReturnHelloWorld() {
        String response = restTemplate.getForObject("http://localhost:" + port + "/hello", String.class);
        assertThat(response).isEqualTo("hello world");
    }

    @Test
    void helloTestingShouldReturnHelloTesting() {
        String response = restTemplate.getForObject("http://localhost:" + port + "/hello/testing", String.class);
        assertThat(response).isEqualTo("Hello Testing");
    }

    @Test
    void indexShouldReturnHelloWorld() {
        String response = restTemplate.getForObject("http://localhost:" + port + "/", String.class);
        assertThat(response).isEqualTo("Hello World");
    }
} 