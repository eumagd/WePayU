package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.SistemaFolha;

public class Facade{
    private static SistemaFolha sistema = new SistemaFolha();

    public void zerarSistema(){
        this.sistema = new SistemaFolha();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        return sistema.adicionarEmpregado(nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception{
        return sistema.adicionarEmpregado(nome, endereco, tipo, salario, comissao);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception{
        return sistema.getAtributoEmpregado(emp, atributo);
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        return sistema.getEmpregadoPorNome(nome, indice);
    }

    public void alteraEmpregado(String id, String atributo, String valor) throws Exception{
        sistema.alteraEmpregado(id, atributo, valor);
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception{
        sistema.alteraEmpregado(id, atributo,valor, idSindicato, taxaSindical);
    }

    public void removerEmpregado(String id) throws Exception{
        sistema.removerEmpregado(id);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getHorasNormaisTrabalhadas(id, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getHorasExtrasTrabalhadas(id, dataInicial, dataFinal);
    }

    public void lancaCartao(String id, String data, String horas) throws Exception{
        sistema.lancaCartao(id, data, horas);
    }

    public void lancaVenda(String id, String data, String valorVenda) throws Exception{
        sistema.lancaVenda(id, data, valorVenda);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getVendasRealizadas(id, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String idMembro, String data, String taxaServico) throws Exception{
        sistema.lancaTaxaServico(idMembro, data, taxaServico);
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal) throws Exception{
        return sistema.getTaxasServico(id, dataInicial, dataFinal);
    }

    public void encerrarSistema(){}
}