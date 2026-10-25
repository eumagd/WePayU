package br.ufal.ic.p2.wepayu.exception.sindicato;

public class IdentificacaoSindicatoNulaException extends Exception {
    public IdentificacaoSindicatoNulaException() {
        super("Identificacao do sindicato nao pode ser nula.");
    }
}
