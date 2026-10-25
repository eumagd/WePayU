package br.ufal.ic.p2.wepayu.exception.sistema;

public class NaoComandoDesfazerException extends Exception{
    public NaoComandoDesfazerException(){
        super("Nao ha comando a desfazer.");
    }
}
