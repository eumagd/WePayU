package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.folha.ValorNaoPositivoException;

public class ValorMonetario {
    public static double converterPositivo(String valor) throws ValorNaoPositivoException{
        if(valor == null || valor.isEmpty()){
            throw new ValorNaoPositivoException();
        }
        try{
            double convertido = Double.parseDouble(valor.replace(",", "."));
            if(convertido <= 0){
                throw new ValorNaoPositivoException();
            }
            return convertido;
        }
        catch(NumberFormatException e){
            throw new ValorNaoPositivoException();
        }
    }
}
