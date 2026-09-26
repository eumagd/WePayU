package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.SistemaFolha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade{
    private SistemaFolha sistema = new SistemaFolha();

    public void zerarSistema(){
        this.sistema = new SistemaFolha();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception{
        return sistema.adicionarEmpregado(nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception{
        return sistema.adicionarEmpregado(nome, endereco, tipo, salario, comissao);
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception{
        return sistema.getAtributoEmpregado(emp, atributo);
    }

    public void encerrarSistema(){}
}