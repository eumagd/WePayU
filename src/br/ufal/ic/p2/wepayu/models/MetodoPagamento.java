package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException;

public abstract class MetodoPagamento {
    public abstract String getTipoPagamento();

    public String getBanco() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }

    public String getAgencia() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }

    public String getContaCorrente() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }
}