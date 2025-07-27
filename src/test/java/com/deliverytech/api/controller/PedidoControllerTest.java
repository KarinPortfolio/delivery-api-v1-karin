package com.deliverytech.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.deliverytech.api.config.SwaggerTestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SwaggerTestSecurityConfig.class)
@ActiveProfiles("test")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarPedidos() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk());
    }
}
