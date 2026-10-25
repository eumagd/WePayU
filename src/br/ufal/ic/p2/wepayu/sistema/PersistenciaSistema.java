package br.ufal.ic.p2.wepayu.sistema;

import br.ufal.ic.p2.wepayu.exception.agenda.*;
import br.ufal.ic.p2.wepayu.exception.empregados.*;
import br.ufal.ic.p2.wepayu.exception.pagamento.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.exception.sistema.*;
import br.ufal.ic.p2.wepayu.models.agenda.*;
import br.ufal.ic.p2.wepayu.models.empregados.*;
import br.ufal.ic.p2.wepayu.models.pagamento.*;
import br.ufal.ic.p2.wepayu.models.sindicato.*;
import br.ufal.ic.p2.wepayu.models.util.*;
import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PersistenciaSistema {
    private static final Path ARQUIVO = Paths.get("wepayu.xml");

    private PersistenciaSistema(){
    }

    public static SistemaFolha carregar(){
        if(!Files.exists(ARQUIVO)){
            return new SistemaFolha();
        }

        try{
            DocumentBuilder builder = criarConstrutor();
            Document documento = builder.parse(ARQUIVO.toFile());
            Element raiz = documento.getDocumentElement();
            int proximoId = Integer.parseInt(raiz.getAttribute("proximoId"));
            List<AgendaPagamento> agendas = carregarAgendas(raiz);
            List<Empregado> empregados = new ArrayList<>();
            NodeList elementos = raiz.getElementsByTagName("empregado");

            for(int i = 0; i < elementos.getLength(); i++){
                empregados.add(carregarEmpregado((Element) elementos.item(i), agendas));
            }

            return SistemaFolha.restaurar(empregados, proximoId, agendas);
        }
        catch(ParserConfigurationException | org.xml.sax.SAXException | IOException
              | IllegalArgumentException | AgendaNaoDisponivelException | AgendaDescricaoInvalidaException
              | EmpregadoNaoHoristaException | EmpregadoNaoComissionadoException e){
            throw new FalhaAoCarregarDadosException(e);
        }
    }

    public static void salvar(SistemaFolha sistema){
        Path arquivoTemporario = null;
        try{
            DocumentBuilder builder = criarConstrutor();
            Document documento = builder.newDocument();
            Element raiz = documento.createElement("sistema");
            raiz.setAttribute("proximoId", String.valueOf(sistema.getProximoId()));
            documento.appendChild(raiz);

            Element agendas = documento.createElement("agendas");
            raiz.appendChild(agendas);
            for(AgendaPagamento agenda : sistema.agendasParaPersistencia()){
                Element item = documento.createElement("agenda");
                item.setAttribute("formula", agenda.getFormula());
                agendas.appendChild(item);
            }

            for(Empregado empregado : sistema.empregadosParaPersistencia()){
                salvarEmpregado(documento, raiz, empregado);
            }

            TransformerFactory fabrica = TransformerFactory.newInstance();
            fabrica.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformador = fabrica.newTransformer();
            transformador.setOutputProperty("encoding", "UTF-8");
            transformador.setOutputProperty("indent", "yes");

            Path destino = ARQUIVO.toAbsolutePath();
            Files.createDirectories(destino.getParent());
            arquivoTemporario = Files.createTempFile(destino.getParent(), "wepayu-", ".xml");
            transformador.transform(new DOMSource(documento), new StreamResult(arquivoTemporario.toFile()));
            try{
                Files.move(arquivoTemporario, destino, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            catch(AtomicMoveNotSupportedException e){
                Files.move(arquivoTemporario, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch(ParserConfigurationException | TransformerException | IOException
              | EmpregadoNaoHoristaException | EmpregadoNaoComissionadoException
              | EmpregadoNaoRecebeEmBancoException | IllegalArgumentException e){
            throw new FalhaAoSalvarDadosException(e);
        }
    }

    private static DocumentBuilder criarConstrutor() throws ParserConfigurationException{
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        fabrica.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        fabrica.setXIncludeAware(false);
        fabrica.setExpandEntityReferences(false);
        return fabrica.newDocumentBuilder();
    }

    private static void salvarEmpregado(Document documento, Element raiz, Empregado empregado)
            throws EmpregadoNaoHoristaException, EmpregadoNaoComissionadoException,
            EmpregadoNaoRecebeEmBancoException{
        Element elemento = documento.createElement("empregado");
        raiz.appendChild(elemento);
        adicionarTexto(documento, elemento, "id", empregado.getId());
        adicionarTexto(documento, elemento, "nome", empregado.getNome());
        adicionarTexto(documento, elemento, "endereco", empregado.getEndereco());
        adicionarTexto(documento, elemento, "tipo", empregado.getTipo());
        adicionarTexto(documento, elemento, "salario", String.valueOf(empregado.getSalario()));
        adicionarTexto(documento, elemento, "agendaPagamento", empregado.getAgendaPagamento());
        adicionarTexto(documento, elemento, "dataUltimoPagamento", empregado.getDataUltimoPagamento());
        adicionarTexto(documento, elemento, "descontosPendentes",
                String.valueOf(empregado.getDescontosPendentes()));

        MetodoPagamento metodo = empregado.getMetodoPagamento();
        Element pagamento = documento.createElement("metodoPagamento");
        pagamento.setAttribute("tipo", metodo.getTipoPagamento());
        elemento.appendChild(pagamento);
        if(metodo.getTipoPagamento().equals("banco")){
            adicionarTexto(documento, pagamento, "banco", metodo.getBanco());
            adicionarTexto(documento, pagamento, "agencia", metodo.getAgencia());
            adicionarTexto(documento, pagamento, "contaCorrente", metodo.getContaCorrente());
        }

        if(empregado.getTipo().equals("comissionado")){
            adicionarTexto(documento, elemento, "comissao", String.valueOf(empregado.getComissaoFolha()));
            Element vendas = documento.createElement("vendas");
            elemento.appendChild(vendas);
            for(ResultadoVenda venda : empregado.getResultadoVendaList()){
                Element item = documento.createElement("venda");
                item.setAttribute("data", venda.getData());
                item.setAttribute("valor", String.valueOf(venda.getValorVenda()));
                vendas.appendChild(item);
            }
        }
        else if(empregado.getTipo().equals("horista")){
            Element cartoes = documento.createElement("cartoesPonto");
            elemento.appendChild(cartoes);
            for(CartaoPonto cartao : empregado.getCartaoPontoList()){
                Element item = documento.createElement("cartaoPonto");
                item.setAttribute("data", cartao.getData());
                item.setAttribute("horas", String.valueOf(cartao.getHoras()));
                cartoes.appendChild(item);
            }
        }

        if(empregado.isSindicalizado()){
            MembroSindicato sindicato = empregado.getFiliacao();
            Element filiacao = documento.createElement("sindicato");
            filiacao.setAttribute("id", sindicato.getIdMembro());
            filiacao.setAttribute("taxaSindical", String.valueOf(sindicato.getTaxaSindical()));
            elemento.appendChild(filiacao);
            for(TaxaServico taxa : sindicato.getTaxaServicos()){
                Element item = documento.createElement("taxaServico");
                item.setAttribute("data", taxa.getData());
                item.setAttribute("valor", String.valueOf(taxa.getValorTaxa()));
                filiacao.appendChild(item);
            }
        }
    }

    private static List<AgendaPagamento> carregarAgendas(Element raiz)
            throws AgendaDescricaoInvalidaException{
        if(!temFilho(raiz, "agendas")){
            return AgendaPagamento.agendasPadrao();
        }

        List<AgendaPagamento> agendas = new ArrayList<>();
        NodeList elementos = filho(raiz, "agendas").getElementsByTagName("agenda");
        for(int i = 0; i < elementos.getLength(); i++){
            Element elemento = (Element) elementos.item(i);
            AgendaPagamento agenda = AgendaPagamento.criar(elemento.getAttribute("formula"));
            for(AgendaPagamento cadastrada : agendas){
                if(cadastrada.mesmaAgenda(agenda)){
                    throw new FalhaAoCarregarDadosException("agenda duplicada no arquivo XML.");
                }
            }
            agendas.add(agenda);
        }
        return agendas.isEmpty() ? AgendaPagamento.agendasPadrao() : agendas;
    }

    private static Empregado carregarEmpregado(Element elemento, List<AgendaPagamento> agendas)
            throws AgendaNaoDisponivelException, EmpregadoNaoHoristaException,
            EmpregadoNaoComissionadoException{
        String tipo = texto(elemento, "tipo");
        String nome = texto(elemento, "nome");
        String endereco = texto(elemento, "endereco");
        double salario = numero(texto(elemento, "salario"));
        Empregado empregado;

        if(tipo.equals("horista")){
            empregado = new EmpregadoHorista(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("assalariado")){
            empregado = new EmpregadoAssalariado(nome, endereco, tipo, salario);
        }
        else if(tipo.equals("comissionado")){
            empregado = new EmpregadoComissionado(nome, endereco, tipo, salario,
                    numero(texto(elemento, "comissao")));
        }
        else{
            throw new FalhaAoCarregarDadosException("tipo de empregado invalido no arquivo XML.");
        }

        empregado.setId(texto(elemento, "id"));
        String formulaAgenda = temFilho(elemento, "agendaPagamento")
                ? texto(elemento, "agendaPagamento")
                : AgendaPagamento.padraoPara(tipo).getFormula();
        empregado.setAgendaPagamento(AgendaPagamento.selecionar(formulaAgenda, agendas));
        empregado.atualizarProximoPagamento(texto(elemento, "dataUltimoPagamento"),
                numero(texto(elemento, "descontosPendentes")));

        Element pagamento = filho(elemento, "metodoPagamento");
        String tipoPagamento = pagamento.getAttribute("tipo");
        if(tipoPagamento.equals("correios")){
            empregado.setMetodoPagamento(new Correios());
        }
        else if(tipoPagamento.equals("banco")){
            empregado.setMetodoPagamento(new Banco(texto(pagamento, "banco"),
                    texto(pagamento, "agencia"), texto(pagamento, "contaCorrente")));
        }
        else if(!tipoPagamento.equals("emMaos")){
            throw new FalhaAoCarregarDadosException("metodo de pagamento invalido no arquivo XML.");
        }

        if(tipo.equals("horista")){
            carregarCartoes(elemento, empregado);
        }
        else if(tipo.equals("comissionado")){
            carregarVendas(elemento, empregado);
        }

        if(temFilho(elemento, "sindicato")){
            carregarSindicato(elemento, empregado);
        }
        return empregado;
    }

    private static void carregarCartoes(Element elemento, Empregado empregado)
            throws EmpregadoNaoHoristaException{
        if(!temFilho(elemento, "cartoesPonto")){
            return;
        }
        NodeList cartoes = filho(elemento, "cartoesPonto").getElementsByTagName("cartaoPonto");
        for(int i = 0; i < cartoes.getLength(); i++){
            Element cartao = (Element) cartoes.item(i);
            empregado.adicionarCartaoPonto(new CartaoPonto(cartao.getAttribute("data"),
                    numero(cartao.getAttribute("horas"))));
        }
    }

    private static void carregarVendas(Element elemento, Empregado empregado)
            throws EmpregadoNaoComissionadoException{
        if(!temFilho(elemento, "vendas")){
            return;
        }
        NodeList vendas = filho(elemento, "vendas").getElementsByTagName("venda");
        for(int i = 0; i < vendas.getLength(); i++){
            Element venda = (Element) vendas.item(i);
            empregado.adicionarResultadoVenda(new ResultadoVenda(venda.getAttribute("data"),
                    numero(venda.getAttribute("valor"))));
        }
    }

    private static void carregarSindicato(Element elemento, Empregado empregado){
        Element sindicatoElemento = filho(elemento, "sindicato");
        MembroSindicato sindicato = new MembroSindicato(sindicatoElemento.getAttribute("id"),
                numero(sindicatoElemento.getAttribute("taxaSindical")));
        NodeList taxas = sindicatoElemento.getElementsByTagName("taxaServico");
        for(int i = 0; i < taxas.getLength(); i++){
            Element taxa = (Element) taxas.item(i);
            sindicato.adicionarTaxaServico(new TaxaServico(taxa.getAttribute("data"),
                    numero(taxa.getAttribute("valor"))));
        }
        empregado.setFiliacao(sindicato);
    }

    private static void adicionarTexto(Document documento, Element pai, String nome, String valor){
        Element elemento = documento.createElement(nome);
        elemento.appendChild(documento.createTextNode(valor));
        pai.appendChild(elemento);
    }

    private static String texto(Element pai, String nome){
        return filho(pai, nome).getTextContent();
    }

    private static Element filho(Element pai, String nome){
        NodeList nos = pai.getChildNodes();
        for(int i = 0; i < nos.getLength(); i++){
            Node no = nos.item(i);
            if(no.getNodeType() == Node.ELEMENT_NODE && ((Element) no).getTagName().equals(nome)){
                return (Element) no;
            }
        }
        throw new FalhaAoCarregarDadosException("elemento XML obrigatorio ausente: " + nome);
    }

    private static boolean temFilho(Element pai, String nome){
        NodeList nos = pai.getChildNodes();
        for(int i = 0; i < nos.getLength(); i++){
            Node no = nos.item(i);
            if(no.getNodeType() == Node.ELEMENT_NODE && ((Element) no).getTagName().equals(nome)){
                return true;
            }
        }
        return false;
    }

    private static double numero(String valor){
        try{
            return Double.parseDouble(valor);
        }
        catch(NumberFormatException e){
            throw new FalhaAoCarregarDadosException(e);
        }
    }
}
