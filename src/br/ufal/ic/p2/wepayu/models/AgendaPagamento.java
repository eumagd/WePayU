package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.agenda.AgendaNaoDisponivelException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AgendaPagamento {
    private final String formula;
    private final String tipo;
    private final int intervaloSemanas;
    private final int dia;
    private final boolean fimDoMes;

    private AgendaPagamento(String formula, String tipo, int intervaloSemanas, int dia, boolean fimDoMes){
        this.formula = formula;
        this.tipo = tipo;
        this.intervaloSemanas = intervaloSemanas;
        this.dia = dia;
        this.fimDoMes = fimDoMes;
    }

    public static AgendaPagamento padraoPara(String tipo){
        if("horista".equals(tipo)){
            return semanal(1);
        }
        if("assalariado".equals(tipo)){
            return mensal();
        }
        if("comissionado".equals(tipo)){
            return quinzenal();
        }
        throw new IllegalArgumentException("Tipo de empregado invalido.");
    }

    public static AgendaPagamento selecionar(String formula) throws AgendaNaoDisponivelException{
        if("semanal 5".equals(formula)){
            return semanal(1);
        }
        if("semanal 2 5".equals(formula)){
            return quinzenal();
        }
        if("mensal $".equals(formula)){
            return mensal();
        }
        throw new AgendaNaoDisponivelException();
    }

    private static AgendaPagamento semanal(int intervalo){
        String formula = intervalo == 1 ? "semanal 5" : "semanal " + intervalo + " 5";
        return new AgendaPagamento(formula, "semanal", intervalo, 5, false);
    }

    private static AgendaPagamento quinzenal(){
        return semanal(2);
    }

    private static AgendaPagamento mensal(){
        return new AgendaPagamento("mensal $", "mensal", 0, 0, true);
    }

    public String getFormula(){
        return formula;
    }

    public String getTipo(){
        return tipo;
    }

    public int getIntervaloSemanas(){
        return intervaloSemanas;
    }

    public String getInicioPeriodo(String data){
        LocalDate diaPagamento = converterData(data);
        LocalDate inicio;
        if("mensal".equals(tipo)){
            inicio = diaPagamento.minusMonths(1);
        }
        else{
            inicio = diaPagamento.minusDays((7L * intervaloSemanas) - 1);
        }
        return inicio.getDayOfMonth() + "/" + inicio.getMonthValue() + "/" + inicio.getYear();
    }

    public boolean devePagar(String data){
        LocalDate diaAtual = converterData(data);
        if("mensal".equals(tipo)){
            return fimDoMes && diaAtual.getDayOfMonth() == diaAtual.lengthOfMonth()
                    || !fimDoMes && diaAtual.getDayOfMonth() == dia;
        }
        if(diaAtual.getDayOfWeek().getValue() != dia){
            return false;
        }

        LocalDate inicioAgenda = LocalDate.of(2005, 1, 1);
        long numeroSemana = ChronoUnit.WEEKS.between(inicioAgenda, diaAtual) + 1;
        return numeroSemana % intervaloSemanas == 0;
    }

    public double calcularSalarioSemanalizado(double salario){
        if("mensal".equals(tipo)){
            return salario;
        }
        double valor = salario * 12 / 52 * intervaloSemanas;
        return Math.floor(valor * 100.0) / 100.0;
    }

    public AgendaPagamento copiar(){
        return new AgendaPagamento(formula, tipo, intervaloSemanas, dia, fimDoMes);
    }

    private LocalDate converterData(String data){
        String[] partes = data.split("/");
        return LocalDate.of(Integer.parseInt(partes[2]),
                Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
    }
}
