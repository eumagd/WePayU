package br.ufal.ic.p2.wepayu.models.empregados;

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
        return obterAgendaPagamento().devePagar(dataAtual);
    }

    @Override
    protected double calcularSalarioBruto(String dataAtual){
        return obterAgendaPagamento().calcularSalarioSemanalizado(getSalario());
    }
}