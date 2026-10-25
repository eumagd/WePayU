package br.ufal.ic.p2.wepayu.exception.empregados;

public class ComissaoNulaException extends Exception {
    public ComissaoNulaException() {
        super("Comissao nao pode ser nula.");
    }
}
