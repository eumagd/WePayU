package br.ufal.ic.p2.wepayu.exception.sistema;

public class ComandoAposEncerrarException extends Exception{
    public ComandoAposEncerrarException(){
        super("Nao pode dar comandos depois de encerrarSistema.");
    }
}
