package br.ufal.ic.p2.wepayu.exception.empregados;

public class NomeNuloException extends Exception{
    public NomeNuloException(){
        super("Nome nao pode ser nulo.");
    }
}
