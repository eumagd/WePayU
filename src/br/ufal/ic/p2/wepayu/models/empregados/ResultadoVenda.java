package br.ufal.ic.p2.wepayu.models.empregados;

import br.ufal.ic.p2.wepayu.models.util.DataPagamento;
import br.ufal.ic.p2.wepayu.models.util.ValorMonetario;
import br.ufal.ic.p2.wepayu.exception.folha.*;

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
