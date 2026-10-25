package br.ufal.ic.p2.wepayu.exception.sindicato;

public class TaxaSindicalNulaException extends Exception {
    public TaxaSindicalNulaException() {
        super("Taxa sindical nao pode ser nula.");
    }
}
