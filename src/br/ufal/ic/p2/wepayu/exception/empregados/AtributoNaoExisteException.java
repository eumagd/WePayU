package br.ufal.ic.p2.wepayu.exception.empregados;

public class AtributoNaoExisteException extends Exception {
    public AtributoNaoExisteException() {
        super("Atributo nao existe.");
    }
}
