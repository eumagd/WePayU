package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {
    public EmpregadoAssalariado(String nome, String endereco, String tipo, double salario){
        super(nome, endereco, tipo, salario);
    }

    @Override
    public Empregado copiar(){
        return copiarDados(new EmpregadoAssalariado(getNome(), getEndereco(), getTipo(), getSalario()));
    }

    @Override
    protected boolean ehEmpregadoAssalariado(){
        return true;
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