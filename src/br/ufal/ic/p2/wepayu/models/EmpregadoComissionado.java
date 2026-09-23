package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class EmpregadoComissionado extends Empregado{
    private double salarioMensal;
    private float taxaDeComissao;

    public EmpregadoComissionado(String nome, String endereco, String tipo, double salario, double salarioMensal, float taxaDeComissao) throws EmpregadoNaoExisteException {
        super(nome, endereco, tipo, salario);
        this.salarioMensal = salarioMensal;
        this.taxaDeComissao = taxaDeComissao;
    }

    public void setSalarioMensal(double salarioMensal){
        this.salarioMensal = salarioMensal;
    }

    public double getSalarioMensal(){
        return salarioMensal;
    }

    public void setTaxaDeComissao(float taxaDeComissao){
        this.taxaDeComissao = taxaDeComissao;
    }

    public float getTaxaDeComissao(){
        return taxaDeComissao;
    }
}
