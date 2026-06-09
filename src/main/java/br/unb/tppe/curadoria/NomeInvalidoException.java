package br.unb.tppe.curadoria;

public class NomeInvalidoException extends IllegalArgumentException {

    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}
