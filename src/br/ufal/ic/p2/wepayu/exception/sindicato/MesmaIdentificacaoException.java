package br.ufal.ic.p2.wepayu.exception.sindicato;

public class MesmaIdentificacaoException extends Exception {
    public MesmaIdentificacaoException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
