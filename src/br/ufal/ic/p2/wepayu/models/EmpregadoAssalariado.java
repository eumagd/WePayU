package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {
    public EmpregadoAssalariado(String nome, String endereco, String tipo, double salario){
        super(nome, endereco, tipo, salario);
    }

    @Override
    public boolean verificaDiaPagamento(String dataAtual) {
        return ehUltimoDiaUtil(dataAtual);
    }

    @Override
    protected double calcularSalarioBruto(String dataAtual){
        return getSalario();
    }
}