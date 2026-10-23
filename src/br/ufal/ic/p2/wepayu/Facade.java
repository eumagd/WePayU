package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.ComandoAposEncerrarException;
import br.ufal.ic.p2.wepayu.exception.NaoComandoDesfazerException;
import br.ufal.ic.p2.wepayu.exception.NaoComandoRefazerException;
import br.ufal.ic.p2.wepayu.exception.OperacaoHistoricoNaoExisteException;
import br.ufal.ic.p2.wepayu.models.SistemaFolha;

import java.util.Stack;

public class Facade{
    private static SistemaFolha sistema = new SistemaFolha();
    private final Stack<SistemaFolha> estadosDesfazer = new Stack<>();
    private final Stack<SistemaFolha> estadosRefazer = new Stack<>();
    private boolean sistemaEncerrado;

    private enum Operacao{
        ZERAR_SISTEMA,
        CRIAR_EMPREGADO,
        CRIAR_COMISSIONADO,
        ALTERAR_EMPREGADO,
        ALTERAR_TIPO_EMPREGADO,
        ALTERAR_SINDICALIZACAO,
        ALTERAR_PAGAMENTO_BANCO,
        REMOVER_EMPREGADO,
        LANCAR_CARTAO,
        LANCAR_VENDA,
        LANCAR_TAXA_SERVICO,
        RODAR_FOLHA
    }

    private String executarComHistorico(Operacao operacao, String... parametros) throws Exception{
        SistemaFolha estadoAnterior = sistema.copiar();
        try{
            String resultado = executarOperacao(operacao, parametros);
            estadosDesfazer.push(estadoAnterior);
            estadosRefazer.clear();
            return resultado;
        }
        catch(Exception e){
            sistema = estadoAnterior;
            throw e;
        }
    }

    private String executarOperacao(Operacao operacao, String[] parametros)
            throws Exception, OperacaoHistoricoNaoExisteException{
        switch(operacao){
            case ZERAR_SISTEMA:
                sistema = new SistemaFolha();
                sistemaEncerrado = false;
                break;
            case CRIAR_EMPREGADO:
                return sistema.adicionarEmpregado(parametros[0], parametros[1], parametros[2], parametros[3]);
            case CRIAR_COMISSIONADO:
                return sistema.adicionarEmpregado(
                        parametros[0], parametros[1], parametros[2], parametros[3], parametros[4]);
            case ALTERAR_EMPREGADO:
                sistema.alteraEmpregado(parametros[0], parametros[1], parametros[2]);
                break;
            case ALTERAR_TIPO_EMPREGADO:
                sistema.alteraEmpregado(parametros[0], parametros[1], parametros[2], parametros[3]);
                break;
            case ALTERAR_SINDICALIZACAO:
                sistema.alteraEmpregado(parametros[0], parametros[1], parametros[2], parametros[3], parametros[4]);
                break;
            case ALTERAR_PAGAMENTO_BANCO:
                sistema.alteraEmpregado(parametros[0], parametros[1], parametros[2],
                        parametros[3], parametros[4], parametros[5]);
                break;
            case REMOVER_EMPREGADO:
                sistema.removerEmpregado(parametros[0]);
                break;
            case LANCAR_CARTAO:
                sistema.lancaCartao(parametros[0], parametros[1], parametros[2]);
                break;
            case LANCAR_VENDA:
                sistema.lancaVenda(parametros[0], parametros[1], parametros[2]);
                break;
            case LANCAR_TAXA_SERVICO:
                sistema.lancaTaxaServico(parametros[0], parametros[1], parametros[2]);
                break;
            case RODAR_FOLHA:
                sistema.rodaFolha(parametros[0], parametros[1]);
                break;
            default:
                throw new OperacaoHistoricoNaoExisteException();
        }
        return null;
    }

    public void zerarSistema() throws Exception{
        executarComHistorico(Operacao.ZERAR_SISTEMA);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception{
        return executarComHistorico(Operacao.CRIAR_EMPREGADO, nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws Exception{
        return executarComHistorico(Operacao.CRIAR_COMISSIONADO, nome, endereco, tipo, salario, comissao);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception{
        return sistema.getAtributoEmpregado(emp, atributo);
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception{
        return sistema.getEmpregadoPorNome(nome, indice);
    }

    public int getNumeroDeEmpregados(){
        return sistema.getNumeroDeEmpregados();
    }

    public void alteraEmpregado(String id, String atributo, String valor) throws Exception{
        executarComHistorico(Operacao.ALTERAR_EMPREGADO, id, atributo, valor);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String comissao) throws Exception{
        executarComHistorico(Operacao.ALTERAR_TIPO_EMPREGADO, id, atributo, valor, comissao);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical)
            throws Exception{
        executarComHistorico(Operacao.ALTERAR_SINDICALIZACAO, id, atributo, valor, idSindicato, taxaSindical);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String banco, String agencia,
                                String contaCorrente) throws Exception{
        executarComHistorico(Operacao.ALTERAR_PAGAMENTO_BANCO, id, atributo, valor, banco, agencia, contaCorrente);
    }

    public void removerEmpregado(String id) throws Exception{
        executarComHistorico(Operacao.REMOVER_EMPREGADO, id);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getHorasNormaisTrabalhadas(id, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getHorasExtrasTrabalhadas(id, dataInicial, dataFinal);
    }

    public void lancaCartao(String id, String data, String horas) throws Exception{
        executarComHistorico(Operacao.LANCAR_CARTAO, id, data, horas);
    }

    public void lancaVenda(String id, String data, String valorVenda) throws Exception{
        executarComHistorico(Operacao.LANCAR_VENDA, id, data, valorVenda);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getVendasRealizadas(id, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String idMembro, String data, String taxaServico) throws Exception{
        executarComHistorico(Operacao.LANCAR_TAXA_SERVICO, idMembro, data, taxaServico);
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getTaxasServico(id, dataInicial, dataFinal);
    }

    public String totalFolha(String data) throws Exception{
        return sistema.totalFolha(data);
    }

    public void rodaFolha(String data, String saida) throws Exception{
        executarComHistorico(Operacao.RODAR_FOLHA, data, saida);
    }

    public void undo() throws NaoComandoDesfazerException, ComandoAposEncerrarException{
        if(sistemaEncerrado){
            throw new ComandoAposEncerrarException();
        }
        if(estadosDesfazer.isEmpty()){
            throw new NaoComandoDesfazerException();
        }
        estadosRefazer.push(sistema.copiar());
        sistema = estadosDesfazer.pop();
    }

    public void redo() throws NaoComandoRefazerException{
        if(estadosRefazer.isEmpty()){
            throw new NaoComandoRefazerException();
        }
        estadosDesfazer.push(sistema.copiar());
        sistema = estadosRefazer.pop();
    }

    public void encerrarSistema(){
        sistemaEncerrado = true;
    }
}
