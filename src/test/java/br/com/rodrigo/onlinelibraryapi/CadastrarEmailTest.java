package br.com.rodrigo.onlinelibraryapi;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CadastrarEmailTest {
    

    @Test
    public void deveCadastrarEmail(){
        List<String> list = new ArrayList<>();
        Mockito.when(list.size()).thenReturn(20);

        var result = list.size();


        Assertions.assertThat(result).isEqualTo(0);
    }
}
