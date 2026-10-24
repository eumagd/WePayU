package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.folha.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.folha.ValorNaoPositivoException;

public class ResultadoVenda {
    private String data;
    private double valorVenda;

    public ResultadoVenda(String data, double valorVenda){
        this.data = data;
        this.valorVenda = valorVenda;
    }

    public static ResultadoVenda criar(String data, String valorVenda)
            throws DataInvalidaException, ValorNaoPositivoException{
        DataPagamento.validar(data);
        return new ResultadoVenda(data, ValorMonetario.converterPositivo(valorVenda));
    }

    public String getData(){
        return data;
    }

    public double getValorVenda(){
        return valorVenda;
    }
}
