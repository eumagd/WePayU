# WePayU — Sistema de Folha de Pagamento

Projeto da disciplina: sistema de informação para administrar o pagamento de empregados de uma empresa. A lógica de negócio é implementada sem interface gráfica, e validada através de testes de aceitação automáticos (EasyAccept).

## Status

Em desenvolvimento — código sendo implementado.

## Ambiente utilizado

Inicialmente VS Code (Microsoft) com posterior alteração para o IntelliJ (JetBrains).

## Sobre o sistema

O sistema gerencia empregados de três tipos:

- **Horista**: recebe por hora trabalhada, registrada em cartões de ponto. Horas acima de 8/dia são pagas a 1.5x. Pago toda sexta-feira.
- **Assalariado**: recebe salário fixo mensal. Pago no último dia útil do mês.
- **Comissionado**: assalariado que também recebe comissão sobre vendas realizadas. Pago a cada 2 sextas-feiras (2 semanas de salário fixo + comissões do período).

Empregados podem receber por **cheque pelos correios**, **cheque em mãos** ou **depósito em conta bancária**.

Empregados podem ser sindicalizados: o sindicato cobra uma taxa mensal, além de taxas de serviço eventuais, ambas deduzidas do contracheque.

A folha é rodada diariamente, pagando os empregados cujo salário vence naquele dia.

## Milestone 1 — User Stories 1 a 8

- [X] US1 — Adição de um empregado
- [X] US2 — Remoção de um empregado
- [ ] US3 — Lançar um cartão de ponto
- [ ] US4 — Lançar um resultado de venda
- [ ] US5 — Lançar uma taxa de serviço
- [ ] US6 — Alterar detalhes de um empregado
- [ ] US7 — Rodar a folha de pagamento para hoje
- [ ] US8 — Undo/redo

## Arquitetura

- Lógica de negócio separada de qualquer interface (não haverá GUI no projeto).
- Acesso à lógica de negócio feito através de uma **Façade**, que expõe os comandos usados pelos testes de aceitação (linguagem de script do EasyAccept).
- Persistência planejada via XML (`java.beans.XMLEncoder` / `XMLDecoder`).

## Comandos da Façade (US 1 a 8)

```
zerarSistema
criarEmpregado nome=<String> endereco=<String> tipo=<String> salario=<String>
criarEmpregado nome=<String> endereco=<String> tipo=comissionado salario=<String> comissao=<String>
removerEmpregado emp=<String>
alteraEmpregado emp=<String> atributo=<String> valor1=<String>
getAtributoEmpregado emp=<String> atributo=<String>
getEmpregadoPorNome nome=<String> indice=<int>
lancaCartao emp=<String> data=<String> horas=<String>
lancaVenda emp=<String> data=<String> valor=<String>
lancaTaxaServico emp=<String> data=<String> valor=<String>
getHorasTrabalhadas emp=<String> dataInicial=<String> dataFinal=<String>
getHorasExtrasTrabalhadas emp=<String> dataInicial=<String> dataFinal=<String>
getVendasRealizadas emp=<String> dataInicial=<String> dataFinal=<String>
getTaxasServico emp=<String> dataInicial=<String> dataFinal=<String>
rodaFolha data=<String> saida=<String>
totalFolha data=<String>
undo
redo
encerrarSistema
```

## Glossário (resumo)

- **Assalariado**: empregado com salário fixo mensal.
- **Horista**: empregado pago por hora, via cartões de ponto.
- **Comissionado**: assalariado que recebe também comissão sobre vendas.
- **Contracheque**: relatório de pagamento de um empregado (salário, deduções, etc).
- **Dedução**: valor retirado do salário do empregado (imposto, taxa sindical, taxa de serviço).
- **Sindicato**: associação que pode cobrar taxa sindical mensal e taxas de serviço adicionais.

Glossário completo no documento de especificação (`Sistema_de_Folha_de_Pagamento__WePayU_.pdf`).

## Como rodar os testes

Na classe Main.java se retira o indicador de comentário (//) do respectivo teste que se quer verificar e roda o respectivo arquivo.

## Referência

Especificação completa do projeto: `Sistema_de_Folha_de_Pagamento__WePayU_.pdf` (disponibilizado no Classroom da disciplina).
