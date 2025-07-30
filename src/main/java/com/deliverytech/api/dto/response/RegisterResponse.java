// Exemplo de RegisterResponse.java
package com.deliverytech.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.deliverytech.api.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String nome;
    private String email;
    private Role role;
}