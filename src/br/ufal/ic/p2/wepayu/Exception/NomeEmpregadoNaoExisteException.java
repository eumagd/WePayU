package br.ufal.ic.p2.wepayu.Exception;

public class NomeEmpregadoNaoExisteException extends Exception {
    public NomeEmpregadoNaoExisteException() {
        super("Nao ha empregado com esse nome.");
    }
}
