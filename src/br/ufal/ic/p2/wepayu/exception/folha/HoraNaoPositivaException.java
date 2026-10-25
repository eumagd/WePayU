package br.ufal.ic.p2.wepayu.exception.folha;

public class HoraNaoPositivaException extends Exception {
    public HoraNaoPositivaException() {
        super("Horas devem ser positivas.");
    }
}
