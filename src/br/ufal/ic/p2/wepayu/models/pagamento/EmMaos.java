package br.ufal.ic.p2.wepayu.models.pagamento;

public class EmMaos extends MetodoPagamento{
    @Override
    public String getTipoPagamento(){
        return "emMaos";
    }

    @Override
    public MetodoPagamento copiar(){
        return new EmMaos();
    }

    @Override
    public String getDescricao(String endereco) {
        return "Em maos";
    }
}
