# Curadoria de Autores Científicos

Implementação acadêmica da deduplicação de nomes de autores.

<!--

## Nome e matrícula dos integrantes do grupo

- Hugo Ricardo Souza Bezerra - 180113585

-->

## Escopo

### Caso 1: Diferenças de grafia (tipográficas)

O `NormalizadorNome` prepara nomes para comparação considerando equivalentes:

- letras maiúsculas e minúsculas;
- acentos e cedilha;
- espaços extras;
- os apóstrofos `'`, `’`, `‘`, `` ` ``, `´` e `ʼ`.

### Caso 2: Sobrenome + Iniciais dos nomes

O `DeduplicadorSobrenomeIniciais` reconhece a equivalência entre a versão
completa de um nome e sua versão abreviada (sobrenome seguido das iniciais dos
nomes, com ou sem pontos) e unifica os registros para a versão completa
(ex.: `Seabra A M` -> `Ana de Mattos Seabra`). Reaproveita o `NormalizadorNome`
do Caso 1.

### Caso 3: Partículas de e uso de ponto nas abreviações opcionais

O `DeduplicadorSobrenomeIniciais` também reconhece a equivalência quando as
partículas _de_ são omitidas ou o ponto nas abreviações é variável. Considera
equivalentes: `Luiz de Oliveira de Souza`, `Luiz Oliveira Souza` e
`Luiz de O. de Souza`. Unifica os registros para a versão com as partículas
explícitas (ex.: `Luiz Oliveira Souza` -> `Luiz de Oliveira de Souza`).

Os casos 4 e 5 não fazem parte desta implementação.

## Tecnologias

- Java 17;
- Maven;
- JUnit 5;
- codificação UTF-8.

## Estrutura

```text
.
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main/java/br/unb/tppe/curadoria
    │   ├── NomeInvalidoException.java
    │   ├── NomeNaoEquivalenteException.java
    │   ├── NormalizadorNome.java
    │   └── DeduplicadorSobrenomeIniciais.java
    └── test/java/br/unb/tppe/curadoria
        ├── NormalizadorNomeTest.java
        ├── DeduplicadorSobrenomeIniciaisTest.java
        ├── Caso1Suite.java
        ├── Caso2Suite.java
        ├── Caso3Suite.java
        └── TodosOsTestesSuite.java
```

## Execução

Execute todos os testes:

```bash
mvn clean test
```

Execute somente os testes de um caso por categoria (tag):

```bash
mvn test -Dgroups=caso1
mvn test -Dgroups=caso2
mvn test -Dgroups=caso3
```

Execute as suítes JUnit Platform por caso ou a suíte global:

```bash
mvn test -Dtest=Caso1Suite
mvn test -Dtest=Caso2Suite
mvn test -Dtest=Caso3Suite
mvn test -Dtest=TodosOsTestesSuite
```

## Ciclo TDD realizado

1. Foram criados primeiro os testes parametrizados, testes de exceção e suíte.
2. A primeira execução falhou na compilação porque as classes de produção ainda
   não existiam.
3. Foram implementados `NormalizadorNome` e `NomeInvalidoException`.
4. Os testes passaram.
5. A implementação foi organizada com validação isolada e expressões regulares
   pré-compiladas, mantendo os testes verdes.

## Sugestão de commits TDD

```text
test: adiciona cenarios do caso 1 e suite JUnit
feat: implementa normalizacao tipografica de nomes
refactor: organiza validacao e padroes de normalizacao
docs: documenta execucao e ciclo TDD
```
