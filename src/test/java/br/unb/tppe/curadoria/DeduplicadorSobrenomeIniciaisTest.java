package br.unb.tppe.curadoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("caso2")
class DeduplicadorSobrenomeIniciaisTest {

    private final DeduplicadorSobrenomeIniciais deduplicador = new DeduplicadorSobrenomeIniciais();

    @ParameterizedTest(name = "[{index}] {0} equivale a {1}")
    @CsvSource({
        "'Ana de Mattos Seabra', 'Seabra A M'",
        "'Ana de Mattos Seabra', 'Seabra A. M.'",
        "'Cassius de Souza', 'Souza C.'",
        "'Cassius de Souza', 'Souza C'",
        "'Veronica de Oliveira Moreira', 'Moreira V O'"
    })
    void deveReconhecerVersaoAbreviadaComoEquivalente(String completo, String abreviado) {
        assertTrue(deduplicador.saoEquivalentes(completo, abreviado));
        assertTrue(deduplicador.saoEquivalentes(abreviado, completo));
    }

    @ParameterizedTest(name = "[{index}] {0} NAO equivale a {1}")
    @CsvSource({
        "'Ana de Mattos Seabra', 'Souza C.'",
        "'Cassius de Souza', 'Seabra A M'",
        "'Ana de Mattos Seabra', 'Seabra C M'",
        "'Cassius de Souza', 'Souza A.'"
    })
    void deveDistinguirNomesDeAutoresDiferentes(String nomeA, String nomeB) {
        assertFalse(deduplicador.saoEquivalentes(nomeA, nomeB));
    }

    @ParameterizedTest(name = "[{index}] unifica para {2}")
    @CsvSource({
        "'Seabra A M', 'Ana de Mattos Seabra', 'Ana de Mattos Seabra'",
        "'Ana de Mattos Seabra', 'Seabra A. M.', 'Ana de Mattos Seabra'",
        "'Souza C.', 'Cassius de Souza', 'Cassius de Souza'",
        "'Cassius de Souza', 'Souza C', 'Cassius de Souza'"
    })
    void deveUnificarParaVersaoCompleta(String nomeA, String nomeB, String esperado) {
        assertEquals(esperado, deduplicador.unificar(nomeA, nomeB));
    }

    @ParameterizedTest(name = "[{index}] nao equivalentes lancam excecao: {0} x {1}")
    @CsvSource({
        "'Ana de Mattos Seabra', 'Souza C.'",
        "'Cassius de Souza', 'Seabra A M'"
    })
    void deveLancarExcecaoAoUnificarNomesNaoEquivalentes(String nomeA, String nomeB) {
        assertThrows(
                NomeNaoEquivalenteException.class,
                () -> deduplicador.unificar(nomeA, nomeB));
    }

    @ParameterizedTest(name = "[{index}] rejeita entrada invalida")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void deveRejeitarNomeInvalido(String nome) {
        assertThrows(
                NomeInvalidoException.class,
                () -> deduplicador.saoEquivalentes(nome, "Souza C."));
    }

    @ParameterizedTest(name = "[{index}] {0} equivale a {1}")
    @CsvSource({
        "'Luiz de Oliveira de Souza', 'Luiz Oliveira Souza'",
        "'Luiz de Oliveira de Souza', 'Luiz de O. de Souza'",
        "'Luiz Oliveira Souza', 'Luiz de O. de Souza'"
    })
    @Tag("caso3")
    void deveReconhecerEquivalenciaComParticulasOpcionaisCaso3(String nomeA, String nomeB) {
        assertTrue(deduplicador.saoEquivalentes(nomeA, nomeB));
        assertTrue(deduplicador.saoEquivalentes(nomeB, nomeA));
    }

    @ParameterizedTest(name = "[{index}] unifica para {2}")
    @CsvSource({
        "'Luiz Oliveira Souza', 'Luiz de Oliveira de Souza', 'Luiz de Oliveira de Souza'",
        "'Luiz de O. de Souza', 'Luiz de Oliveira de Souza', 'Luiz de Oliveira de Souza'",
        "'Luiz Oliveira Souza', 'Luiz de O. de Souza', 'Luiz de O. de Souza'"
    })
    @Tag("caso3")
    void deveUnificarParaVersaoCompletaComParticulasCaso3(String nomeA, String nomeB, String esperado) {
        assertEquals(esperado, deduplicador.unificar(nomeA, nomeB));
    }
}
