package br.ufal.ic.p2.wepayu.models.sindicato;

import br.ufal.ic.p2.wepayu.models.util.DataPagamento;
import br.ufal.ic.p2.wepayu.models.util.ValorMonetario;
import br.ufal.ic.p2.wepayu.exception.folha.*;

public class TaxaServico {
    private String data;
    private double valorTaxa;

    public TaxaServico(String data, double valorTaxa){
        this.data = data;
        this.valorTaxa = valorTaxa;
    }

    public static TaxaServico criar(String data, String valorTaxa)
            throws DataInvalidaException, ValorNaoPositivoException{
        DataPagamento.validar(data);
        return new TaxaServico(data, ValorMonetario.converterPositivo(valorTaxa));
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(double valorTaxa) {
        this.valorTaxa = valorTaxa;
    }
}
