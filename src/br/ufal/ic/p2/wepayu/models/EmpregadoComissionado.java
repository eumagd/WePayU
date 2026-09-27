package br.ufal.ic.p2.wepayu.models;

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
}