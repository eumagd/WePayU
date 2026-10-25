package br.ufal.ic.p2.wepayu.exception.sistema;

public class NaoComandoRefazerException extends Exception{
    public NaoComandoRefazerException(){
        super("Nao ha comando a refazer.");
    }
}
