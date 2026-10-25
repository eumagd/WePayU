package br.ufal.ic.p2.wepayu.models.util;

import br.ufal.ic.p2.wepayu.exception.folha.*;

public class DataPagamento {
    public static void validar(String data) throws DataInvalidaException{
        if(!formatoValido(data)){
            throw new DataInvalidaException();
        }
    }

    public static void validarInicial(String data) throws DataInicialInvalidaException{
        if(!formatoValido(data)){
            throw new DataInicialInvalidaException();
        }
    }

    public static void validarFinal(String data) throws DataFinalInvalidaException{
        if(!formatoValido(data)){
            throw new DataFinalInvalidaException();
        }
    }

    public static void validarPeriodo(String inicial, String fin) throws DataInicialInvalidaException,
            DataFinalInvalidaException, DataInicialPosDataFinalException{
        validarInicial(inicial);
        validarFinal(fin);
        if(converter(inicial) > converter(fin)){
            throw new DataInicialPosDataFinalException();
        }
    }

    public static int converter(String data){
        String[] partes = data.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        return ano * 10000 + mes * 100 + dia;
    }

    public static String formatarParaRelatorio(String data){
        String[] partes = data.split("/");
        return String.format("%04d-%02d-%02d", Integer.parseInt(partes[2]),
                Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
    }

    private static boolean formatoValido(String data){
        if(data == null || data.isEmpty()){
            return false;
        }

        try{
            String[] partes = data.split("/");
            if(partes.length != 3){
                return false;
            }

            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            Integer.parseInt(partes[2]);

            if(mes < 1 || mes > 12 || dia < 1 || dia > 31){
                return false;
            }
            return (mes != 4 && mes != 6 && mes != 9 && mes != 11 || dia <= 30)
                    && (mes != 2 || dia <= 29);
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}
