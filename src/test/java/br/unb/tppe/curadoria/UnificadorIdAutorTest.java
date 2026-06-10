package br.unb.tppe.curadoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("caso5")
class UnificadorIdAutorTest {

    private final UnificadorIdAutor unificador = new UnificadorIdAutor();

    @Test
    void deveUnificarParaMenorIdENomeMaisCompleto() {
        List<Autor> registros = List.of(
            new Autor(150, "Raphael G. Viana"),
            new Autor(102, "Raphael Goncalves Viana"),
            new Autor(200, "Viana R. G.")
        );

        Autor resultado = unificador.unificarRegistros(registros);

        assertEquals(102, resultado.id());
        assertEquals("Raphael Goncalves Viana", resultado.nome());
    }

    @Test
    void deveLancarExcecaoSeHouverNomesDiferentesNaLista() {
        List<Autor> registros = List.of(
            new Autor(10, "Raphael Goncalves Viana"),
            new Autor(11, "Carlos Eduardo Santos")
        );

        assertThrows(NomeNaoEquivalenteException.class, () -> unificador.unificarRegistros(registros));
    }
    
    @Test
    void deveLancarExcecaoParaListaVazia() {
        assertThrows(IllegalArgumentException.class, () -> unificador.unificarRegistros(List.of()));
    }
}