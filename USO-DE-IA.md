# Relatório de Uso de Inteligência Artificial — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Finalidade:** Transparência sobre o uso de ferramentas de IA generativa conforme preconizado no enunciado da disciplina.

---

## 1. O que foi solicitado à IA
1. **Estruturação do Plano de Testes:** Auxílio na análise textual da especificação funcional (Anexo A) para identificação exaustiva das variáveis de entrada, partições de equivalência (classes válidas e inválidas) e valores de fronteira (análise de valor-limite).
2. **Construção da Suíte JUnit 5:** Apoio na codificação dos casos de teste utilizando boas práticas do JUnit 5 (como `@Nested`, `@DisplayName`, `@ParameterizedTest`, `@ValueSource`).
3. **Tabulação dos Defeitos:** Apoio no processamento automatizado dos relatórios do Surefire (`target/surefire-reports`) para tabular as discrepâncias entre especificação e execução real.
4. **Preparação de Slides para Arguição:** Elaboração de roteiro de slides em Markdown e HTML com mapas mentais das regras, casos de fronteira e possíveis perguntas de arguição individual.

---

## 2. O que foi aproveitado
- A organização modular da suíte de testes em classes aninhadas (`@Nested`) facilitou a rastreabilidade direta com o `PLANO-DE-TESTES.md`.
- A nomenclatura padronizada dos casos de teste (`CT-PD-xx`, `CT-DD-xx`, etc.) permitiu mapear com clareza cada asserção à respectiva classe de equivalência ou valor-limite.
- A tabulação no formato requerido pelo professor ("uma linha por falha") no `DEFEITOS.md`.
- A síntese pedagógica nos slides de apresentação, permitindo que todos os membros da equipe compreendam as justificativas conceituais de cada teste para a arguição individual.

---

## 3. O que teve de ser corrigido / ajustado pela equipe
- **Rigor com a Especificação vs Retorno do JAR:** A IA inicialmente poderia ser tentada a ajustar asserções para fazer os testes passarem. Reforçamos estritamente a regra de ouro do trabalho: *o resultado esperado vem sempre da especificação, nunca do que o sistema devolve. Uma falha de teste é um sucesso porque revela um defeito do software*.
- **Semântica de Fronteira em `podeRenovar`:** Foi necessário ajustar a interpretação de "renovam até 2 vezes": se `renovacoesFeitas == 2`, o usuário já consumiu suas 2 renovações permitidas, logo uma nova renovação deve ser recusada (`false`).
- **Comportamento de Dias da Semana em `dataDevolucao`:** Validação cuidadosa de que o sábado é dia normal (não posterga) e apenas o domingo é postergado para segunda-feira, testando explicitamente a transição sexta-sábado-domingo-segunda para todos os perfis de usuário.
- **Cálculo da Multa Escalonada e Carência:** Ajuste detalhado da contagem de dias cobrados para `PROFESSOR`, garantindo que os 3 primeiros dias de atraso não gerem valor e que a virada de alíquota (R$ 1,00 para R$ 2,00) incida a partir do 11º dia *cobrado*, respeitando o teto de R$ 50,00 por empréstimo.
- **Configuração do Ambiente Maven:** Ajuste do caminho do JDK 17+ e integração do plugin Surefire para execução consistente via linha de comando (`mvn test`).
