package br.com.rodrigo.onlinelibraryapi.dtos.category;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDTO(@NotBlank String name) {

    
}
