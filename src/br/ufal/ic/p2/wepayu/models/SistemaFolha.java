/*
    CORREÇÕES: a classe foi erroneamente estruturda com atributos de Empregado,
    sendo que seu propósito é gerenciamento do sistema.
    Exceções inicialmente tratadas de forma genérica.
*/
package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class SistemaFolha {
    private final ArrayList<Empregado> empregados = new ArrayList<>();
    private int contId = 1;

    private Empregado buscarEmpregado(String id) throws EmpregadoNaoExisteException{
        for(Empregado e : empregados) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        throw new EmpregadoNaoExisteException();
    }

    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception {
        if (nome == null || nome.isEmpty()) throw new Exception("Nome nao pode ser nulo.");
        if (endereco == null || endereco.isEmpty()) throw new Exception("Endereco nao pode ser nulo.");
        if (salarioStr == null || salarioStr.isEmpty()) throw new Exception("Salario nao pode ser nulo.");

        double salario;
        try {
            salario = Double.parseDouble(salarioStr.replace(",","."));
        } catch (NumberFormatException e) {
            throw new Exception("Salario deve ser numerico.");
        }

        if (salario < 0) throw new Exception("Salario deve ser nao-negativo.");

        Empregado novoEmpregado;
        if(tipo.equals("horista")){
            novoEmpregado = new EmpregadoHorista(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("assalariado")){
            novoEmpregado = new EmpregadoAssalariado(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("comissionado")){
            throw new Exception("Tipo nao aplicavel.");
        }
        else{
            throw new Exception("Tipo invalido.");
        }

        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);
        return novoId;
    }

    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception{
        if (nome == null || nome.isEmpty()) throw new Exception("Nome nao pode ser nulo.");
        if (endereco == null || endereco.isEmpty()) throw new Exception("Endereco nao pode ser nulo.");
        if (salarioStr == null || salarioStr.isEmpty()) throw new Exception("Salario nao pode ser nulo.");
        if (comissaoStr == null || comissaoStr.isEmpty()) throw new Exception("Comissao nao pode ser nula.");

        if(tipo.equals("horista") || tipo.equals("assalariado")){
            throw new Exception("Tipo nao aplicavel.");
        }

        double salario;
        try {
            salario = Double.parseDouble(salarioStr.replace(",","."));
        } catch (NumberFormatException e) {
            throw new Exception("Salario deve ser nao-negativo.");
        }
        if (salario < 0) throw new Exception("Salario deve ser nao-negativo.");

        float comissao;
        try {
            comissao = Float.parseFloat(comissaoStr.replace(",","."));
        } catch (NumberFormatException e) {
            throw new Exception("Comissao deve ser numerica.");
        }
        if (comissao < 0) throw new Exception("Comissao deve ser nao-negativa.");

        Empregado novoEmpregado = new EmpregadoComissionado(nome, endereco, tipo, salario, comissao);

        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);

        return novoId;
    }

    public String getAtributoEmpregado(String id, String atributo) throws Exception{
        if(id == null || id.isEmpty()){
            throw new Exception("Identificacao do empregado nao pode ser nula.");
        }

        Empregado emp = buscarEmpregado(id);
        switch(atributo){
            case "nome":
                return emp.getNome();
            case "endereco":
                return emp.getEndereco();
            case "tipo":
                return emp.getTipo();
            case "salario":
                String salarioFormatado = String.format("%.2f", emp.getSalario()).replace(".",",");
                return salarioFormatado;
            case "comissao":
                if(emp instanceof EmpregadoComissionado){
                    float comissao = ((EmpregadoComissionado) emp).getComissao();
                    return String.format("%.2f", comissao).replace(".",",");
                }
                throw new Exception("Empregado não é comissionado.");
            case "sindicalizado":
                return "false";
            default:
                throw new Exception("Atributo nao existe.");
        }
    }
}