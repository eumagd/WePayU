package br.ufal.ic.p2.wepayu.models;

public class TaxaServico {
    private String data;
    private double valorTaxa;

    public TaxaServico(String data, double valorTaxa){
        this.data = data;
        this.valorTaxa = valorTaxa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(double valorTaxa) {
        this.valorTaxa = valorTaxa;
    }
}
