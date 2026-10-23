package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.*;

import java.time.LocalDate;
import java.util.List;

public abstract class Empregado {
    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private MembroSindicato filiacao;
    private MetodoPagamento metodoPagamento = new EmMaos();
    protected String dataUltimoPagamento = "31/12/2004";
    private double descontosPendentes;

    public Empregado(String nome, String endereco, String tipo, double salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
    }

    public int getHorasNormaisPagas(String dataAtual) { return 0; }
    public int getHorasExtrasPagas(String dataAtual) { return 0; }
    public double getVendasPagas(String dataAtual) { return 0.0; }
    public double getSalarioFixoPago(String dataAtual) { return 0.0; }

    public abstract boolean verificaDiaPagamento(String dataAtual);
    protected abstract double calcularSalarioBruto(String dataAtual);
    public abstract Empregado copiar();

    protected Empregado copiarDados(Empregado copia){
        copia.setId(id);
        copia.setNome(nome);
        copia.setEndereco(endereco);
        copia.setTipo(tipo);
        copia.setSalario(salario);
        copia.setMetodoPagamento(metodoPagamento.copiar());
        copia.atualizarProximoPagamento(dataUltimoPagamento, descontosPendentes);
        if(filiacao != null){
            copia.setFiliacao(filiacao.copiar());
        }
        return copia;
    }

    protected boolean ehEmpregadoAssalariado(){
        return false;
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

    protected LocalDate converteParaLocalDate(String data){
        String[] partes = data.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);

        return LocalDate.of(ano, mes, dia);
    }

    protected boolean ehSexta(String data){
        LocalDate dataLD = converteParaLocalDate(data);
        return dataLD.getDayOfWeek().getValue() == 5;
    }

    protected long diasEntre(String dataInicial, String dataFinal){
        LocalDate inicio = converteParaLocalDate(dataInicial);
        LocalDate fim = converteParaLocalDate(dataFinal);
        return fim.toEpochDay() - inicio.toEpochDay();
    }

    protected boolean ehUltimoDiaUtil(String data){
        LocalDate dataLD = converteParaLocalDate(data);
        int ultimoDiaUtil = dataLD.lengthOfMonth();
        LocalDate dataUltimoDia = LocalDate.of(dataLD.getYear(), dataLD.getMonthValue(), ultimoDiaUtil);
        int diaSemana = dataUltimoDia.getDayOfWeek().getValue();

        if(diaSemana == 6){
            dataUltimoDia = dataUltimoDia.minusDays(1);
        }
        else if(diaSemana == 7){
            dataUltimoDia = dataUltimoDia.minusDays(2);
        }
        return dataLD.equals(dataUltimoDia);
    }

    public String getPagamentoAnterior(String dataAtual) {
        return this.dataUltimoPagamento;
    }

    public void atualizarProximoPagamento(String dataAtual, double descontosPendentes){
        this.dataUltimoPagamento = dataAtual;
        this.descontosPendentes = descontosPendentes;
    }

    public String getDataUltimoPagamento(){
        return dataUltimoPagamento;
    }

    public double getDescontosPendentes(){
        return descontosPendentes;
    }

    public double getDescontosFolha(String dataAtual) {
        if(!isSindicalizado()) return 0.0;

        String pagamentoAnterior = getPagamentoAnterior(dataAtual);
        LocalDate inicio = converteParaLocalDate(pagamentoAnterior);
        LocalDate fim = converteParaLocalDate(dataAtual);

        long dias = fim.toEpochDay() - inicio.toEpochDay();

        if(ehEmpregadoAssalariado() && dias < 28){
            dias = 28;
        }

        double descontos = descontosPendentes + filiacao.getTaxaSindical() * dias;

        for(TaxaServico taxa : filiacao.getTaxaServicos()){
            LocalDate dataAux = converteParaLocalDate(taxa.getData());
            if(dataAux.isAfter(inicio) && !dataAux.isAfter(fim)){
                descontos += taxa.getValorTaxa();
            }
        }
        return descontos;
    }
}