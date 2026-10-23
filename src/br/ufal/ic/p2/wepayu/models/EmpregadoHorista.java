package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpregadoHorista extends Empregado{
    private List<CartaoPonto> cartaoPontoList = new ArrayList<>();

    public EmpregadoHorista(String nome, String endereco, String tipo, double salario){
        super(nome, endereco, tipo, salario);
    }

    @Override
    public Empregado copiar(){
        EmpregadoHorista copia = new EmpregadoHorista(getNome(), getEndereco(), getTipo(), getSalario());
        for(CartaoPonto cartao : cartaoPontoList){
            copia.adicionarCartaoPonto(new CartaoPonto(cartao.getData(), cartao.getHoras()));
        }
        return copiarDados(copia);
    }

    @Override
    public boolean verificaDiaPagamento(String dataAtual) {
        return ehSexta(dataAtual);
    }

    @Override
    public void adicionarCartaoPonto(CartaoPonto cartao){
        cartaoPontoList.add(cartao);
    }

    public List<CartaoPonto> getCartaoPontoList(){
        return cartaoPontoList;
    }

    @Override
    protected  double calcularSalarioBruto(String dataAtual){
        LocalDate inicio = converteParaLocalDate(getPagamentoAnterior(dataAtual));
        LocalDate fim = converteParaLocalDate(dataAtual);
        double totalSalario = 0;

        for (CartaoPonto cartao : cartaoPontoList) {
            LocalDate dateAux = converteParaLocalDate(cartao.getData());
            if(!dateAux.isBefore(inicio) && !dateAux.isAfter(fim)){
                double horas = cartao.getHoras();
                if(horas <= 8){
                    totalSalario += horas * getSalario();
                }
                else{
                    totalSalario += 8 * getSalario() + ((horas - 8) * (getSalario() * 1.5));
                }
            }
        }
        return totalSalario;
    }

    @Override
    public int getHorasNormaisPagas(String dataAtual){
        LocalDate inicio = converteParaLocalDate(getPagamentoAnterior(dataAtual));
        LocalDate fim = converteParaLocalDate(dataAtual);
        int normais = 0;
        for (CartaoPonto cartao : cartaoPontoList) {
            LocalDate dataAux = converteParaLocalDate(cartao.getData());
            if(!dataAux.isBefore(inicio) && !dataAux.isAfter(fim)){
                double h = cartao.getHoras();
                normais += (h <= 8) ? h : 8;
            }
        }
        return normais;
    }

    @Override
    public int getHorasExtrasPagas(String dataAtual){
        LocalDate inicio = converteParaLocalDate(getPagamentoAnterior(dataAtual));
        LocalDate fim = converteParaLocalDate(dataAtual);
        int extras = 0;
        for(CartaoPonto cartao : cartaoPontoList) {
            LocalDate d = converteParaLocalDate(cartao.getData());
            if(!d.isBefore(inicio) && !d.isAfter(fim)) {
                double h = cartao.getHoras();
                if (h > 8) extras += (h - 8);
            }
        }
        return extras;
    }
}