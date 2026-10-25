package br.ufal.ic.p2.wepayu.exception.empregados;

public class EmpregadoNaoComissionadoException extends Exception{
    public EmpregadoNaoComissionadoException() {
        super("Empregado nao eh comissionado.");
    }
}
