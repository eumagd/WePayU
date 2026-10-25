package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.empregados.*;

public class FabricaEmpregado {
    public static void validarNome(String nome) throws NomeNuloException{
        if(nome == null || nome.isEmpty()){
            throw new NomeNuloException();
        }
    }

    public static void validarEndereco(String endereco) throws EnderecoNuloException{
        if(endereco == null || endereco.isEmpty()){
            throw new EnderecoNuloException();
        }
    }

    public static void validarSalario(String salario) throws SalarioNuloException{
        if(salario == null || salario.isEmpty()){
            throw new SalarioNuloException();
        }
    }

    public static double converterSalario(String salario) throws SalarioNaoNegativoException,
            SalarioNaoNumericoException{
        try{
            double valor = Double.parseDouble(salario.replace(",", "."));
            if(valor < 0){
                throw new SalarioNaoNegativoException();
            }
            return valor;
        }
        catch(NumberFormatException e){
            throw new SalarioNaoNumericoException();
        }
    }

    public static void validarComissao(String comissao) throws ComissaoNulaException{
        if(comissao == null || comissao.isEmpty()){
            throw new ComissaoNulaException();
        }
    }

    public static double converterComissao(String comissao) throws ComissaoNaoNumericaException,
            ComissaoNaoNegativaException{
        try{
            double valor = Double.parseDouble(comissao.replace(",", "."));
            if(valor < 0){
                throw new ComissaoNaoNegativaException();
            }
            return valor;
        }
        catch(NumberFormatException e){
            throw new ComissaoNaoNumericaException();
        }
    }

    public static Empregado criar(String nome, String endereco, String tipo, double salario)
            throws TipoNaoAplicavelException, TipoInvalidoException{
        if("horista".equals(tipo)){
            return new EmpregadoHorista(nome, endereco, tipo, salario);
        }
        if("assalariado".equals(tipo)){
            return new EmpregadoAssalariado(nome, endereco, tipo, salario);
        }
        if("comissionado".equals(tipo)){
            throw new TipoNaoAplicavelException();
        }
        throw new TipoInvalidoException();
    }

    public static Empregado criarComissionado(String nome, String endereco, String tipo, double salario,
                                               double comissao) throws TipoNaoAplicavelException{
        if("horista".equals(tipo) || "assalariado".equals(tipo)){
            throw new TipoNaoAplicavelException();
        }
        return new EmpregadoComissionado(nome, endereco, tipo, salario, comissao);
    }

    public static Empregado trocarTipo(Empregado empregado, String novoTipo) throws TipoInvalidoException{
        Empregado novo;
        if("horista".equals(novoTipo)){
            novo = new EmpregadoHorista(empregado.getNome(), empregado.getEndereco(),
                    novoTipo, empregado.getSalario());
        }
        else if("assalariado".equals(novoTipo)){
            novo = new EmpregadoAssalariado(empregado.getNome(), empregado.getEndereco(),
                    novoTipo, empregado.getSalario());
        }
        else if("comissionado".equals(novoTipo)){
            novo = new EmpregadoComissionado(empregado.getNome(), empregado.getEndereco(),
                    novoTipo, empregado.getSalario(), 0);
        }
        else{
            throw new TipoInvalidoException();
        }

        novo.setId(empregado.getId());
        novo.setMetodoPagamento(empregado.getMetodoPagamento());
        novo.setFiliacao(empregado.getFiliacao());
        novo.copiarAgendaPagamentoDe(empregado);
        return novo;
    }

    public static void definirComissao(Empregado empregado, double comissao)
            throws EmpregadoNaoComissionadoException{
        if(!"comissionado".equals(empregado.getTipo())){
            throw new EmpregadoNaoComissionadoException();
        }
        ((EmpregadoComissionado) empregado).setComissao(comissao);
    }
}
