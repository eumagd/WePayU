package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.sindicato.*;

import java.util.ArrayList;
import java.util.List;

public class MembroSindicato {
    private String idMembro;
    private double taxaSindical;
    private List<TaxaServico> taxaServicoList;

    public MembroSindicato(String idMembro, double taxaSindical){
        this.idMembro = idMembro;
        this.taxaSindical = taxaSindical;
        this.taxaServicoList = new ArrayList<>();
    }

    public static void validarIdentificacao(String idMembro) throws IdentificacaoMembroNulaException{
        if(idMembro == null || idMembro.isEmpty()){
            throw new IdentificacaoMembroNulaException();
        }
    }

    public static MembroSindicato criar(String idMembro, String taxaSindical)
            throws IdentificacaoSindicatoNulaException, TaxaSindicalNulaException,
            TaxaSindicalNaoNumericaException, TaxaSindicalNaoNegativaException{
        if(idMembro == null || idMembro.isEmpty()){
            throw new IdentificacaoSindicatoNulaException();
        }
        if(taxaSindical == null || taxaSindical.isEmpty()){
            throw new TaxaSindicalNulaException();
        }

        try{
            double taxa = Double.parseDouble(taxaSindical.replace(",", "."));
            if(taxa < 0){
                throw new TaxaSindicalNaoNegativaException();
            }
            return new MembroSindicato(idMembro, taxa);
        }
        catch(NumberFormatException e){
            throw new TaxaSindicalNaoNumericaException();
        }
    }

    public static void associar(List<Empregado> empregados, Empregado empregado, String idMembro, String taxaSindical)
            throws IdentificacaoSindicatoNulaException, TaxaSindicalNulaException,
            TaxaSindicalNaoNumericaException, TaxaSindicalNaoNegativaException, MesmaIdentificacaoException{
        MembroSindicato novoMembro = criar(idMembro, taxaSindical);
        for(Empregado existente : empregados){
            if(existente != empregado && existente.isSindicalizado()
                    && existente.getFiliacao().getIdMembro().equals(idMembro)){
                throw new MesmaIdentificacaoException();
            }
        }
        empregado.setFiliacao(novoMembro);
    }

    public static Empregado buscarEmpregado(List<Empregado> empregados, String idMembro)
            throws MembroSindicatoNaoExisteException{
        for(Empregado empregado : empregados){
            if(empregado.isSindicalizado() && empregado.getFiliacao().getIdMembro().equals(idMembro)){
                return empregado;
            }
        }
        throw new MembroSindicatoNaoExisteException();
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void adicionarTaxaServico(TaxaServico taxa){
        taxaServicoList.add(taxa);
    }

    public List<TaxaServico> getTaxaServicos() {
        List<TaxaServico> taxas = new ArrayList<>();
        for(TaxaServico taxa : taxaServicoList){
            taxas.add(new TaxaServico(taxa.getData(), taxa.getValorTaxa()));
        }
        return taxas;
    }

    public double getTaxaSindical() {
        return taxaSindical;
    }

    public MembroSindicato copiar(){
        MembroSindicato copia = new MembroSindicato(idMembro, taxaSindical);
        for(TaxaServico taxa : taxaServicoList){
            copia.adicionarTaxaServico(new TaxaServico(taxa.getData(), taxa.getValorTaxa()));
        }
        return copia;
    }
}
