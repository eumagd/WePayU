package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {
    private double salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, String tipo, double salario){
        super(nome, endereco, tipo, salario);
    }
}