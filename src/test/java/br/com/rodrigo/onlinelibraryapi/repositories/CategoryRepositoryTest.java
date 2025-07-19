package br.com.rodrigo.onlinelibraryapi.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.entities.Category.CategoryBuilder;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("test given category object when exists by name then return a true")
    public void testGivenCategoryObjectWhenExistsByNameThenReturnTrue(){

        //arrang
        String name = "Test";
        this.categoryRepository.save(Category.builder().name("Test").build());
        //act
        var result = this.categoryRepository.existsByName(name);

        //assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("test given category object when exists by name then return a false")
    public void testGivenCategoryObjectWhenExistsByNameThenReturnFalse(){

        //arrang
        String name = "Test";
        //act
        var result = this.categoryRepository.existsByName(name);

        //assert
        assertThat(result).isFalse();
    }
}
