package br.ufal.ic.p2.wepayu.exception.sistema;

public class FalhaAoSalvarDadosException extends RuntimeException {
    public FalhaAoSalvarDadosException(Throwable causa){
        super("Falha ao salvar os dados do sistema.", causa);
    }
}
