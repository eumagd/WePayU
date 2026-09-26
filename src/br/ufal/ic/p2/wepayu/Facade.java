package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.NomeEmpregadoNaoExisteException;
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

    public void removerEmpregado(String id) throws Exception{
        sistema.removerEmpregado(id);
    }

    public void encerrarSistema(){}
}