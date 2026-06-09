package br.unb.tppe.curadoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("caso1")
class NormalizadorNomeTest {

    private final NormalizadorNome normalizador = new NormalizadorNome();

    @ParameterizedTest(name = "[{index}] {0} equivale a {1}")
    @CsvSource({
        "'Sergio Henrique Guaraldi', 'Sérgio Henrique Guaraldi'",
        "'Monica Hirata Sant`anna', 'Mônica Hirata Sant’anna'",
        "'Joao Goncalves', 'João Gonçalves'"
    })
    void deveReconhecerDiferencasTipograficas(String nome1, String nome2) {
        assertEquals(
                normalizador.normalizarParaComparacao(nome1),
                normalizador.normalizarParaComparacao(nome2));
    }

    @ParameterizedTest(name = "[{index}] normaliza {0}")
    @CsvSource({
        "'  Ana   Maria  ', 'ana maria'",
        "'MARIA DA SILVA', 'maria da silva'",
        "'Conceição Gonçalves', 'conceicao goncalves'",
        "'Sant’anna', 'sant''anna'",
        "'Sant‘anna', 'sant''anna'",
        "'Sant`anna', 'sant''anna'",
        "'Sant´anna', 'sant''anna'",
        "'Santʼanna', 'sant''anna'"
    })
    void deveNormalizarNomeParaFormaCanonica(String nome, String esperado) {
        assertEquals(esperado, normalizador.normalizarParaComparacao(nome));
    }

    @ParameterizedTest(name = "[{index}] rejeita entrada invalida")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void deveRejeitarNomeInvalido(String nome) {
        assertThrows(
                NomeInvalidoException.class,
                () -> normalizador.normalizarParaComparacao(nome));
    }
}
