package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;
import java.util.List;

public class EmpregadoHorista extends Empregado{
    private List<CartaoPonto> cartaoPontoList = new ArrayList<>();

    public EmpregadoHorista(String nome, String endereco, String tipo, double salario){
        super(nome, endereco, tipo, salario);
    }

    public void adicionarCartaoPonto(CartaoPonto cartao){
        cartaoPontoList.add(cartao);
    }

    public List<CartaoPonto> getCartaoPontoList(){
        return cartaoPontoList;
    }
}