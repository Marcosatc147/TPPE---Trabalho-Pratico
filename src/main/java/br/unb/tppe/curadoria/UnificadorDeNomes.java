package br.unb.tppe.curadoria;

/**
 * Objeto-Metodo extraido de {@link DeduplicadorSobrenomeIniciais#unificar(String, String)}.
 *
 * Encapsula a execucao da unificacao: os parametros (nomeA e nomeB) sao recebidos
 * no construtor e as antigas variaveis locais (aTemParticulas, bTemParticulas,
 * aAbreviado, bAbreviado) tornam-se atributos. O metodo {@link #computar()} resolve
 * a logica de decisao sobre qual versao do nome deve prevalecer.
 */
class UnificadorDeNomes {

    private final DeduplicadorSobrenomeIniciais deduplicador;
    private final String nomeA;
    private final String nomeB;

    private boolean aTemParticulas;
    private boolean bTemParticulas;
    private boolean aAbreviado;
    private boolean bAbreviado;

    UnificadorDeNomes(DeduplicadorSobrenomeIniciais deduplicador, String nomeA, String nomeB) {
        this.deduplicador = deduplicador;
        this.nomeA = nomeA;
        this.nomeB = nomeB;
    }

    String computar() {
        if (!deduplicador.saoEquivalentes(nomeA, nomeB)) {
            throw new NomeNaoEquivalenteException(
                    "Os nomes nao representam o mesmo autor: \"" + nomeA + "\" e \"" + nomeB + "\".");
        }

        this.aTemParticulas = deduplicador.temParticulas(nomeA);
        this.bTemParticulas = deduplicador.temParticulas(nomeB);

        if (aTemParticulas && !bTemParticulas) {
            return nomeA.strip();
        }
        if (bTemParticulas && !aTemParticulas) {
            return nomeB.strip();
        }

        this.aAbreviado = deduplicador.ehAbreviado(nomeA);
        this.bAbreviado = deduplicador.ehAbreviado(nomeB);

        if (aAbreviado && !bAbreviado) {
            return nomeB.strip();
        }
        if (bAbreviado && !aAbreviado) {
            return nomeA.strip();
        }

        return nomeA.strip().length() >= nomeB.strip().length() ? nomeA.strip() : nomeB.strip();
    }
}
