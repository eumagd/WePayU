//lembrar: organizar classes, métodos e exceptions

package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaFolha {
    private final ArrayList<Empregado> empregados = new ArrayList<>();
    private int contId = 1;

    //métodos privados
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
        if(!validarFormatoData(data)){
            throw new DataInvalidaException();
        }
    }

    private void validarDataInicial(String dataInicial) throws DataInicialInvalidaException{
        if(!validarFormatoData(dataInicial)){
            throw new DataInicialInvalidaException();
        }
    }

    private void validarDataFinal(String dataFinal) throws DataFinalInvalidaException{
        if(!validarFormatoData(dataFinal)){
            throw new DataFinalInvalidaException();
        }
    }

    private boolean validarFormatoData(String data){
        if(data == null || data.isEmpty()){
            return false;
        }

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

    private String formatarValor(double valor){
        return String.format("%.2f", valor).replace(".", ",");
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

    private void validarComissao(String comissaoStr) throws ComissaoNulaException {
        if (comissaoStr == null || comissaoStr.isEmpty()) {
            throw new ComissaoNulaException();
        }
    }

    private double converterComissao(String comissao) throws ComissaoNaoNumericaException, ComissaoNaoNegativaException {
        try {
            double novaComissao = Double.parseDouble(comissao.replace(",", "."));
            if (novaComissao < 0) {
                throw new ComissaoNaoNegativaException();
            }
            return novaComissao;
        }
        catch(NumberFormatException e){
            throw new ComissaoNaoNumericaException();
        }
    }

    private void validarBanco(String banco) throws BancoNuloException {
        if(banco == null || banco.isEmpty()) throw new BancoNuloException();
    }

    private void validarAgencia(String agencia) throws AgenciaNulaException {
        if(agencia == null || agencia.isEmpty()) throw new AgenciaNulaException();
    }

    private void validarContaCorrente(String contaCorrente) throws ContaCorrenteNulaException {
        if(contaCorrente == null || contaCorrente.isEmpty()) throw new ContaCorrenteNulaException();
    }

    private void validarIdSindicato(String idSindicato) throws  IdentificacaoSindicatoNulaException{
        if(idSindicato == null || idSindicato.isEmpty()) throw new IdentificacaoSindicatoNulaException();
    }

    private void validarTaxaSindical(String taxaSindical) throws TaxaSindicalNulaException{
        if(taxaSindical == null || taxaSindical.isEmpty()) throw new TaxaSindicalNulaException();
    }

    private Empregado getEmpregadoPorSindicato(String idSindicato) throws Exception{
        for (Empregado e : empregados) {
            if (e.isSindicalizado() && e.getFiliacao().getIdMembro().equals(idSindicato)){
                return e;
            }
        }
        throw new MembroSindicatoNaoExisteException();
    }

    private void mudarTipoEmpregado(Empregado empAntigo, String novoTipo) throws Exception {
        Empregado empNovo;
        double salario = empAntigo.getSalario();
        switch(novoTipo){
            case "horista":
                empNovo = new EmpregadoHorista(empAntigo.getNome(), empAntigo.getEndereco(), novoTipo, salario);
                break;
            case "assalariado":
                empNovo = new EmpregadoAssalariado(empAntigo.getNome(), empAntigo.getEndereco(), novoTipo, salario);
                break;
            case "comissionado":
                empNovo = new EmpregadoComissionado(empAntigo.getNome(), empAntigo.getEndereco(), novoTipo, salario, 0.0f);
                break;
            default:
                throw new TipoInvalidoException();
        }

        empNovo.setId(empAntigo.getId());
        empNovo.setMetodoPagamento(empAntigo.getMetodoPagamento());
        empNovo.setFiliacao(empAntigo.getFiliacao());
        int index = empregados.indexOf(empAntigo);
        empregados.set(index, empNovo);
    }

    private List<CartaoPonto> getCartoeFiltrados(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt) throw new DataInicialPosDataFinalException();

        Empregado emp = buscarEmpregado(id);
        List<CartaoPonto> cartoesFiltrados = new ArrayList<>();
        for(CartaoPonto cartao : emp.getCartaoPontoList()){
            int dataCartao = converterDataInt(cartao.getData());
            if(dataCartao>=diInt&&dataCartao<dfInt){
                cartoesFiltrados.add(cartao);
            }
        }
        return cartoesFiltrados;
    }

    private List<ResultadoVenda> getVendasFiltradas(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt) throw new DataInicialPosDataFinalException();

        Empregado emp = buscarEmpregado(id);
        List<ResultadoVenda> vendasFiltradas = new ArrayList<>();
        for(ResultadoVenda venda : emp.getResultadoVendaList()){
            int dataVenda = converterDataInt(venda.getData());
            if(dataVenda>=diInt&&dataVenda<dfInt){
                vendasFiltradas.add(venda);
            }
        }
        return vendasFiltradas;
    }

    private List<TaxaServico> getTaxasFiltradas(String id, String dataInicial, String dataFinal) throws Exception{
        validarId(id);
        validarDataInicial(dataInicial);
        validarDataFinal(dataFinal);

        Empregado emp = buscarEmpregado(id);
        if(!emp.isSindicalizado()) throw new EmpregadoNaoSindicalizadoException();

        int diInt = converterDataInt(dataInicial);
        int dfInt = converterDataInt(dataFinal);
        if(diInt > dfInt) throw new DataInicialPosDataFinalException();

        List<TaxaServico> taxasFiltradas = new ArrayList<>();
        for(TaxaServico taxa : emp.getFiliacao().getTaxaServicos()){
            int dataTaxa = converterDataInt(taxa.getData());
            if(dataTaxa>=diInt&&dataTaxa<dfInt){
                taxasFiltradas.add(taxa);
            }
        }
        return taxasFiltradas;
    }

    //métodos public
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
        validarComissao(comissaoStr);

        double salario = converterSalario(salarioStr);
        double comissao = converterComissao(comissaoStr);
        if(tipo.equals("horista") || tipo.equals("assalariado")){
            throw new TipoNaoAplicavelException();
        }

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
                return formatarValor(emp.getSalario());
            case "comissao":
                return formatarValor(emp.getComissao());
            case "sindicalizado":
                return String.valueOf(emp.isSindicalizado());
            case "metodoPagamento":
                return emp.getMetodoPagamento().getTipoPagamento();
            case "banco":
                return emp.getMetodoPagamento().getBanco();
            case "agencia":
                return emp.getMetodoPagamento().getAgencia();
            case "contaCorrente":
                return emp.getMetodoPagamento().getContaCorrente();
            case "idSindicato":
                if(!emp.isSindicalizado()) throw new EmpregadoNaoSindicalizadoException();
                return emp.getFiliacao().getIdMembro();
            case "taxaSindical":
                if(!emp.isSindicalizado()) throw new EmpregadoNaoSindicalizadoException();
                return formatarValor(emp.getFiliacao().getTaxaSindical());
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
        switch(atributo){
            case "nome":
                validarNome(valor);
                emp.setNome(valor);
                break;
            case "endereco":
                validarEndereco(valor);
                emp.setEndereco(valor);
                break;
            case "salario":
                validarSalario(valor);
                emp.setSalario(converterSalario(valor));
                break;
            case "comissao":
                validarComissao(valor);
                emp.setComissao(converterComissao(valor));
                break;
            case "metodoPagamento":
                if(valor.equals("emMaos")){
                    emp.setMetodoPagamento(new EmMaos());
                }
                else if(valor.equals("correios")){
                    emp.setMetodoPagamento(new Correios());
                }
                else if(!valor.equals("banco")){
                    throw new MetodoPagamentoInvalidoException();
                }
                break;
            case "sindicalizado":
                if(!valor.equals("true") && !valor.equals("false")){
                    throw new ValorBooleanoInvalidoException();
                }
                if(valor.equals("false")){
                    emp.setFiliacao(null);
                }
                break;
            case "tipo":
                mudarTipoEmpregado(emp, valor);
                break;
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String comissao) throws Exception{
        validarId(id);
        Empregado emp = buscarEmpregado(id);

        if (atributo.equals("tipo")){
            mudarTipoEmpregado(emp, valor);
            Empregado novoEmp = buscarEmpregado(id);

            if(valor.equals("comissionado")){
                validarComissao(comissao);
                novoEmp.setComissao(converterComissao(comissao));
            }
            else if(valor.equals("horista") || valor.equals("assalariado")){
                validarSalario(comissao);;
                novoEmp.setSalario(converterSalario(comissao));
            }
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception{
        validarId(id);

        Empregado emp = buscarEmpregado(id);
        if (atributo.equals("sindicalizado") && valor.equals("true")){
            validarIdSindicato(idSindicato);
            validarTaxaSindical(taxaSindical);

            double taxa;
            try{
                taxa = Double.parseDouble(taxaSindical.replace(",", "."));
                if(taxa < 0){
                    throw new TaxaSindicalNaoNegativaException();
                }
            }
            catch(NumberFormatException e){
                throw new TaxaSindicalNaoNumericaException();
            }

            for(Empregado e : empregados){
                if(e.isSindicalizado() && e.getFiliacao().getIdMembro().equals(idSindicato)) {
                    throw new MesmaIdentificacaoException();
                }
            }

            MembroSindicato novaFiliacao = new MembroSindicato(idSindicato, taxa);
            emp.setFiliacao(novaFiliacao);
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String banco, String agencia, String contaCorrente) throws Exception{
        validarId(id);

        Empregado emp = buscarEmpregado(id);
        if(atributo.equals("metodoPagamento") && valor.equals("banco")){
            validarBanco(banco);
            validarAgencia(agencia);
            validarContaCorrente(contaCorrente);

            MetodoPagamento novoMetodo = new Banco(banco, agencia, contaCorrente);
            emp.setMetodoPagamento(novoMetodo);
        }
    }

    public void removerEmpregado(String id) throws Exception{
        validarId(id);
        Empregado e = buscarEmpregado(id);
        empregados.remove(e);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        List<CartaoPonto> cartoes = getCartoeFiltrados(id, dataInicial, dataFinal);
        double horasNormais = 0;
        for(CartaoPonto cartao : cartoes){
            double horasDia = cartao.getHoras();
            if(horasDia <= 8){
                horasNormais += horasDia;
            }
            else{
                horasNormais += 8;
            }
        }

        return formatarHoras(horasNormais);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception{
        List<CartaoPonto> cartoes = getCartoeFiltrados(id, dataInicial, dataFinal);
        double horasExtras = 0;
        for(CartaoPonto cartao : cartoes){
            double horasDia = cartao.getHoras();
            if(horasDia > 8){
                horasExtras += (horasDia - 8);
            }
        }

        return formatarHoras(horasExtras);
    }

    public void lancaCartao(String id, String data, String horas) throws Exception{
        validarId(id);
        validarData(data);
        validarHoras(horas);

        Empregado emp = buscarEmpregado(id);
        double horasDouble = converterHoras(horas);
        CartaoPonto cartao = new CartaoPonto(data, horasDouble);
        emp.adicionarCartaoPonto(cartao);
    }

    public void lancaVenda(String id, String data, String valorVenda) throws Exception{
        validarId(id);
        validarData(data);
        validarValorVenda(valorVenda);

        Empregado emp = buscarEmpregado(id);
        double novoValor = converterValorVenda(valorVenda);
        ResultadoVenda novaVenda = new ResultadoVenda(data, novoValor);
        emp.adicionarResultadoVenda(novaVenda);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal) throws Exception{
        List<ResultadoVenda> vendas = getVendasFiltradas(id, dataInicial, dataFinal);
        double vendasTotais = 0;
        for(ResultadoVenda venda : vendas){
            vendasTotais += venda.getValorVenda();
        }
        return formatarValor(vendasTotais);
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
        List<TaxaServico> taxas = getTaxasFiltradas(id, dataInicial, dataFinal);
        double totalTaxas = 0;
        for(TaxaServico taxa : taxas){
            totalTaxas += taxa.getValorTaxa();
        }
        return formatarValor(totalTaxas);
    }
}