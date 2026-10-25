package br.ufal.ic.p2.wepayu.exception.pagamento;

public class BancoNuloException extends Exception {
    public BancoNuloException() {
        super("Banco nao pode ser nulo.");
    }
}
