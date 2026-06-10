package br.unb.tppe.curadoria;

import java.util.Comparator;
import java.util.List;

public class UnificadorIdAutor {

    private final DeduplicadorSobrenomeIniciais deduplicadorNome = new DeduplicadorSobrenomeIniciais();
    private final NormalizadorNome normalizador = new NormalizadorNome();

    public Autor unificarRegistros(List<Autor> registros) {
        if (registros == null || registros.isEmpty()) {
            throw new IllegalArgumentException("A lista de registros nao pode ser vazia.");
        }

        // Encontra o registro com o menor ID
        Autor autorMenorId = registros.stream()
                .min(Comparator.comparingInt(Autor::id))
                .orElseThrow();

        String nomeUnificado = autorMenorId.nome();

        for (Autor registro : registros) {
            String nomeNormUnificado = normalizador.normalizarParaComparacao(nomeUnificado);
            String nomeNormRegistro = normalizador.normalizarParaComparacao(registro.nome());
            
            if (nomeNormUnificado.equals(nomeNormRegistro)) {
                continue; 
            }

            // Se não forem idênticos, delega para a lógica de sobrenome/iniciais (Casos 2 e 3)
            if (deduplicadorNome.saoEquivalentes(nomeUnificado, registro.nome())) {
                nomeUnificado = deduplicadorNome.unificar(nomeUnificado, registro.nome());
            } else {
                throw new NomeNaoEquivalenteException("Registros contem autores distintos: " 
                        + nomeUnificado + " e " + registro.nome());
            }
        }

        return new Autor(autorMenorId.id(), nomeUnificado);
    }
}