package br.unb.tppe.curadoria;

public class NomeNaoEquivalenteException extends IllegalArgumentException {

    public NomeNaoEquivalenteException(String mensagem) {
        super(mensagem);
    }
}
