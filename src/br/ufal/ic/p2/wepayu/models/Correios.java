package br.ufal.ic.p2.wepayu.models;

public class Correios extends MetodoPagamento{

    @Override
    public String getTipoPagamento(){
        return "correios";
    }
}
