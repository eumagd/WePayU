package br.ufal.ic.p2.wepayu.Exception;

public class DataInicialPosDataFinalException extends Exception {
    public DataInicialPosDataFinalException() {
        super("Data inicial nao pode ser posterior aa data final.");
    }
}
