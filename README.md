# Projeto de Teste de Software — Sistema de Biblioteca

**Disciplina:** Engenharia de Software  
**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia do Ceará (IFCE)  
**Entrega:** Fase 1 — Teste Caixa-Preta (Partição de Equivalência e Análise de Valor-Limite)  
**Data:** 24 de novembro de 2026  

---

## 📋 Visão Geral da Entrega

Nesta primeira fase, a equipe recebeu exclusivamente a especificação funcional (**Anexo A do Enunciado**) e o sistema compilado (`lib/biblioteca-1.0.jar`), sem acesso ao código-fonte.

A suíte de testes foi concebida por técnicas rigorosas de **Partição de Equivalência (PE)** e **Análise de Valor-Limite (AVL)**. Conforme a regra de ouro do enunciado, o resultado esperado deriva sempre da especificação: uma falha em teste automatizado contra o JAR defeituoso comprova o sucesso da suíte em revelar um defeito do software.

---

## 📁 Estrutura dos Arquivos de Entrega

- [`src/test/java/br/edu/biblioteca/`](src/test/java/br/edu/biblioteca/): Suíte de testes com **86 casos de teste** em JUnit 5 organizados em arquivos dedicados por método (`PrazoDiasTest.java`, `DataDevolucaoTest.java`, `LimiteEmprestimosTest.java`, `MultaCentavosTest.java`, `PodeEmprestarTest.java`, `PodeRenovarTest.java`, `MultaTotalCentavosTest.java`).
- [`PLANO-DE-TESTES.md`](PLANO-DE-TESTES.md): Mapeamento formal de cada método com suas classes de equivalência (válidas e inválidas), fronteiras de limites e rastreabilidade dos casos de teste (`CT-PD-xx` a `CT-MT-xx`).
- [`DEFEITOS.md`](DEFEITOS.md): Registro das **31 falhas reveladas**, seguindo estritamente o formato de *uma linha por falha* (Método, Entrada, Resultado Esperado, Resultado Obtido e Classe/Limite).
- [`USO-DE-IA.md`](USO-DE-IA.md): Relatório de transparência sobre o uso de assistentes de IA generativa, detalhando solicitações, conteúdo aproveitado e refinamentos conceituais aplicados pela equipe.
- [`SLIDES-APRESENTACAO.md`](SLIDES-APRESENTACAO.md): Slides da apresentação da Fase 1 em formato Markdown (compatível com Marp).
- [`slides.html`](slides.html): Apresentação interativa em HTML/CSS/JS (100% responsiva e navegável offline com setas do teclado).

---

## ⚙️ Como Executar a Suíte de Testes

### Requisitos:
- **JDK 17** ou superior instalado
- **Apache Maven 3.8+**

### Comando de Execução:
```bash
mvn clean test
```

### Resultados Esperados:
```text
Total de testes executados: 86
Testes em conformidade:     55
Falhas esperadas (defeitos): 31
Resultado final do build:   BUILD SUCCESS (testFailureIgnore ativado)
```

Os relatórios detalhados da execução do JUnit são gerados automaticamente na pasta `target/surefire-reports/`.

---

## 🌐 Reprodutibilidade de Ambiente

A suíte é determinística e imune a discrepâncias de relógio de máquina ou formatação regional:
1. **Datas Isoladas:** O método `dataDevolucao` manipula estritamente `java.time.LocalDate` (calendário civil ISO-8601 sem componente horário ou fuso horário).
2. **Moeda em Centavos:** Valores monetários são inteiros primitivos (`int` em centavos: 1000 = R$ 10,00), eliminando imprecisões de ponto flutuante e de formatação monetária (`Locale`).
3. **Blindagem no Surefire (`pom.xml`):** As propriedades de sistema `<user.timezone>UTC</user.timezone>` e `<file.encoding>UTF-8</file.encoding>` estão fixadas para garantir execução homogênea em qualquer sistema operacional.
