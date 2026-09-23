package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class EmpregadoAssalariado extends Empregado {
    private double salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, String tipo, double salario, double salarioMensal) throws EmpregadoNaoExisteException {
        super(nome, endereco, tipo, salario);
        this.salarioMensal = salarioMensal;
    }

    public void setSalarioMensal(double salarioMensal){
        this.salarioMensal = salarioMensal;
    }

    public double getSalarioMensal(){
        return salarioMensal;
    }
}
