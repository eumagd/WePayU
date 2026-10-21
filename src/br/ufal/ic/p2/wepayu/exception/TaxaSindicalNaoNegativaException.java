package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalNaoNegativaException extends RuntimeException {
    public TaxaSindicalNaoNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
