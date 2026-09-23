package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaFolha {
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private int id;

    public SistemaFolha(String nome, String endereco, String tipo, String salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
    }

    public void adicionarEmpregado(String nome, String endereco, String tipo, double salario){
        Empregado emp = new Empregado(nome, endereco, tipo, salario);
    }
}
