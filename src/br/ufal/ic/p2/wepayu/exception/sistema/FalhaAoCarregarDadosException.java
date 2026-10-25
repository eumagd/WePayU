package br.ufal.ic.p2.wepayu.exception.sistema;

public class FalhaAoCarregarDadosException extends RuntimeException {
    public FalhaAoCarregarDadosException(Throwable causa){
        super("Falha ao carregar os dados do sistema.", causa);
    }

    public FalhaAoCarregarDadosException(String mensagem){
        super("Falha ao carregar os dados do sistema: " + mensagem);
    }
}
