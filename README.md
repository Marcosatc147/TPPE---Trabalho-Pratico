# Curadoria de Autores Científicos

Implementação acadêmica do Caso 1 de deduplicação de nomes: diferenças de
grafia ou diferenças tipográficas.

## Escopo

O `NormalizadorNome` prepara nomes para comparação considerando equivalentes:

- letras maiúsculas e minúsculas;
- acentos e cedilha;
- espaços extras;
- os apóstrofos `'`, `’`, `‘`, `` ` ``, `´` e `ʼ`.

Os casos 2, 3, 4 e 5 não fazem parte desta implementação.

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
    │   └── NormalizadorNome.java
    └── test/java/br/unb/tppe/curadoria
        ├── NormalizadorNomeTest.java
        └── TodosOsTestesSuite.java
```

## Execução

Execute todos os testes:

```bash
mvn clean test
```

Execute somente os testes categorizados como Caso 1:

```bash
mvn test -Dgroups=caso1
```

Execute explicitamente a suíte JUnit Platform:

```bash
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
