package br.ufal.ic.p2.wepayu.exception;

public class MembroSindicatoNaoExisteException extends Exception {
    public MembroSindicatoNaoExisteException() {
        super("Membro nao existe.");
    }
}
