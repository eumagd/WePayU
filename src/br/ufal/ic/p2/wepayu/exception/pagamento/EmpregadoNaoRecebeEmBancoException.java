package br.ufal.ic.p2.wepayu.exception.pagamento;

public class EmpregadoNaoRecebeEmBancoException extends Exception {
    public EmpregadoNaoRecebeEmBancoException() {
        super("Empregado nao recebe em banco.");
    }
}
