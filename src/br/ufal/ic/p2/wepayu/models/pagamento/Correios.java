package br.ufal.ic.p2.wepayu.models.pagamento;

public class Correios extends MetodoPagamento{

    @Override
    public String getTipoPagamento(){
        return "correios";
    }

    @Override
    public MetodoPagamento copiar(){
        return new Correios();
    }

    @Override
    public String getDescricao(String endereco) {
        return "Correios, " + endereco;
    }
}
