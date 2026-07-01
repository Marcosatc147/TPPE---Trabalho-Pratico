package br.unb.tppe.curadoria;

/**
 * Objeto-método extraído de {@link DeduplicadorSobrenomeIniciais#unificar(String, String)}.
 *
 * Encapsula a execução da unificação: os parâmetros (nomeA e nomeB) são recebidos
 * no construtor e as antigas variáveis locais (aTemParticulas, bTemParticulas,
 * aAbreviado, bAbreviado) tornam-se atributos. O método {@link #computar()} resolve
 * a lógica de decisão sobre qual versão do nome deve prevalecer.
 */
class UnificadorDeNomes {

    private boolean aTemParticulas;
    private boolean bTemParticulas;
    private boolean aAbreviado;
    private boolean bAbreviado;

    private final DeduplicadorSobrenomeIniciais deduplicadorSobrenomeIniciais;
    private final String nomeA;
    private final String nomeB;
    


    public UnificadorDeNomes(DeduplicadorSobrenomeIniciais deduplicadorSobrenomeIniciais, String nomeA, String nomeB) {
        this.deduplicadorSobrenomeIniciais = deduplicadorSobrenomeIniciais;
        this.nomeA = nomeA;
        this.nomeB = nomeB;
    }


    
    public String computar() {
        if (!deduplicadorSobrenomeIniciais.saoEquivalentes(nomeA, nomeB)) {
            throw new NomeNaoEquivalenteException(
                    "Os nomes nao representam o mesmo autor: \"" + nomeA + "\" e \"" + nomeB + "\".");
        }
        
        aTemParticulas = deduplicadorSobrenomeIniciais.temParticulas(nomeA);
        bTemParticulas = deduplicadorSobrenomeIniciais.temParticulas(nomeB);
        
        if (aTemParticulas && !bTemParticulas) {
            return nomeA.strip();
        }
        if (bTemParticulas && !aTemParticulas) {
            return nomeB.strip();
        }
        
        aAbreviado = deduplicadorSobrenomeIniciais.ehAbreviado(nomeA);
        bAbreviado = deduplicadorSobrenomeIniciais.ehAbreviado(nomeB);
        
        if (aAbreviado && !bAbreviado) {
            return nomeB.strip();
        }
        if (bAbreviado && !aAbreviado) {
            return nomeA.strip();
        }

        return nomeA.strip().length() >= nomeB.strip().length() ? nomeA.strip() : nomeB.strip();
    }
}
