package br.ufal.ic.p2.wepayu.exception.sindicato;

public class MembroSindicatoNaoExisteException extends Exception {
    public MembroSindicatoNaoExisteException() {
        super("Membro nao existe.");
    }
}
