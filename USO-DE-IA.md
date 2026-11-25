# Relatório de Uso de Inteligência Artificial — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Finalidade:** Transparência sobre o uso de ferramentas de IA generativa conforme preconizado no enunciado da disciplina.

---

## 1. O que foi solicitado à IA
1. **Estruturação do Plano de Testes:** Auxílio na análise textual da especificação funcional (Anexo A) para identificação exaustiva das variáveis de entrada, partições de equivalência (classes válidas e inválidas) e valores de fronteira (análise de valor-limite).
2. **Construção da Suíte JUnit 5:** Apoio na codificação dos casos de teste utilizando boas práticas do JUnit 5 (`@DisplayName`, `@ParameterizedTest`, `@ValueSource`, asserções determinísticas).
3. **Tabulação dos Defeitos:** Apoio no processamento estruturado dos relatórios do Surefire (`target/surefire-reports`) para tabular as discrepâncias entre especificação e execução real.
4. **Preparação de Slides para Arguição:** Elaboração de roteiro de slides em Markdown e HTML com mapas conceituais das regras, casos de fronteira e possíveis perguntas de arguição individual.

---

## 2. O que foi aproveitado
- A nomenclatura padronizada e unificada dos casos de teste (`CT-PD-xx`, `CT-DD-xx`, `CT-MC-xx`, etc.), permitindo mapear com clareza cada asserção à respectiva partição de equivalência ou valor-limite.
- A tabulação no formato requerido pelo professor ("uma linha por falha") no `DEFEITOS.md`.
- A síntese pedagógica nos slides de apresentação, servindo como material de estudo interno para os membros da equipe justificarem os fundamentos de cada teste na arguição individual.
- A configuração determinística do Maven Surefire com encoding UTF-8 e timezone UTC para reprodutibilidade estrita em qualquer ambiente.

---

## 3. O que teve de ser corrigido / ajustado pela equipe
- **Eliminação de Especulações de Código Interno (Caixa-Branca):** A IA gerou inicialmente comentários com hipóteses sobre falhas internas do código do JAR (como "laço iniciando em `i = 1`"). A equipe vetou imediatamente qualquer menção estrutural, preservando a disciplina estrita de **caixa-preta** exigida na Fase 1 (o resultado esperado vem sempre e exclusivamente do Anexo A).
- **Modularização da Suíte em Arquivos por Método:** A IA havia centralizado todos os testes em um arquivo monolítico. A equipe exigiu a divisão em 7 classes Java dedicadas (`PrazoDiasTest.java`, `DataDevolucaoTest.java`, etc.), facilitando a navegação na IDE durante a arguição presencial.
- **Poda de Testes Artificiais Fora da Especificação:** A IA havia criado testes de fronteira de "virada de mês" e "virada de ano" para `dataDevolucao`. A equipe identificou que o método utiliza `java.time.LocalDate` (calendário civil ISO-8601 padrão) e que tais testes não correspondiam a fronteiras funcionais da especificação, removendo-os.
- **Correção Conceitual de Técnicas (PE vs AVL):** A equipe corrigiu etiquetas incorretas geradas pela IA em `multaTotalCentavos`: entradas como `[40]` (além do teto individual) e `[30, 30, 30]` (além do teto global) foram reclassificadas de `[AVL]` para `[PE]`, visto que representam pontos no interior de partições equivalentes, e não limites adjacentes imediatos.
- **Remoção de Duplicatas e Casos Compostos:** A equipe eliminou o teste de demonstração `ExemploTest` (redundante com `CT-PD-01`) e o caso `CT-PE-15` (que combinava múltiplas violações simultâneas em `podeEmprestar`), garantindo ortogonalidade aos testes para evitar mascaramento de falhas.
- **Semântica de Fronteira em `podeRenovar`:** Ajuste da interpretação de "renovam até 2 vezes": se `renovacoesFeitas == 2`, o usuário já consumiu suas 2 renovações permitidas, logo uma nova renovação deve ser recusada (`false`).
- **Unificação Rigorosa dos Identificadores:** Ajuste dos prefixos em `DEFEITOS.md` para garantir correspondência 1:1 (`CT-XX-NN`) com a suíte JUnit e o `PLANO-DE-TESTES.md`.
