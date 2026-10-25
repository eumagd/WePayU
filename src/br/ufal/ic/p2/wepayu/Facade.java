package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.agenda.*;
import br.ufal.ic.p2.wepayu.exception.empregados.*;
import br.ufal.ic.p2.wepayu.exception.folha.*;
import br.ufal.ic.p2.wepayu.exception.pagamento.*;
import br.ufal.ic.p2.wepayu.exception.sindicato.*;
import br.ufal.ic.p2.wepayu.exception.sistema.*;
import br.ufal.ic.p2.wepayu.sistema.*;
import java.io.IOException;
import java.util.*;

public class Facade{
    private static SistemaFolha sistema = PersistenciaSistema.carregar();
    private final Stack<SistemaFolha> estadosDesfazer = new Stack<>();
    private final Stack<SistemaFolha> estadosRefazer = new Stack<>();
    private boolean sistemaEncerrado;

    private SistemaFolha guardarEstado(){
        return sistema.copiar();
    }

    private void confirmarComando(SistemaFolha estadoAnterior){
        try{
            PersistenciaSistema.salvar(sistema);
        }
        catch(FalhaAoSalvarDadosException e){
            sistema = estadoAnterior;
            throw e;
        }
        estadosDesfazer.push(estadoAnterior);
        estadosRefazer.clear();
    }

    public void zerarSistema(){
        SistemaFolha estadoAnterior = guardarEstado();
        sistema = new SistemaFolha();
        confirmarComando(estadoAnterior);
        sistemaEncerrado = false;
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeNuloException, EnderecoNuloException, SalarioNuloException,
            SalarioNaoNegativoException, SalarioNaoNumericoException, TipoNaoAplicavelException, TipoInvalidoException{
        SistemaFolha estadoAnterior = guardarEstado();
        String id = sistema.adicionarEmpregado(nome, endereco, tipo, salario);
        confirmarComando(estadoAnterior);
        return id;
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws NomeNuloException, EnderecoNuloException, SalarioNuloException, SalarioNaoNegativoException,
            SalarioNaoNumericoException, ComissaoNulaException, ComissaoNaoNumericaException,
            ComissaoNaoNegativaException, TipoNaoAplicavelException{
        SistemaFolha estadoAnterior = guardarEstado();
        String id = sistema.adicionarEmpregado(nome, endereco, tipo, salario, comissao);
        confirmarComando(estadoAnterior);
        return id;
    }

    public String getAtributoEmpregado(String emp, String atributo)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, EmpregadoNaoComissionadoException,
            EmpregadoNaoSindicalizadoException, EmpregadoNaoRecebeEmBancoException, AtributoNaoExisteException{
        return sistema.getAtributoEmpregado(emp, atributo);
    }

    public String getEmpregadoPorNome(String nome, int indice)
            throws NomeNuloException, NomeEmpregadoNaoExisteException{
        return sistema.getEmpregadoPorNome(nome, indice);
    }

    public int getNumeroDeEmpregados(){
        return sistema.getNumeroDeEmpregados();
    }

    public void criarAgendaDePagamentos(String descricao)
            throws AgendaDescricaoInvalidaException, AgendaJaExisteException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.criarAgendaDePagamentos(descricao);
        confirmarComando(estadoAnterior);
    }

    public void alteraEmpregado(String id, String atributo, String valor)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, NomeNuloException,
            EnderecoNuloException, SalarioNuloException, SalarioNaoNegativoException, SalarioNaoNumericoException,
            ComissaoNulaException, ComissaoNaoNegativaException, ComissaoNaoNumericaException,
            EmpregadoNaoComissionadoException, MetodoPagamentoInvalidoException, ValorBooleanoInvalidoException,
            TipoInvalidoException, AtributoNaoExisteException, AgendaNaoDisponivelException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.alteraEmpregado(id, atributo, valor);
        confirmarComando(estadoAnterior);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String comissao)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, SalarioNuloException,
            SalarioNaoNegativoException, SalarioNaoNumericoException, ComissaoNulaException,
            ComissaoNaoNegativaException, ComissaoNaoNumericaException, EmpregadoNaoComissionadoException,
            TipoInvalidoException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.alteraEmpregado(id, atributo, valor, comissao);
        confirmarComando(estadoAnterior);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException, TaxaSindicalNaoNegativaException,
            MesmaIdentificacaoException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.alteraEmpregado(id, atributo, valor, idSindicato, taxaSindical);
        confirmarComando(estadoAnterior);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String banco, String agencia,
                                String contaCorrente)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, BancoNuloException,
            AgenciaNulaException, ContaCorrenteNulaException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.alteraEmpregado(id, atributo, valor, banco, agencia, contaCorrente);
        confirmarComando(estadoAnterior);
    }

    public void removerEmpregado(String id) throws IdentificacaoNulaException, EmpregadoNaoExisteException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.removerEmpregado(id);
        confirmarComando(estadoAnterior);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        return sistema.getHorasNormaisTrabalhadas(id, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        return sistema.getHorasExtrasTrabalhadas(id, dataInicial, dataFinal);
    }

    public void lancaCartao(String id, String data, String horas)
            throws IdentificacaoNulaException, DataInvalidaException, HoraNaoPositivaException,
            EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.lancaCartao(id, data, horas);
        confirmarComando(estadoAnterior);
    }

    public void lancaVenda(String id, String data, String valorVenda)
            throws IdentificacaoNulaException, DataInvalidaException, ValorNaoPositivoException,
            EmpregadoNaoExisteException, EmpregadoNaoComissionadoException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.lancaVenda(id, data, valorVenda);
        confirmarComando(estadoAnterior);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoComissionadoException{
        return sistema.getVendasRealizadas(id, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String idMembro, String data, String taxaServico)
            throws IdentificacaoMembroNulaException, DataInvalidaException, ValorNaoPositivoException,
            MembroSindicatoNaoExisteException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.lancaTaxaServico(idMembro, data, taxaServico);
        confirmarComando(estadoAnterior);
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoSindicalizadoException{
        return sistema.getTaxasServico(id, dataInicial, dataFinal);
    }

    public String totalFolha(String data) throws DataInvalidaException{
        return sistema.totalFolha(data);
    }

    public void rodaFolha(String data, String saida) throws DataInvalidaException, IOException{
        SistemaFolha estadoAnterior = guardarEstado();
        sistema.rodaFolha(data, saida);
        confirmarComando(estadoAnterior);
    }

    public void undo() throws NaoComandoDesfazerException, ComandoAposEncerrarException{
        if(sistemaEncerrado){
            throw new ComandoAposEncerrarException();
        }
        if(estadosDesfazer.isEmpty()){
            throw new NaoComandoDesfazerException();
        }
        SistemaFolha estadoAtual = sistema;
        sistema = estadosDesfazer.peek();
        try{
            PersistenciaSistema.salvar(sistema);
        }
        catch(FalhaAoSalvarDadosException e){
            sistema = estadoAtual;
            throw e;
        }
        estadosRefazer.push(estadoAtual);
        estadosDesfazer.pop();
    }

    public void redo() throws NaoComandoRefazerException{
        if(estadosRefazer.isEmpty()){
            throw new NaoComandoRefazerException();
        }
        SistemaFolha estadoAtual = sistema;
        sistema = estadosRefazer.peek();
        try{
            PersistenciaSistema.salvar(sistema);
        }
        catch(FalhaAoSalvarDadosException e){
            sistema = estadoAtual;
            throw e;
        }
        estadosDesfazer.push(estadoAtual);
        estadosRefazer.pop();
    }

    public void encerrarSistema(){
        PersistenciaSistema.salvar(sistema);
        sistemaEncerrado = true;
    }
}
