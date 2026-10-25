package br.ufal.ic.p2.wepayu.exception.sindicato;

public class TaxaSindicalNaoNegativaException extends RuntimeException {
    public TaxaSindicalNaoNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
