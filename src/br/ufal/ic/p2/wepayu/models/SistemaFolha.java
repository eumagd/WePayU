/*
    CORREÇÕES: a classe foi erroneamente estruturda com atributos de Empregado,
    sendo que seu propósito é gerenciamento do sistema.
    Exceções inicialmente tratadas de forma genérica.
*/
package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.*;

import java.util.ArrayList;

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

    private void validarDados(String nome, String endereco, String salarioStr) throws NomeNuloException, EnderecoNuloException, SalarioNuloException{
        if(nome == null || nome.isEmpty()){
            throw new NomeNuloException();
        }
        if(endereco == null || endereco.isEmpty()){
            throw new EnderecoNuloException();
        }
        if(salarioStr == null || salarioStr.isEmpty()){
            throw new SalarioNuloException();
        }
    }

    public double converterSalario(String salarioStr) throws SalarioNaoNegativoException, SalarioNaoNumericoException{
        try{
            double salario = Double.parseDouble(salarioStr.replace(",","."));

            if(salario < 0) throw new SalarioNaoNegativoException();
            return salario;
        }
        catch(NumberFormatException e){
            throw new SalarioNaoNumericoException();
        }
    }

    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception{
        validarDados(nome, endereco, salarioStr);
        double salario = converterSalario(salarioStr);

        Empregado novoEmpregado;
        if(tipo.equals("horista")){
            novoEmpregado = new EmpregadoHorista(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("assalariado")){
            novoEmpregado = new EmpregadoAssalariado(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("comissionado")){
            throw new TipoNaoAplicavelException();
        }
        else{
            throw new TipoInvalidoException();
        }

        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);
        return novoId;
    }

    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception{
        validarDados(nome, endereco, salarioStr);
        double salario = converterSalario(salarioStr);

        if (comissaoStr == null || comissaoStr.isEmpty()) throw new ComissaoNulaException();
        if(tipo.equals("horista") || tipo.equals("assalariado")){
            throw new TipoNaoAplicavelException();
        }

        float comissao;
        try {
            comissao = Float.parseFloat(comissaoStr.replace(",","."));
        }
        catch(NumberFormatException e){
            throw new ComissaoNaoNumericaException();
        }
        if (comissao < 0) throw new ComissaoNaoNegativaException();

        Empregado novoEmpregado = new EmpregadoComissionado(nome, endereco, tipo, salario, comissao);

        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);

        return novoId;
    }

    public String getAtributoEmpregado(String id, String atributo) throws Exception {
        if(id == null || id.isEmpty()){
            throw new IdentificacaoNulaException();
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
                return String.format("%.2f", emp.getSalario()).replace(".",",");
            case "comissao":
                return String.format("%.2f", emp.getComissao()).replace(".",",");
            case "sindicalizado":
                return "false";
            default:
                throw new AtributoNaoExisteException();
        }
    }
}