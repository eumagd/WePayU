package br.ufal.ic.p2.wepayu.exception.agenda;

public class AgendaNaoDisponivelException extends Exception{
    public AgendaNaoDisponivelException(){
        super("Agenda de pagamento nao esta disponivel");
    }
}
