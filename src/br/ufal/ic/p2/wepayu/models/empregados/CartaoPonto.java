package br.ufal.ic.p2.wepayu.models.empregados;

import br.ufal.ic.p2.wepayu.models.util.DataPagamento;

import br.ufal.ic.p2.wepayu.exception.folha.*;

public class CartaoPonto {
    private String data;
    private double horas;

    public CartaoPonto(String data, double horas){
        this.data = data;
        this.horas = horas;
    }

    public static CartaoPonto criar(String data, String horas)
            throws DataInvalidaException, HoraNaoPositivaException{
        DataPagamento.validar(data);
        if(horas == null || horas.isEmpty()){
            throw new HoraNaoPositivaException();
        }

        try{
            double valor = Double.parseDouble(horas.replace(",", "."));
            if(valor <= 0){
                throw new HoraNaoPositivaException();
            }
            return new CartaoPonto(data, valor);
        }
        catch(NumberFormatException e){
            throw new HoraNaoPositivaException();
        }
    }

    public String getData(){
        return data;
    }

    public double getHoras(){
        return horas;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setHoras(double horas){
        this.horas = horas;
    }
}