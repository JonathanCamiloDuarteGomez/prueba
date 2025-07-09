package com.davivienda.movies.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CrearUsuarioDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 15, message = "El nombre debe tener entre 3 y 15 caracteres")
        String nombre,

        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El formato del correo electrónico es inválido")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Pattern(regexp = "\\d{6,8}", message = "La contraseña debe tener entre 6 y 8 dígitos numéricos")
        String contrasena
) {}
