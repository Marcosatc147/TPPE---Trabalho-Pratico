package br.unb.tppe.curadoria;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class NormalizadorNome {

    private static final Pattern ESPACOS = Pattern.compile("\\s+");
    private static final Pattern MARCAS_DIACRITICAS = Pattern.compile("\\p{M}+");
    private static final Pattern APOSTROFOS_EQUIVALENTES = Pattern.compile("['’‘`´ʼ]");

    public String normalizarParaComparacao(String nome) {
        validar(nome);

        String nomeSemEspacosExtras = ESPACOS.matcher(nome.strip()).replaceAll(" ");
        String nomeMinusculo = nomeSemEspacosExtras.toLowerCase(Locale.ROOT);
        String nomeSemCedilha = nomeMinusculo.replace('ç', 'c');
        String nomeDecomposto = Normalizer.normalize(nomeSemCedilha, Normalizer.Form.NFD);
        String nomeSemAcentos = MARCAS_DIACRITICAS.matcher(nomeDecomposto).replaceAll("");

        return APOSTROFOS_EQUIVALENTES.matcher(nomeSemAcentos).replaceAll("'");
    }

    private void validar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new NomeInvalidoException("O nome deve ser informado.");
        }
    }
}
