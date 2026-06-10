# Curadoria de Autores Científicos

Implementação acadêmica da deduplicação de nomes de autores.

## Equipe
- Hugo Ricardo Souza Bezerra - 180113585
- Danilo Carvalho Antunes - 211039312
- Thales Germano - 202017147
- Israel Thalles Dutra dos Santos - 190014776
- Marcos Antonio Teles de Castilhos - 221008300

## Escopo

### Caso 1: Diferenças de grafia (tipográficas)

O `NormalizadorNome` prepara nomes para comparação considerando equivalentes:

- letras maiúsculas e minúsculas;
- acentos e cedilha;
- espaços extras;
- os apóstrofos `'`, `’`, `‘`, `` ` ``, `´` e `ʼ`.

### Caso 2: Sobrenome + Iniciais dos nomes
O `DeduplicadorSobrenomeIniciais` reconhece a equivalência entre a versão completa de um nome e sua versão abreviada (sobrenome seguido das iniciais dos nomes, com ou sem pontos) e unifica os registros para a versão completa
(ex.: `Seabra A M` -> `Ana de Mattos Seabra`). Reaproveita o `NormalizadorNome` do Caso 1.

### Caso 3: Partículas "de" e uso de ponto nas abreviações opcionais
O `DeduplicadorSobrenomeIniciais` também reconhece a equivalência quando as partículas _de_ são omitidas ou o ponto nas abreviações é variável. Considera equivalentes: `Luiz de Oliveira de Souza`, `Luiz Oliveira Souza` e `Luiz de O. de Souza`. Unifica os registros para a versão com as partículas explícitas (ex.: `Luiz Oliveira Souza` -> `Luiz de Oliveira de Souza`).

### Caso 4: Iniciais dos nomes agrupadas + sobrenome

O `DeduplicadorIniciaisAgrupadas` reconhece a equivalência entre a versão completa de um nome e sua versão abreviada em que as iniciais do primeiro nome e dos sobrenomes intermediários aparecem aglutinadas e sem espaços antes do último sobrenome. A rotina identifica essa correspondência e unifica os registros priorizando a versão por extenso (ex.: `VC Junior` -> `Vanilda Cristina Junior`). Reaproveita o `NormalizadorNome` do Caso 1.

### Caso 5: IDs diferentes para o mesmo autor

O `UnificadorIdAutor` processa um conjunto de múltiplos registros de um mesmo autor que foram salvos com identificadores distintos devido à integração de diferentes indexadores e fontes de dados. O algoritmo consolida esses registros mapeando-os para um identificador único — elegendo sempre o ID de menor valor numérico da base — e mantém associada a versão de nome mais completa e dedupada (ex.: centraliza os diversos registros de `Raphael Gonçalves Viana` sob o menor ID `31298`). Delega a validação de equivalência ao `DeduplicadorSobrenomeIniciais`.

## Tecnologias
- Java 17;
- Maven;
- JUnit 5;
- Codificação UTF-8.

## Estrutura

```text
.
├── pom.xml
├── README.md
├── .gitignore
└── src
    ├── main/java/br/unb/tppe/curadoria
    │   ├── Autor.java
    │   ├── DeduplicadorIniciaisAgrupadas.java
    │   └── DeduplicadorSobrenomeIniciais.java
    │   ├── NomeInvalidoException.java
    │   ├── NomeNaoEquivalenteException.java
    │   ├── NormalizadorNome.java
    │   ├── UnificadorIdAutor.java
    └── test/java/br/unb/tppe/curadoria
        ├── Caso1Suite.java
        ├── Caso2Suite.java
        ├── Caso3Suite.java
        ├── Caso4Suite.java
        ├── Caso5Suite.java
        ├── DeduplicadorIniciaisAgrupadasTest.java
        ├── DeduplicadorSobrenomeIniciaisTest.java
        ├── NormalizadorNomeTest.java
        └── TodosOsTestesSuite.java
        └── UnificadorIdAutorTest.java
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
mvn test -Dgroups=caso4
mvn test -Dgroups=caso5
```

Execute as suítes JUnit Platform por caso ou a suíte global:

```bash
mvn test -Dtest=Caso1Suite
mvn test -Dtest=Caso2Suite
mvn test -Dtest=Caso3Suite
mvn test -Dtest=Caso4Suite
mvn test -Dtest=Caso5Suite
mvn test -Dtest=TodosOsTestesSuite
```

Como etapa final, o comando da Suíte Global para atestar a ausência de concorrência destrutiva entre os testes dos 5 casos:

```bash
mvn test -Dtest=TodosOsTestesSuite
```

## Ciclo TDD realizado

1. Foram criados primeiro os testes parametrizados, testes de exceção e suíte.
2. A primeira execução falhou na compilação porque as classes de produção ainda não existiam.
3. Foram implementados `NormalizadorNome` e `NomeInvalidoException`.
4. Os testes passaram.
5. A implementação foi organizada com validação isolada e expressões regulares pré-compiladas, mantendo os testes verdes.
6. Escrita dos cenários de testes parametrizados para os Casos 2 e 3 (`DeduplicadorSobrenomeIniciaisTest`), gerando falhas controladas para nomes com iniciais pontuadas e partículas omitidas. 
7. Codificação do `DeduplicadorSobrenomeIniciais` com lógica de tokenização e extração estruturada de sobrenomes, acionando a barra verde para as regras de abreviações e partículas opcionais (*de*, *da*, *do*). 
8. Elaboração dos testes parametrizados e de exceção para o Caso 4 (`DeduplicadorIniciaisAgrupadasTest`), simulando falhas para a identificação de iniciais aglutinadas (ex.: `VC Junior`). 
9. Desenvolvimento da regra de negócio na classe `DeduplicadorIniciaisAgrupadas`, reaproveitando o `NormalizadorNome` para validar equivalências de siglas coladas em relação ao nome por extenso e estabilizando os testes do caso. 
10. Criação de testes de unidade para o Caso 5 (`UnificadorIdAutorTest`), forçando erros de asserção ao tentar unificar listas de múltiplos registros de autores sob o menor identificador. 
11. Implementação do `UnificadorIdAutor` com busca de ID mínimo e posterior refatoração defensiva contra auto-comparações e falsos-positivos na massa de dados, garantindo o sucesso de execução de toda a suíte unificada (`TodosOsTestesSuite`) livre de condições de corrida.

## Sugestão de commits TDD

```text
test: adiciona cenarios do caso 1 e suite JUnit
feat: implementa normalizacao tipografica de nomes
refactor: organiza validacao e padroes de normalizacao
docs: documenta execucao e ciclo TDD
```
