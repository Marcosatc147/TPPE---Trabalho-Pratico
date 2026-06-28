package br.unb.tppe.curadoria;

public class UnificadorDeNomes {
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
