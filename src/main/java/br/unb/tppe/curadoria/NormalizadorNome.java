package br.unb.tppe.curadoria;

import java.util.Locale;
import java.util.regex.Pattern;

public class NormalizadorNome {

    private static final Pattern ESPACOS = Pattern.compile("\\s+");
    private static final Pattern APOSTROFOS_EQUIVALENTES = Pattern.compile("['’‘`´ʼ]");

    private final RemovedorDeDiacriticos removedorDeDiacriticos = new RemovedorDeDiacriticos();

    public String normalizarParaComparacao(String nome) {
        validar(nome);

        String nomeSemEspacosExtras = ESPACOS.matcher(nome.strip()).replaceAll(" ");
        String nomeMinusculo = nomeSemEspacosExtras.toLowerCase(Locale.ROOT);
        String nomeSemDiacriticos = removedorDeDiacriticos.remover(nomeMinusculo);

        return APOSTROFOS_EQUIVALENTES.matcher(nomeSemDiacriticos).replaceAll("'");
    }

    private void validar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new NomeInvalidoException("O nome deve ser informado.");
        }
    }
}
