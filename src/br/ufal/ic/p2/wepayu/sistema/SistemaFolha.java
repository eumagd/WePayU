package br.ufal.ic.p2.wepayu.sistema;

import br.ufal.ic.p2.wepayu.exception.agenda.*;
import br.ufal.ic.p2.wepayu.exception.empregados.*;
import br.ufal.ic.p2.wepayu.exception.folha.*;
import br.ufal.ic.p2.wepayu.exception.pagamento.*;
import br.ufal.ic.p2.wepayu.exception.sindicato.*;
import br.ufal.ic.p2.wepayu.models.agenda.*;
import br.ufal.ic.p2.wepayu.models.empregados.*;
import br.ufal.ic.p2.wepayu.models.pagamento.*;
import br.ufal.ic.p2.wepayu.models.sindicato.*;
import br.ufal.ic.p2.wepayu.models.util.*;
import java.io.*;
import java.util.*;

public class SistemaFolha {
    private final ArrayList<Empregado> empregados = new ArrayList<>();
    private final ArrayList<AgendaPagamento> agendas = new ArrayList<>(AgendaPagamento.agendasPadrao());
    private int contId = 1;

    public static SistemaFolha restaurar(List<Empregado> empregados, int proximoId, List<AgendaPagamento> agendas){
        SistemaFolha sistema = new SistemaFolha();
        sistema.agendas.clear();
        for(AgendaPagamento agenda : agendas){
            sistema.agendas.add(agenda.copiar());
        }
        for(Empregado empregado : empregados){
            sistema.empregados.add(empregado.copiar());
        }
        sistema.contId = proximoId;
        return sistema;
    }

    public SistemaFolha copiar(){
        SistemaFolha copia = new SistemaFolha();
        copia.contId = contId;

        copia.agendas.clear();
        for(AgendaPagamento agenda : agendas){
            copia.agendas.add(agenda.copiar());
        }
        for(Empregado empregado : empregados){
            copia.empregados.add(empregado.copiar());
        }

        return copia;
    }

    public List<Empregado> empregadosParaPersistencia(){
        List<Empregado> copia = new ArrayList<>();
        for(Empregado empregado : empregados){
            copia.add(empregado.copiar());
        }
        return copia;
    }

    public int getProximoId(){
        return contId;
    }

    public List<AgendaPagamento> agendasParaPersistencia(){
        List<AgendaPagamento> copia = new ArrayList<>();
        for(AgendaPagamento agenda : agendas){
            copia.add(agenda.copiar());
        }
        return copia;
    }

    public void criarAgendaDePagamentos(String descricao)
            throws AgendaDescricaoInvalidaException, AgendaJaExisteException{
        AgendaPagamento novaAgenda = AgendaPagamento.criar(descricao);
        for(AgendaPagamento agenda : agendas){
            if(agenda.mesmaAgenda(novaAgenda)){
                throw new AgendaJaExisteException();
            }
        }
        agendas.add(novaAgenda);
    }

    //métodos privados
    private Empregado buscarEmpregado(String id) throws EmpregadoNaoExisteException{
        for(Empregado e : empregados){
            if(e.getId().equals(id)){
                return e;
            }
        }
        throw new EmpregadoNaoExisteException();
    }

    private void substituirEmpregado(Empregado antigo, Empregado novo){
        empregados.set(empregados.indexOf(antigo), novo);
    }

    private List<CartaoPonto> getCartoeFiltrados(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        Empregado.validarIdentificacao(id);
        DataPagamento.validarPeriodo(dataInicial, dataFinal);
        int diInt = DataPagamento.converter(dataInicial);
        int dfInt = DataPagamento.converter(dataFinal);

        Empregado emp = buscarEmpregado(id);
        List<CartaoPonto> cartoesFiltrados = new ArrayList<>();
        for(CartaoPonto cartao : emp.getCartaoPontoList()){
            int dataCartao = DataPagamento.converter(cartao.getData());
            if(dataCartao>=diInt&&dataCartao<dfInt){
                cartoesFiltrados.add(cartao);
            }
        }
        return cartoesFiltrados;
    }

    private List<ResultadoVenda> getVendasFiltradas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoComissionadoException{
        Empregado.validarIdentificacao(id);
        DataPagamento.validarPeriodo(dataInicial, dataFinal);
        int diInt = DataPagamento.converter(dataInicial);
        int dfInt = DataPagamento.converter(dataFinal);

        Empregado emp = buscarEmpregado(id);
        List<ResultadoVenda> vendasFiltradas = new ArrayList<>();
        for(ResultadoVenda venda : emp.getResultadoVendaList()){
            int dataVenda = DataPagamento.converter(venda.getData());
            if(dataVenda>=diInt&&dataVenda<dfInt){
                vendasFiltradas.add(venda);
            }
        }
        return vendasFiltradas;
    }

    private List<TaxaServico> getTaxasFiltradas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoSindicalizadoException{
        Empregado.validarIdentificacao(id);
        DataPagamento.validarPeriodo(dataInicial, dataFinal);
        Empregado emp = buscarEmpregado(id);
        if(!emp.isSindicalizado()) throw new EmpregadoNaoSindicalizadoException();

        int diInt = DataPagamento.converter(dataInicial);
        int dfInt = DataPagamento.converter(dataFinal);

        List<TaxaServico> taxasFiltradas = new ArrayList<>();
        for(TaxaServico taxa : emp.getFiliacao().getTaxaServicos()){
            int dataTaxa = DataPagamento.converter(taxa.getData());
            if(dataTaxa>=diInt&&dataTaxa<dfInt){
                taxasFiltradas.add(taxa);
            }
        }
        return taxasFiltradas;
    }

    //métodos public
    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr)
            throws NomeNuloException, EnderecoNuloException, SalarioNuloException,
            SalarioNaoNegativoException, SalarioNaoNumericoException, TipoNaoAplicavelException, TipoInvalidoException{
        FabricaEmpregado.validarNome(nome);
        FabricaEmpregado.validarEndereco(endereco);
        FabricaEmpregado.validarSalario(salarioStr);
        Empregado novoEmpregado = FabricaEmpregado.criar(nome, endereco, tipo,
                FabricaEmpregado.converterSalario(salarioStr));

        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);

        return novoId;
    }

    public String adicionarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr)
            throws NomeNuloException, EnderecoNuloException, SalarioNuloException,
            SalarioNaoNegativoException, SalarioNaoNumericoException, ComissaoNulaException,
            ComissaoNaoNumericaException, ComissaoNaoNegativaException, TipoNaoAplicavelException{
        FabricaEmpregado.validarNome(nome);
        FabricaEmpregado.validarEndereco(endereco);
        FabricaEmpregado.validarSalario(salarioStr);
        FabricaEmpregado.validarComissao(comissaoStr);
        Empregado novoEmpregado = FabricaEmpregado.criarComissionado(nome, endereco, tipo,
                FabricaEmpregado.converterSalario(salarioStr), FabricaEmpregado.converterComissao(comissaoStr));
        String novoId = String.valueOf(contId++);
        novoEmpregado.setId(novoId);
        empregados.add(novoEmpregado);
        return novoId;
    }

    public String getAtributoEmpregado(String id, String atributo)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, EmpregadoNaoComissionadoException,
            EmpregadoNaoSindicalizadoException, EmpregadoNaoRecebeEmBancoException, AtributoNaoExisteException{
        Empregado.validarIdentificacao(id);

        Empregado emp = buscarEmpregado(id);
        switch(atributo){
            case "nome":
                return emp.getNome();
            case "endereco":
                return emp.getEndereco();
            case "tipo":
                return emp.getTipo();
            case "salario":
                return Formatador.formatarValor(emp.getSalario());
            case "agendaPagamento":
                return emp.getAgendaPagamento();
            case "comissao":
                return Formatador.formatarValor(emp.getComissao());
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
                return Formatador.formatarValor(emp.getFiliacao().getTaxaSindical());
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public String getEmpregadoPorNome(String nome, int indice)
            throws NomeNuloException, NomeEmpregadoNaoExisteException{
        FabricaEmpregado.validarNome(nome);

        int aux = 0;
        for(int i = 0, empregadosSize = empregados.size(); i < empregadosSize; i++){
            Empregado e = empregados.get(i);
            if(e.getNome().contains(nome)){
                aux++;
                if(aux == indice){
                    return e.getId();
                }
            }
        }

        throw new NomeEmpregadoNaoExisteException();
    }

    public int getNumeroDeEmpregados(){
        return empregados.size();
    }

    public void alteraEmpregado(String id, String atributo, String valor)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, NomeNuloException,
            EnderecoNuloException, SalarioNuloException, SalarioNaoNegativoException, SalarioNaoNumericoException,
            ComissaoNulaException, ComissaoNaoNegativaException, ComissaoNaoNumericaException,
            EmpregadoNaoComissionadoException, MetodoPagamentoInvalidoException, ValorBooleanoInvalidoException,
            TipoInvalidoException, AtributoNaoExisteException, AgendaNaoDisponivelException{
        Empregado.validarIdentificacao(id);

        Empregado emp = buscarEmpregado(id);
        switch(atributo){
            case "nome":
                FabricaEmpregado.validarNome(valor);
                emp.setNome(valor);
                break;
            case "endereco":
                FabricaEmpregado.validarEndereco(valor);
                emp.setEndereco(valor);
                break;
            case "salario":
                FabricaEmpregado.validarSalario(valor);
                emp.setSalario(FabricaEmpregado.converterSalario(valor));
                break;
            case "comissao":
                FabricaEmpregado.validarComissao(valor);
                emp.setComissao(FabricaEmpregado.converterComissao(valor));
                break;
            case "agendaPagamento":
                emp.setAgendaPagamento(AgendaPagamento.selecionar(valor, agendas));
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
                substituirEmpregado(emp, FabricaEmpregado.trocarTipo(emp, valor));
                break;
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String comissao)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, SalarioNuloException,
            SalarioNaoNegativoException, SalarioNaoNumericoException, ComissaoNulaException,
            ComissaoNaoNegativaException, ComissaoNaoNumericaException, EmpregadoNaoComissionadoException,
            TipoInvalidoException{
        Empregado.validarIdentificacao(id);
        Empregado emp = buscarEmpregado(id);

        if(atributo.equals("tipo")){
            double novoSalario = emp.getSalario();
            double novaComissao = 0;
            if(valor.equals("comissionado")){
                FabricaEmpregado.validarComissao(comissao);
                novaComissao = FabricaEmpregado.converterComissao(comissao);
            }
            else if(valor.equals("horista") || valor.equals("assalariado")){
                FabricaEmpregado.validarSalario(comissao);
                novoSalario = FabricaEmpregado.converterSalario(comissao);
            }
            Empregado novoEmpregado = FabricaEmpregado.trocarTipo(emp, valor);
            if(valor.equals("comissionado")){
                FabricaEmpregado.definirComissao(novoEmpregado, novaComissao);
            }
            else if(valor.equals("horista") || valor.equals("assalariado")){
                novoEmpregado.setSalario(novoSalario);
            }
            substituirEmpregado(emp, novoEmpregado);
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException, TaxaSindicalNaoNegativaException,
            MesmaIdentificacaoException{
        Empregado.validarIdentificacao(id);

        Empregado emp = buscarEmpregado(id);
        if(atributo.equals("sindicalizado") && valor.equals("true")){
            MembroSindicato.associar(empregados, emp, idSindicato, taxaSindical);
        }
    }

    public void alteraEmpregado(String id, String atributo, String valor, String banco, String agencia, String contaCorrente)
            throws IdentificacaoNulaException, EmpregadoNaoExisteException, BancoNuloException,
            AgenciaNulaException, ContaCorrenteNulaException{
        Empregado.validarIdentificacao(id);

        Empregado emp = buscarEmpregado(id);
        if(atributo.equals("metodoPagamento") && valor.equals("banco")){
            MetodoPagamento novoMetodo = Banco.criar(banco, agencia, contaCorrente);
            emp.setMetodoPagamento(novoMetodo);
        }
    }

    public void removerEmpregado(String id) throws IdentificacaoNulaException, EmpregadoNaoExisteException{
        Empregado.validarIdentificacao(id);
        Empregado e = buscarEmpregado(id);
        empregados.remove(e);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
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

        return Formatador.formatarHoras(horasNormais);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        List<CartaoPonto> cartoes = getCartoeFiltrados(id, dataInicial, dataFinal);
        double horasExtras = 0;
        for(CartaoPonto cartao : cartoes){
            double horasDia = cartao.getHoras();
            if(horasDia > 8){
                horasExtras += (horasDia - 8);
            }
        }

        return Formatador.formatarHoras(horasExtras);
    }

    public void lancaCartao(String id, String data, String horas)
            throws IdentificacaoNulaException, DataInvalidaException, HoraNaoPositivaException,
            EmpregadoNaoExisteException, EmpregadoNaoHoristaException{
        Empregado.validarIdentificacao(id);
        DataPagamento.validar(data);
        CartaoPonto cartao = CartaoPonto.criar(data, horas);
        Empregado emp = buscarEmpregado(id);
        emp.adicionarCartaoPonto(cartao);
    }

    public void lancaVenda(String id, String data, String valorVenda)
            throws IdentificacaoNulaException, DataInvalidaException, ValorNaoPositivoException,
            EmpregadoNaoExisteException, EmpregadoNaoComissionadoException{
        Empregado.validarIdentificacao(id);
        DataPagamento.validar(data);
        ResultadoVenda novaVenda = ResultadoVenda.criar(data, valorVenda);
        Empregado emp = buscarEmpregado(id);
        emp.adicionarResultadoVenda(novaVenda);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoComissionadoException{
        List<ResultadoVenda> vendas = getVendasFiltradas(id, dataInicial, dataFinal);
        double vendasTotais = 0;
        for(ResultadoVenda venda : vendas){
            vendasTotais += venda.getValorVenda();
        }
        return Formatador.formatarValor(vendasTotais);
    }

    public void lancaTaxaServico(String idMembro, String data, String taxaServico)
            throws IdentificacaoMembroNulaException, DataInvalidaException, ValorNaoPositivoException,
            MembroSindicatoNaoExisteException{
        MembroSindicato.validarIdentificacao(idMembro);
        DataPagamento.validar(data);
        TaxaServico taxa = TaxaServico.criar(data, taxaServico);
        Empregado emp = MembroSindicato.buscarEmpregado(empregados, idMembro);
        emp.getFiliacao().adicionarTaxaServico(taxa);
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal)
            throws IdentificacaoNulaException, DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosDataFinalException, EmpregadoNaoExisteException, EmpregadoNaoSindicalizadoException{
        List<TaxaServico> taxas = getTaxasFiltradas(id, dataInicial, dataFinal);
        double totalTaxas = 0;
        for(TaxaServico taxa : taxas){
            totalTaxas += taxa.getValorTaxa();
        }
        return Formatador.formatarValor(totalTaxas);
    }

    public String totalFolha(String data) throws DataInvalidaException{
        DataPagamento.validar(data);
        double total = 0.0;
        for(Empregado emp : empregados){
            if(emp.verificaDiaPagamento(data)){
                total += emp.getSalarioBruto(data);
            }
        }
        return Formatador.formatarValor(total);
    }

    public void rodaFolha(String data, String arquivoSaida) throws DataInvalidaException, IOException{
        DataPagamento.validar(data);
        List<Empregado> pagamentosDoDia = new ArrayList<>();
        double totalFolha = 0.0;

        for(Empregado emp : empregados){
            if(emp.verificaDiaPagamento(data)){
                pagamentosDoDia.add(emp);
                totalFolha += emp.getSalarioBruto(data);
            }
        }

        pagamentosDoDia.sort(Comparator.comparing(Empregado::getNome));
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoSaida))){
            String dataFormatada = DataPagamento.formatarParaRelatorio(data);

            writer.write("FOLHA DE PAGAMENTO DO DIA " + dataFormatada + "\n");
            writer.write("====================================\n\n");

            //horistas
            writer.write("===============================================================================================================================\n");
            writer.write("===================== HORISTAS ================================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo\n");
            writer.write("==================================== ===== ===== ============= ========= =============== ======================================\n");

            int totalHoras = 0;
            int totalExtra = 0;
            double totalBrutoH = 0, totalDescH = 0, totalLiqH = 0;

            for(Empregado e : pagamentosDoDia){
                if(e.getTipo().equals("horista")){
                    int horas = e.getHorasNormaisPagas(data);
                    int extra = e.getHorasExtrasPagas(data);
                    double bruto = e.getSalarioBruto(data);
                    double desc = Math.min(bruto, e.getDescontosFolha(data));
                    double liq = bruto - desc;

                    String linha = String.format("%-36s %5d %5d %13s %9s %15s %s",
                            e.getNome(), horas, extra,
                            Formatador.formatarValor(bruto),
                            Formatador.formatarValor(desc), Formatador.formatarValor(liq),
                            e.getMetodoPagamento().getDescricao(e.getEndereco()));
                    writer.write(linha + "\n");

                    totalHoras += horas;
                    totalExtra += extra;
                    totalBrutoH += bruto;
                    totalDescH += desc;
                    totalLiqH += liq;
                }
            }
            writer.write("\n");
            writer.write(String.format("%-36s %5d %5d %13s %9s %15s\n\n",
                    "TOTAL HORISTAS", totalHoras,
                    totalExtra, Formatador.formatarValor(totalBrutoH),
                    Formatador.formatarValor(totalDescH), Formatador.formatarValor(totalLiqH)));

            //assalariados
            writer.write("===============================================================================================================================\n");
            writer.write("===================== ASSALARIADOS ============================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                                             Salario Bruto Descontos Salario Liquido Metodo\n");
            writer.write("================================================ ============= ========= =============== ======================================\n");

            double totalBrutoA = 0, totalDescA = 0, totalLiqA = 0;

            for(Empregado e : pagamentosDoDia){
                if(e.getTipo().equals("assalariado")){
                    double bruto = e.getSalarioBruto(data);
                    double desc = Math.min(bruto, e.getDescontosFolha(data));
                    double liq = bruto - desc;

                    String linha = String.format("%-48s %13s %9s %15s %s",
                            e.getNome(), Formatador.formatarValor(bruto),
                            Formatador.formatarValor(desc), Formatador.formatarValor(liq),
                            e.getMetodoPagamento().getDescricao(e.getEndereco()));
                    writer.write(linha + "\n");

                    totalBrutoA += bruto;
                    totalDescA += desc;
                    totalLiqA += liq;
                }
            }
            writer.write("\n");
            writer.write(String.format("%-48s %13s %9s %15s\n\n", "TOTAL ASSALARIADOS",
                    Formatador.formatarValor(totalBrutoA), Formatador.formatarValor(totalDescA),
                    Formatador.formatarValor(totalLiqA)));

            //comissionados
            writer.write("===============================================================================================================================\n");
            writer.write("===================== COMISSIONADOS ===========================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo\n");
            writer.write("===================== ======== ======== ======== ============= ========= =============== ======================================\n");

            double totalFixo = 0, totalVendas = 0, totalComissao = 0;
            double totalBrutoC = 0, totalDescC = 0, totalLiqC = 0;

            for(Empregado e : pagamentosDoDia){
                if(e.getTipo().equals("comissionado")){
                    double fixo = e.getSalarioFixoPago(data);
                    double vendas = e.getVendasPagas(data);
                    double valorComissao = Math.floor((vendas * e.getComissaoFolha()) * 100.0) / 100.0;
                    double bruto = e.getSalarioBruto(data);
                    double desc = Math.min(bruto, e.getDescontosFolha(data));
                    double liq = bruto - desc;

                    String linha = String.format("%-21s %8s %8s %8s %13s %9s %15s %s",
                            e.getNome(), Formatador.formatarValor(fixo),
                            Formatador.formatarValor(vendas), Formatador.formatarValor(valorComissao),
                            Formatador.formatarValor(bruto), Formatador.formatarValor(desc),
                            Formatador.formatarValor(liq),
                            e.getMetodoPagamento().getDescricao(e.getEndereco()));
                    writer.write(linha + "\n");

                    totalFixo += fixo;
                    totalVendas += vendas;
                    totalComissao += valorComissao;
                    totalBrutoC += bruto;
                    totalDescC += desc;
                    totalLiqC += liq;
                }
            }
            writer.write("\n");
            writer.write(String.format("%-21s %8s %8s %8s %13s %9s %15s\n\n", "TOTAL COMISSIONADOS",
                    Formatador.formatarValor(totalFixo), Formatador.formatarValor(totalVendas),
                    Formatador.formatarValor(totalComissao), Formatador.formatarValor(totalBrutoC),
                    Formatador.formatarValor(totalDescC), Formatador.formatarValor(totalLiqC)));

            writer.write("TOTAL FOLHA: " + Formatador.formatarValor(totalFolha) + "\n");
        }

        for(Empregado e : pagamentosDoDia){
            double bruto = e.getSalarioBruto(data);
            if(bruto > 0){
                double descontosPendentes = Math.max(0, e.getDescontosFolha(data) - bruto);
                e.atualizarProximoPagamento(data, descontosPendentes);
            }
        }
    }
}