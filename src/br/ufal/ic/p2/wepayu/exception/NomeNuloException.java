package br.ufal.ic.p2.wepayu.exception;

public class NomeNuloException extends Exception{
    public NomeNuloException(){
        super("Nome nao pode ser nulo.");
    }
}
