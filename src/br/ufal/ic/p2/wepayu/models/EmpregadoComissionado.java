package br.ufal.ic.p2.wepayu.models;

public class EmpregadoComissionado extends Empregado{
    private float comissao;

    public EmpregadoComissionado(String nome, String endereco, String tipo, double salario, float comissao){
        super(nome, endereco, tipo, salario);
        this.comissao = comissao;
    }

    public void setComissao(float comissao){
        this.comissao = comissao;
    }

    public float getComissao(){
        return comissao;
    }
}