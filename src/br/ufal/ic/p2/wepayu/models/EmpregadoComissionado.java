package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpregadoComissionado extends Empregado{
    private double comissao;
    private List<ResultadoVenda> resultadoVendaList = new ArrayList<>();

    public EmpregadoComissionado(String nome, String endereco, String tipo, double salario, double comissao){
        super(nome, endereco, tipo, salario);
        this.comissao = comissao;
    }

    @Override
    public Empregado copiar(){
        EmpregadoComissionado copia = new EmpregadoComissionado(
                getNome(), getEndereco(), getTipo(), getSalario(), comissao);
        for(ResultadoVenda venda : resultadoVendaList){
            copia.adicionarResultadoVenda(new ResultadoVenda(venda.getData(), venda.getValorVenda()));
        }
        return copiarDados(copia);
    }

    @Override
    public void setComissao(double comissao){
        this.comissao = comissao;
    }

    @Override
    public double getComissao(){
        return this.comissao;
    }

    public void adicionarResultadoVenda(ResultadoVenda venda){
        resultadoVendaList.add(venda);
    }

    public List<ResultadoVenda> getResultadoVendaList(){
        return resultadoVendaList;
    }

    @Override
    public boolean verificaDiaPagamento(String dataAtual){
        if (!ehSexta(dataAtual)) return false;
        long dias = diasEntre(getPagamentoAnterior(dataAtual), dataAtual);
        return dias > 0 && (dias % 14 == 0);
    }

    @Override
    protected double calcularSalarioBruto(String dataAtual){
        double bruto = getSalarioFixoPago(dataAtual) + (getVendasPagas(dataAtual) * getComissao());
        return Math.floor(bruto * 100.0) / 100.0;
    }

    @Override
    public double getVendasPagas(String dataAtual) {
        LocalDate inicio = converteParaLocalDate(getPagamentoAnterior(dataAtual));
        LocalDate fim = converteParaLocalDate(dataAtual);
        double total = 0;

        for (ResultadoVenda venda : resultadoVendaList) {
            LocalDate d = converteParaLocalDate(venda.getData());
            if (!d.isBefore(inicio) && !d.isAfter(fim)) {
                total += venda.getValorVenda();
            }
        }
        return total;
    }

    @Override
    public double getSalarioFixoPago(String dataAtual) {
        double valor = (getSalario() * 12) / 26;
        return Math.floor(valor * 100.0) / 100.0;
    }
}