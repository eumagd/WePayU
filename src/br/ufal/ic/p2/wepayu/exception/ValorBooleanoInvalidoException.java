package br.ufal.ic.p2.wepayu.exception;

public class ValorBooleanoInvalidoException extends Exception {
    public ValorBooleanoInvalidoException() {
        super("Valor deve ser true ou false.");
    }
}
