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

    private void validarId(String id) throws IdentificacaoNulaException{
        if(id == null || id.isEmpty()){
            throw new IdentificacaoNulaException();
        }
    }

    private void validarNome(String nome) throws NomeNuloException{
        if(nome == null || nome.isEmpty()){
            throw new NomeNuloException();
        }
    }

    private void validarEndereco(String endereco) throws EnderecoNuloException{
        if(endereco == null || endereco.isEmpty()){
            throw new EnderecoNuloException();
        }
    }

    private void validarSalario(String salarioStr) throws SalarioNuloException{
        if(salarioStr == null || salarioStr.isEmpty()){
            throw new SalarioNuloException();
        }
    }

    private void validarData(String data) throws DataInvalidaException{
        if(data == null || data.isEmpty()){
            throw new DataInvalidaException();
        }

        if(!validarFormatoData(data)){
            throw new DataInvalidaException();
        }
    }

    private void validarDataInicial(String dataInicial) throws DataInicialInvalidaException{
        if(dataInicial == null || dataInicial.isEmpty()){
            throw new DataInicialInvalidaException();
        }

        if(!validarFormatoData(dataInicial)){
            throw new DataInicialInvalidaException();
        }
    }

    private void validarDataFinal(String dataFinal) throws DataFinalInvalidaException{
        if(dataFinal == null || dataFinal.isEmpty()){
            throw new DataFinalInvalidaException();
        }

        if(!validarFormatoData(dataFinal)){
            throw new DataFinalInvalidaException();
        }
    }

    private boolean validarFormatoData(String data){
        try{
            String[] partes = data.split("/");
            if (partes.length != 3) {
                return false;
            }

            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            if (mes < 1 || mes > 12) return false;

            if (dia < 1 || dia > 31) return false;

            if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) return false;

            if (mes == 2 && dia > 29) return false;

            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    private void validarHoras(String horas) throws HoraNaoPositivaException{
        if (horas == null || horas.isEmpty()) {
            throw new HoraNaoPositivaException();
        }

        double horasDouble;
        try{
            horasDouble = Double.parseDouble(horas.replace(",", "."));
        }
        catch(NumberFormatException e) {
            throw new HoraNaoPositivaException();
        }

        if(horasDouble <= 0){
            throw new HoraNaoPositivaException();
        }
    }

    private double converterHoras(String horas){
        return Double.parseDouble(horas.replace(",","."));
    }

    private String formatarHoras(double horas){
        if(horas == Math.floor(horas)){
            return String.format("%d", (int) horas);
        }
        else{
            return String.format("%.1f", horas).replace(".", ",");
        }
    }

    private void validarValorVenda(String valorVenda) throws ValorNaoPositivoException {
        double valor = Double.parseDouble(valorVenda.replace(",","."));

        if(valor <= 0){
            throw new ValorNaoPositivoException();
        }
    }

    private void validarIdMembro(String idMembro) throws IdentificacaoMembroNulaException{
        if(idMembro == null || idMembro.isEmpty()){
            throw new IdentificacaoMembroNulaException();
        }
    }

    private double converterValorVenda(String valorVenda){
        return Double.parseDouble(valorVenda.replace(",","."));
    }

    public int converterDataInt(String data){
        String[] partesData = data.split("/");
        int dia = Integer.parseInt(partesData[0]);
        int mes = Integer.parseInt(partesData[1]);
        int ano = Integer.parseInt(partesData[2]);

        return ano * 10000 + mes * 100 + dia;
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
        validarNome(nome);
        validarEndereco(endereco);
        validarSalario(salarioStr);

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
        validarNome(nome);
        validarEndereco(endereco);
        validarSalario(salarioStr);

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
        validarId(id);

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
                return String.valueOf(emp.isSindicalizado());
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception{
        validarNome(nome);

        int aux = 0;
        for (int i = 0, empregadosSize = empregados.size(); i < empregadosSize; i++) {
            Empregado e = empregados.get(i);
            if (e.getNome().contains(nome)) {
                aux++;
                if (aux == indice) {
                    return e.getId();
                }
            }
        }

        throw new NomeEmpregadoNaoExisteException();
    }

    public void alteraEmpregado(String id, String atributo, String valor) throws Exception{
        validarId(id);

        Empregado emp = buscarEmpregado(id);
        if (atributo.equals("sindicalizado") && valor.equals("false")){
            emp.setFiliacao(null);
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception{
        validarId(id);

        Empregado emp = buscarEmpregado(id);
        if (atributo.equals("sindicalizado") && valor.equals("true")){
            for (Empregado e : empregados) {
                if (e.isSindicalizado() && e.getFiliacao().getIdMembro().equals(idSindicato)) {
                    throw new MesmaIdentificacaoException();
                }
            }
            double taxa = Double.parseDouble(taxaSindical.replace(",", "."));
            MembroSindicato novaFiliacao = new MembroSindicato(idSindicato, taxa);
            emp.setFiliacao(novaFiliacao);
        }
    }

    private Empregado getEmpregadoPorSindicato(String idSindicato) throws Exception{
        for (Empregado e : empregados) {
            if (e.isSindicalizado() && e.getFiliacao().getIdMembro().equals(idSindicato)){
                return e;
            }
        }
        throw new MembroSindicatoNaoExisteException();
    }

    public void removerEmpregado(String id) throws Exception{
        validarId(id);
        Empregado e = buscarEmpregado(id);
        empregados.remove(e);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        Empregado emp = buscarEmpregado(id);
        if(!emp.getTipo().equals("horista")){
            throw new EmpregadoNaoHoristaException();
        }

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt){
            throw new DataInicialPosDataFinalException();
        }

        EmpregadoHorista horista = (EmpregadoHorista) emp;
        double horasNormais = 0;
        for(CartaoPonto cartao : horista.getCartaoPontoList()){
            int dataCartao = converterDataInt(cartao.getData());
            if(dataCartao >= diInt && dataCartao < dfInt){
                double horasDia = cartao.getHoras();
                if(horasDia <= 8){
                    horasNormais += horasDia;
                }
                else{
                    horasNormais += 8;
                }
            }
        }

        return formatarHoras(horasNormais);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        Empregado emp = buscarEmpregado(id);
        if(!emp.getTipo().equals("horista")){
            throw new EmpregadoNaoHoristaException();
        }

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt){
            throw new DataInicialPosDataFinalException();
        }

        EmpregadoHorista horista = (EmpregadoHorista) emp;
        double horasExtras = 0;
        for(CartaoPonto cartao : horista.getCartaoPontoList()){
            int dataCartao = converterDataInt(cartao.getData());

            if(dataCartao >= diInt && dataCartao < dfInt){
                double horasDia = cartao.getHoras();
                if(horasDia > 8){
                    horasExtras += (horasDia - 8);
                }
            }
        }

        return formatarHoras(horasExtras);
    }

    public void lancaCartao(String id, String data, String horas) throws Exception{
        validarId(id);
        validarData(data);
        validarHoras(horas);

        Empregado emp = buscarEmpregado(id);
        if(!emp.getTipo().equals("horista")){
            throw new EmpregadoNaoHoristaException();
        }

        double horasDouble = converterHoras(horas);
        CartaoPonto cartao = new CartaoPonto(data, horasDouble);
        EmpregadoHorista horista = (EmpregadoHorista) emp;
        horista.adicionarCartaoPonto(cartao);
    }

    public void lancaVenda(String id, String data, String valorVenda) throws Exception{
        validarId(id);
        validarData(data);
        validarValorVenda(valorVenda);

        Empregado emp = buscarEmpregado(id);
        if(!emp.getTipo().equals("comissionado")){
            throw new EmpregadoNaoComissionadoException();
        }

        double novoValor = converterValorVenda(valorVenda);
        ResultadoVenda novaVenda = new ResultadoVenda(data, novoValor);
        EmpregadoComissionado comissionado = (EmpregadoComissionado) emp;
        comissionado.adicionarResultadoVenda(novaVenda);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        Empregado emp = buscarEmpregado(id);
        if(!emp.getTipo().equals("comissionado")){
            throw new EmpregadoNaoComissionadoException();
        }

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt){
            throw new DataInicialPosDataFinalException();
        }

        EmpregadoComissionado comissionado = (EmpregadoComissionado) emp;
        double vendasTotais = 0;
        for(ResultadoVenda venda : comissionado.getResultadoVendaList()){
            int dataVenda = converterDataInt(venda.getData());
            if(dataVenda >= diInt && dataVenda < dfInt){
                vendasTotais += venda.getValorVenda();
            }
        }

        return String.format("%.2f", vendasTotais).replace(".",",");
    }

    public void lancaTaxaServico(String idMembro, String data, String taxaServico) throws Exception{
        validarIdMembro(idMembro);
        validarData(data);
        validarValorVenda(taxaServico);

        Empregado emp = getEmpregadoPorSindicato(idMembro);
        double valor = converterValorVenda(taxaServico);
        emp.getFiliacao().adicionarTaxaServico(new TaxaServico(data, valor));
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal) throws Exception {
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        Empregado emp = buscarEmpregado(id);
        if (!emp.isSindicalizado()) {
            throw new EmpregadoNaoSindicalizadoException();
        }

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if (diInt > dfInt) {
            throw new DataInicialPosDataFinalException();
        }

        double totalTaxas = 0;
        for (TaxaServico taxa : emp.getFiliacao().getTaxaServicos()) {
            int dataTaxa = converterDataInt(taxa.getData());
            if (dataTaxa >= diInt && dataTaxa < dfInt) {
                totalTaxas += taxa.getValorTaxa();
            }
        }

        return String.format("%.2f", totalTaxas).replace(".", ",");
    }
}