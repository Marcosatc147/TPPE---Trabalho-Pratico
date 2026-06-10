package br.unb.tppe.curadoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("caso5")
class UnificadorIdAutorTest {

    private final UnificadorIdAutor unificador = new UnificadorIdAutor();

    @ParameterizedTest(name = "[{index}] menor id={1}, nome=\"{2}\"")
    @MethodSource("cenariosDeUnificacao")
    void deveUnificarParaMenorIdENomeMaisCompleto(List<Autor> registros, int idEsperado, String nomeEsperado) {
        Autor resultado = unificador.unificarRegistros(registros);

        assertEquals(idEsperado, resultado.id());
        assertEquals(nomeEsperado, resultado.nome());
    }

    static Stream<Arguments> cenariosDeUnificacao() {
        return Stream.of(
            Arguments.of(List.of(
                new Autor(150, "Raphael G. Viana"),
                new Autor(102, "Raphael Goncalves Viana"),
                new Autor(200, "Viana R. G.")
            ), 102, "Raphael Goncalves Viana"),
            Arguments.of(List.of(
                new Autor(433094, "Raphael Gonçalves Viana"),
                new Autor(31298, "Raphael Goncalves Viana"),
                new Autor(549243, "Raphael Gonçalves Viana")
            ), 31298, "Raphael Goncalves Viana"),
            Arguments.of(List.of(
                new Autor(746936, "Souza C."),
                new Autor(28371, "Cassius de Souza")
            ), 28371, "Cassius de Souza")
        );
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