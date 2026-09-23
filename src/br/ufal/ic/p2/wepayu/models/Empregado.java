package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
<<<<<<< HEAD
    private double salario;

    public Empregado(String nome, String endereco, String tipo, double salario) throws EmpregadoNaoExisteException {
=======
    private int salario;

    public Empregado(String nome, String endereco, String tipo, int salario) throws EmpregadoNaoExisteException {
>>>>>>> 2e3c64a39bbd7fd46be321b76937fd9ecf985faf
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo;
    }

<<<<<<< HEAD
    public double getSalario() {
        return salario;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public void setSalario(Double salario){
        this.salario = salario;
    }
=======
    public int getSalario() {
        return salario;
    }

>>>>>>> 2e3c64a39bbd7fd46be321b76937fd9ecf985faf
}
