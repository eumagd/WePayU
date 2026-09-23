package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
public class Facade{

    private SistemaFolha sistema = new SistemaFolha();

    public void zerarSistema(){
        this.sistema = new SistemaFolha();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, double salario){
        return sistema.adicionarEmpregado(nome, endereco, tipo, salario);
    }
}
=======
public class Facade {

}
>>>>>>> 2e3c64a39bbd7fd46be321b76937fd9ecf985faf
