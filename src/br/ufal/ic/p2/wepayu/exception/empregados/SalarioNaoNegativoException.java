package br.ufal.ic.p2.wepayu.exception.empregados;

public class SalarioNaoNegativoException extends Exception{
    public SalarioNaoNegativoException() {
        super("Salario deve ser nao-negativo.");
    }
}
