package br.ufal.ic.p2.wepayu.models;

public class Banco extends MetodoPagamento{
    private String banco;
    private String agencia;
    private String contaCorrente;

    public Banco(String banco, String agencia, String contaCorrente) {
        this.banco = banco;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
    }

    @Override
    public String getTipoPagamento(){
        return "banco";
    }

    @Override
    public String getBanco(){
        return banco;
    }

    @Override
    public String getAgencia(){
        return agencia;
    }

    @Override
    public String getContaCorrente(){
        return contaCorrente;
    }
}
