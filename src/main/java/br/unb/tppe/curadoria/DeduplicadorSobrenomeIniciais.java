package br.unb.tppe.curadoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Caso 2: Sobrenome + Iniciais dos nomes.
 *
 * Reconhece a equivalencia entre a versao completa de um nome e sua versao
 * abreviada (sobrenome seguido das iniciais dos nomes, com ou sem pontos) e
 * unifica os registros para a versao completa.
 */
public class DeduplicadorSobrenomeIniciais {

    private static final Set<String> PARTICULAS = Set.of("de", "da", "do", "das", "dos", "e");

    private final NormalizadorNome normalizador = new NormalizadorNome();

    public boolean saoEquivalentes(String nomeA, String nomeB) {
        NomeEstruturado a = estruturar(nomeA);
        NomeEstruturado b = estruturar(nomeB);
        return a.sobrenome().equals(b.sobrenome()) && a.iniciais().equals(b.iniciais());
    }

    public String unificar(String nomeA, String nomeB) {
        if (!saoEquivalentes(nomeA, nomeB)) {
            throw new NomeNaoEquivalenteException(
                    "Os nomes nao representam o mesmo autor: \"" + nomeA + "\" e \"" + nomeB + "\".");
        }
        
        boolean aTemParticulas = temParticulas(nomeA);
        boolean bTemParticulas = temParticulas(nomeB);
        
        if (aTemParticulas && !bTemParticulas) {
            return nomeA.strip();
        }
        if (bTemParticulas && !aTemParticulas) {
            return nomeB.strip();
        }
        
        boolean aAbreviado = ehAbreviado(nomeA);
        boolean bAbreviado = ehAbreviado(nomeB);
        
        if (aAbreviado && !bAbreviado) {
            return nomeB.strip();
        }
        if (bAbreviado && !aAbreviado) {
            return nomeA.strip();
        }
        
        return nomeA.strip().length() >= nomeB.strip().length() ? nomeA.strip() : nomeB.strip();
    }
    
    private boolean temParticulas(String nome) {
        String normalizado = normalizador.normalizarParaComparacao(nome).replace(".", "");
        for (String token : normalizado.split(" ")) {
            if (PARTICULAS.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private NomeEstruturado estruturar(String nome) {
        List<String> tokens = tokenizar(nome);
        if (ehAbreviadoTokens(tokens)) {
            return estruturarAbreviado(tokens);
        }
        return estruturarCompleto(tokens);
    }

    private NomeEstruturado estruturarAbreviado(List<String> tokens) {
        List<String> nomesCompletos = new ArrayList<>();
        for (String token : tokens) {
            if (!PARTICULAS.contains(token) && token.length() > 1) {
                nomesCompletos.add(token);
            }
        }
        
        String sobrenome = nomesCompletos.isEmpty() ? tokens.get(tokens.size() - 1) : nomesCompletos.get(nomesCompletos.size() - 1);
        
        List<String> iniciais = new ArrayList<>();
        for (String token : tokens) {
            if (!PARTICULAS.contains(token) && !token.equals(sobrenome)) {
                iniciais.add(token.substring(0, 1));
            }
        }
        
        return new NomeEstruturado(sobrenome, iniciais);
    }

    private NomeEstruturado estruturarCompleto(List<String> tokens) {
        String sobrenome = tokens.get(tokens.size() - 1);
        List<String> iniciais = new ArrayList<>();
        for (int i = 0; i < tokens.size() - 1; i++) {
            String token = tokens.get(i);
            if (!PARTICULAS.contains(token)) {
                iniciais.add(token.substring(0, 1));
            }
        }
        return new NomeEstruturado(sobrenome, iniciais);
    }

    private List<String> tokenizar(String nome) {
        String normalizado = normalizador.normalizarParaComparacao(nome).replace(".", "");
        List<String> tokens = new ArrayList<>();
        for (String token : normalizado.split(" ")) {
            if (!token.isBlank()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private boolean ehAbreviado(String nome) {
        return ehAbreviadoTokens(tokenizar(nome));
    }

    private boolean ehAbreviadoTokens(List<String> tokens) {
        return tokens.stream().anyMatch(token -> token.length() == 1);
    }

    private record NomeEstruturado(String sobrenome, List<String> iniciais) {
    }
}
