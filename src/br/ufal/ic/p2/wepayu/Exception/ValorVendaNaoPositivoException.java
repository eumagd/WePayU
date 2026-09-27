package br.ufal.ic.p2.wepayu.Exception;

public class ValorVendaNaoPositivoException extends Exception {
    public ValorVendaNaoPositivoException() {
        super("Valor deve ser positivo.");
    }
}
