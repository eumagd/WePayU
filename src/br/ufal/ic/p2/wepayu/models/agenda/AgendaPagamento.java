package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.exception.agenda.*;
import br.ufal.ic.p2.wepayu.exception.empregados.TipoEmpregadoInvalidoException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AgendaPagamento {
    private final String formula;
    private final String tipo;
    private final int intervaloSemanas;
    private final int dia;
    private final boolean fimDoMes;
    private final boolean diaUtil;

    private AgendaPagamento(String formula, String tipo, int intervaloSemanas, int dia, boolean fimDoMes, boolean diaUtil){
        this.formula = formula;
        this.tipo = tipo;
        this.intervaloSemanas = intervaloSemanas;
        this.dia = dia;
        this.fimDoMes = fimDoMes;
        this.diaUtil = diaUtil;
    }

    public static AgendaPagamento padraoPara(String tipo) throws TipoEmpregadoInvalidoException{
        if("horista".equals(tipo)){
            return semanal(1, 5, "semanal 5");
        }
        if("assalariado".equals(tipo)){
            return mensal(0, true);
        }
        if("comissionado".equals(tipo)){
            return semanal(2, 5, "semanal 2 5");
        }
        throw new TipoEmpregadoInvalidoException();
    }

    public static List<AgendaPagamento> agendasPadrao(){
        List<AgendaPagamento> agendas = new ArrayList<>();
        agendas.add(mensal(0, true));
        agendas.add(semanal(1, 5, "semanal 5"));
        agendas.add(semanal(2, 5, "semanal 2 5"));
        return agendas;
    }

    public static AgendaPagamento selecionar(String formula) throws AgendaNaoDisponivelException{
        if("semanal 5".equals(formula)){
            return semanal(1, 5, "semanal 5");
        }
        if("semanal 2 5".equals(formula)){
            return semanal(2, 5, "semanal 2 5");
        }
        if("mensal $".equals(formula)){
            return mensal(0, true);
        }
        throw new AgendaNaoDisponivelException();
    }

    public static AgendaPagamento selecionar(String formula, List<AgendaPagamento> agendas)
            throws AgendaNaoDisponivelException{
        String formulaNormalizada = normalizarFormula(formula);
        for(AgendaPagamento agenda : agendas){
            if(agenda.getFormula().equals(formulaNormalizada)){
                return agenda.copiar();
            }
        }
        throw new AgendaNaoDisponivelException();
    }

    public static AgendaPagamento criar(String descricao) throws AgendaDescricaoInvalidaException{
        if(descricao == null){
            throw new AgendaDescricaoInvalidaException();
        }
        String texto = normalizarFormula(descricao);
        String[] partes = texto.split("\\s+");
        if(partes.length == 2 && "mensal".equals(partes[0])){
            if("$".equals(partes[1])){
                return mensal(0, true);
            }
            int diaMes = converterInteiro(partes[1]);
            if(diaMes >= 1 && diaMes <= 28){
                return mensal(diaMes, false);
            }
            throw new AgendaDescricaoInvalidaException();
        }
        if("semanal".equals(partes[0]) && (partes.length == 2 || partes.length == 3)){
            int intervalo = 1;
            String diaTexto;
            if(partes.length == 2){
                diaTexto = partes[1];
            }
            else{
                intervalo = converterInteiro(partes[1]);
                diaTexto = partes[2];
            }
            int diaSemana = converterDiaSemana(diaTexto);
            if(intervalo < 1 || intervalo > 52 || diaSemana < 1){
                throw new AgendaDescricaoInvalidaException();
            }
            return semanal(intervalo, diaSemana, String.join(" ", partes));
        }
        throw new AgendaDescricaoInvalidaException();
    }

    private static int converterInteiro(String valor) throws AgendaDescricaoInvalidaException{
        try{
            return Integer.parseInt(valor);
        }
        catch(NumberFormatException e){
            throw new AgendaDescricaoInvalidaException();
        }
    }

    private static int converterDiaSemana(String dia) throws AgendaDescricaoInvalidaException{
        try{
            int valor = Integer.parseInt(dia);
            if(valor >= 1 && valor <= 7){
                return valor;
            }
        }
        catch(NumberFormatException e){
            if("segunda".equals(dia) || "segunda-feira".equals(dia)){
                return 1;
            }
            if("terca".equals(dia) || "terça".equals(dia) || "terca-feira".equals(dia)
                    || "terça-feira".equals(dia)){
                return 2;
            }
            if("quarta".equals(dia) || "quarta-feira".equals(dia)){
                return 3;
            }
            if("quinta".equals(dia) || "quinta-feira".equals(dia)){
                return 4;
            }
            if("sexta".equals(dia) || "sexta-feira".equals(dia)){
                return 5;
            }
            if("sabado".equals(dia) || "sábado".equals(dia)){
                return 6;
            }
            if("domingo".equals(dia)){
                return 7;
            }
        }
        throw new AgendaDescricaoInvalidaException();
    }

    private static AgendaPagamento semanal(int intervalo, int dia, String formula){
        return new AgendaPagamento(formula, "semanal", intervalo, dia, false, false);
    }

    private static AgendaPagamento mensal(int dia, boolean fimDoMes){
        String formula = fimDoMes ? "mensal $" : "mensal " + dia;
        return new AgendaPagamento(formula, "mensal", 0, dia, fimDoMes, fimDoMes);
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
            return fimDoMes && ehUltimoDiaUtilDoMes(diaAtual)
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
        return new AgendaPagamento(formula, tipo, intervaloSemanas, dia, fimDoMes, diaUtil);
    }

    public boolean mesmaAgenda(AgendaPagamento outra){
        return formula.equals(outra.formula);
    }

    private static String normalizarFormula(String formula){
        if(formula == null){
            return "";
        }
        return formula.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private boolean ehUltimoDiaUtilDoMes(LocalDate data){
        if(!diaUtil){
            return data.getDayOfMonth() == data.lengthOfMonth();
        }
        LocalDate ultimoDia = data.withDayOfMonth(data.lengthOfMonth());
        while(ultimoDia.getDayOfWeek().getValue() > 5){
            ultimoDia = ultimoDia.minusDays(1);
        }
        return data.equals(ultimoDia);
    }

    private LocalDate converterData(String data){
        String[] partes = data.split("/");
        return LocalDate.of(Integer.parseInt(partes[2]),
                Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
    }
}
