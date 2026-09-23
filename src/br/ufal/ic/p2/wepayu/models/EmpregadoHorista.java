package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class EmpregadoHorista extends Empregado{
    private double salarioPorHora;

    public EmpregadoHorista(String nome, String endereco, String tipo, double salario, double salarioPorHora) throws EmpregadoNaoExisteException {
        super(nome, endereco, tipo, salario);
        this.salarioPorHora = salarioPorHora;
    }

    public void setSalarioPorhora(double salarioPorHora){
        this.salarioPorHora = salarioPorHora;
    }

    public double getSalarioPorHora(){
        return salarioPorHora;
    }
}


