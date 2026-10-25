package br.ufal.ic.p2.wepayu.models.pagamento;

import br.ufal.ic.p2.wepayu.exception.pagamento.EmpregadoNaoRecebeEmBancoException;

public abstract class MetodoPagamento {
    public abstract String getTipoPagamento();
    public abstract MetodoPagamento copiar();

    public String getBanco() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }

    public String getAgencia() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }

    public String getContaCorrente() throws EmpregadoNaoRecebeEmBancoException{
        throw new EmpregadoNaoRecebeEmBancoException();
    }

    public abstract String getDescricao(String enderecoEmpregado);
}