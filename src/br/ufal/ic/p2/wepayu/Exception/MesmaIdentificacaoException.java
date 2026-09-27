package br.ufal.ic.p2.wepayu.Exception;

public class MesmaIdentificacaoException extends Exception {
    public MesmaIdentificacaoException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
