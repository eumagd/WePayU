package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.*;

import java.util.List;

public class Empregado {
    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private MembroSindicato filiacao;
    private MetodoPagamento metodoPagamento = new EmMaos();

    public Empregado(String nome, String endereco, String tipo, double salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
    }

    public String getId(){
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public void setSalario(double salario){
        this.salario = salario;
    }

    public double getComissao() throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException();
    }

    public void setComissao(double comissao) throws Exception{
        throw new EmpregadoNaoComissionadoException();
    }

    public void adicionarCartaoPonto(CartaoPonto cartao) throws EmpregadoNaoHoristaException{
        throw new EmpregadoNaoHoristaException();
    }

    public List<CartaoPonto> getCartaoPontoList() throws EmpregadoNaoHoristaException{
        throw new EmpregadoNaoHoristaException();
    }

    public void adicionarResultadoVenda(ResultadoVenda venda) throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException();
    }

    public List<ResultadoVenda> getResultadoVendaList() throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException();
    }

    public boolean isSindicalizado(){
        return this.filiacao != null;
    }

    public MembroSindicato getFiliacao(){
        return filiacao;
    }

    public void setFiliacao(MembroSindicato filiacao){
        this.filiacao = filiacao;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}