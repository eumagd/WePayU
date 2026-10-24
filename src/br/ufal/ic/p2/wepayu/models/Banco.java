package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.pagamento.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.exception.pagamento.BancoNuloException;
import br.ufal.ic.p2.wepayu.exception.pagamento.ContaCorrenteNulaException;

public class Banco extends MetodoPagamento{
    private String banco;
    private String agencia;
    private String contaCorrente;

    public Banco(String banco, String agencia, String contaCorrente) {
        this.banco = banco;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
    }

    public static Banco criar(String banco, String agencia, String contaCorrente)
            throws BancoNuloException, AgenciaNulaException, ContaCorrenteNulaException{
        if(banco == null || banco.isEmpty()){
            throw new BancoNuloException();
        }
        if(agencia == null || agencia.isEmpty()){
            throw new AgenciaNulaException();
        }
        if(contaCorrente == null || contaCorrente.isEmpty()){
            throw new ContaCorrenteNulaException();
        }
        return new Banco(banco, agencia, contaCorrente);
    }

    @Override
    public MetodoPagamento copiar(){
        return new Banco(banco, agencia, contaCorrente);
    }

    @Override
    public String getTipoPagamento(){
        return "banco";
    }

    @Override
    public String getBanco(){
        return banco;
    }

    @Override
    public String getAgencia(){
        return agencia;
    }

    @Override
    public String getContaCorrente(){
        return contaCorrente;
    }

    @Override
    public String getDescricao(String endereco) {
        return banco + ", Ag. " + agencia + " CC " + contaCorrente;
    }
}
