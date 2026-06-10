package br.unb.tppe.curadoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("caso4")
class DeduplicadorIniciaisAgrupadasTest {

    private final DeduplicadorIniciaisAgrupadas deduplicador = new DeduplicadorIniciaisAgrupadas();

    @ParameterizedTest(name = "[{index}] {0} equivale a {1}")
    @CsvSource({
        "'Vanilda Cristina Junior', 'VC Junior'",
        "'Carlos Eduardo Santos', 'CE Santos'",
        "'João Pedro Souza', 'JP Souza'"
    })
    void deveReconhecerIniciaisAgrupadas(String completo, String abreviado) {
        assertTrue(deduplicador.saoEquivalentes(completo, abreviado));
        assertTrue(deduplicador.saoEquivalentes(abreviado, completo));
    }

    @ParameterizedTest(name = "[{index}] {0} NAO equivale a {1}")
    @CsvSource({
        "'Vanilda Cristina Junior', 'VR Junior'",
        "'Carlos Eduardo Santos', 'CE Silva'"
    })
    void deveDistinguirNomesDiferentes(String nomeA, String nomeB) {
        assertFalse(deduplicador.saoEquivalentes(nomeA, nomeB));
    }

    @ParameterizedTest(name = "[{index}] unifica para {2}")
    @CsvSource({
        "'VC Junior', 'Vanilda Cristina Junior', 'Vanilda Cristina Junior'",
        "'Carlos Eduardo Santos', 'CE Santos', 'Carlos Eduardo Santos'"
    })
    void deveUnificarParaVersaoCompleta(String nomeA, String nomeB, String esperado) {
        assertEquals(esperado, deduplicador.unificar(nomeA, nomeB));
    }

    @ParameterizedTest(name = "[{index}] nao equivalentes lancam excecao: {0} x {1}")
    @CsvSource({
        "'Vanilda Cristina Junior', 'VR Junior'"
    })
    void deveLancarExcecaoAoUnificarNomesNaoEquivalentes(String nomeA, String nomeB) {
        assertThrows(NomeNaoEquivalenteException.class, () -> deduplicador.unificar(nomeA, nomeB));
    }
}