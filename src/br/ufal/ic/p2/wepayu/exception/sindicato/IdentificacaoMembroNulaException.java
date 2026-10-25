package br.ufal.ic.p2.wepayu.exception.sindicato;

public class IdentificacaoMembroNulaException extends Exception {
    public IdentificacaoMembroNulaException() {
        super("Identificacao do membro nao pode ser nula.");
    }
}
