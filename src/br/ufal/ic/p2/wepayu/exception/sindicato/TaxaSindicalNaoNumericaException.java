package br.ufal.ic.p2.wepayu.exception.sindicato;

public class TaxaSindicalNaoNumericaException extends Exception {
    public TaxaSindicalNaoNumericaException() {
        super("Taxa sindical deve ser numerica.");
    }
}
