package br.ufal.ic.p2.wepayu.models;

public class ResultadoVenda {
    private String data;
    private double valorVenda;

    public ResultadoVenda(String data, double valorVenda){
        this.data = data;
        this.valorVenda = valorVenda;
    }

    public String getData(){
        return data;
    }

    public double getValorVenda(){
        return valorVenda;
    }
}
