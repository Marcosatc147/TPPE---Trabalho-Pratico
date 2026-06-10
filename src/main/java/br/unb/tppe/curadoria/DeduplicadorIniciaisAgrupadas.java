package br.unb.tppe.curadoria;

import java.util.Arrays;
import java.util.List;

public class DeduplicadorIniciaisAgrupadas {

    private final NormalizadorNome normalizador = new NormalizadorNome();

    public boolean saoEquivalentes(String nomeA, String nomeB) {
        String normA = normalizador.normalizarParaComparacao(nomeA);
        String normB = normalizador.normalizarParaComparacao(nomeB);
        return verificarEquivalenciaIniciaisAgrupadas(normA, normB) || 
               verificarEquivalenciaIniciaisAgrupadas(normB, normA);
    }

    public String unificar(String nomeA, String nomeB) {
        if (!saoEquivalentes(nomeA, nomeB)) {
            throw new NomeNaoEquivalenteException("Os nomes nao representam o mesmo autor.");
        }
        return nomeA.length() >= nomeB.length() ? nomeA.strip() : nomeB.strip();
    }

    private boolean verificarEquivalenciaIniciaisAgrupadas(String nomeCompleto, String nomeAbreviado) {
        List<String> tokensCompleto = Arrays.asList(nomeCompleto.split(" "));
        List<String> tokensAbreviado = Arrays.asList(nomeAbreviado.split(" "));

        if (tokensCompleto.size() < 2 || tokensAbreviado.size() != 2) return false;

        String sobrenomeCompleto = tokensCompleto.get(tokensCompleto.size() - 1);
        String sobrenomeAbreviado = tokensAbreviado.get(1);

        if (!sobrenomeCompleto.equals(sobrenomeAbreviado)) return false;

        String iniciaisAgrupadas = tokensAbreviado.get(0);
        if (iniciaisAgrupadas.length() != tokensCompleto.size() - 1) return false;

        for (int i = 0; i < iniciaisAgrupadas.length(); i++) {
            if (iniciaisAgrupadas.charAt(i) != tokensCompleto.get(i).charAt(0)) {
                return false;
            }
        }
        return true;
    }
}