package br.unb.tppe.curadoria;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Classe extraida de {@link NormalizadorNome} (Extract Class).
 *
 * Responsabilidade unica: remover as marcas diacriticas de um texto, ou seja,
 * a cedilha e os acentos, preservando as letras base. A normalizacao geral
 * (espacos, caixa, apostrofos) permanece em NormalizadorNome, que delega a
 * remocao de diacriticos para esta classe.
 */
class RemovedorDeDiacriticos {

    private static final Pattern MARCAS_DIACRITICAS = Pattern.compile("\\p{M}+");

    String remover(String texto) {
        String semCedilha = texto.replace('ç', 'c').replace('Ç', 'C');
        String decomposto = Normalizer.normalize(semCedilha, Normalizer.Form.NFD);
        return MARCAS_DIACRITICAS.matcher(decomposto).replaceAll("");
    }
}
