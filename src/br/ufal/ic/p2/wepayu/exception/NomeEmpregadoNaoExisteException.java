package br.ufal.ic.p2.wepayu.exception;

public class NomeEmpregadoNaoExisteException extends Exception {
    public NomeEmpregadoNaoExisteException() {
        super("Nao ha empregado com esse nome.");
    }
}
