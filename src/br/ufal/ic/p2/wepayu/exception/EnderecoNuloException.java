package br.ufal.ic.p2.wepayu.exception;

public class EnderecoNuloException extends Exception{
    public EnderecoNuloException() {
        super("Endereco nao pode ser nulo.");
    }
}
