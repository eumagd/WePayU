package br.ufal.ic.p2.wepayu.models;

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

    public String getIdMembro() {
        return idMembro;
    }

    public void adicionarTaxaServico(TaxaServico taxa){
        taxaServicoList.add(taxa);
    }

    public List<TaxaServico> getTaxaServicos() {
        return taxaServicoList;
    }

    public double getTaxaSindical() {
        return taxaSindical;
    }
}
