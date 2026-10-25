package br.ufal.ic.p2.wepayu.models.util;

public class Formatador {
    public static String formatarHoras(double horas){
        if(horas == Math.floor(horas)){
            return String.format("%d", (int) horas);
        }
        return String.format("%.1f", horas).replace(".", ",");
    }

    public static String formatarValor(double valor){
        return String.format("%.2f", valor).replace(".", ",");
    }
}
