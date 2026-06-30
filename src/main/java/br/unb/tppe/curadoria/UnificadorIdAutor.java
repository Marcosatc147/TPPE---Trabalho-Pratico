package br.unb.tppe.curadoria;

import java.util.Comparator;
import java.util.List;

public class UnificadorIdAutor {

    private final DeduplicadorSobrenomeIniciais deduplicadorNome = new DeduplicadorSobrenomeIniciais();
    private final NormalizadorNome normalizador = new NormalizadorNome();

    public Autor unificarRegistros(List<Autor> registros) {
        validarRegistros(registros);

        Autor autorMenorId = encontrarAutorDeMenorId(registros);
        String nomeUnificado = autorMenorId.nome();

        for (Autor registro : registros) {
            nomeUnificado = consolidarNome(nomeUnificado, registro);
        }

        return new Autor(autorMenorId.id(), nomeUnificado);
    }

    private void validarRegistros(List<Autor> registros) {
        if (registros == null || registros.isEmpty()) {
            throw new IllegalArgumentException("A lista de registros nao pode ser vazia.");
        }
    }

    private Autor encontrarAutorDeMenorId(List<Autor> registros) {
        return registros.stream()
                .min(Comparator.comparingInt(Autor::id))
                .orElseThrow();
    }

    private String consolidarNome(String nomeUnificado, Autor registro) {
        if (possuemMesmaFormaNormalizada(nomeUnificado, registro.nome())) {
            return nomeUnificado;
        }

        if (deduplicadorNome.saoEquivalentes(nomeUnificado, registro.nome())) {
            return deduplicadorNome.unificar(nomeUnificado, registro.nome());
        }

        throw new NomeNaoEquivalenteException("Registros contem autores distintos: "
                + nomeUnificado + " e " + registro.nome());
    }

    private boolean possuemMesmaFormaNormalizada(String nomeA, String nomeB) {
        return normalizador.normalizarParaComparacao(nomeA)
                .equals(normalizador.normalizarParaComparacao(nomeB));
    }
}