# Relatório de Uso de Inteligência Artificial — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Finalidade:** Documentação detalhada e transparente sobre o uso de ferramentas de IA generativa, detalhando as solicitações realizadas, os componentes aproveitados e, prioritariamente, todas as correções e decisões técnicas conduzidas criticamente pela equipe.

---

## 1. O que foi solicitado à IA
1. **Estruturação do Plano de Testes:** Auxílio na decomposição da especificação funcional (Anexo A) em variáveis de entrada, partições de equivalência (classes válidas e inválidas) e fronteiras para Análise de Valor-Limite (AVL).
2. **Construção da Suíte JUnit 5:** Apoio na geração de código repetitivo de asserções, anotações do JUnit 5 (`@DisplayName`, `@ParameterizedTest`, `@ValueSource`) e manipulação de tipos da API padrão do Java (`java.time.LocalDate`).
3. **Tabulação de Falhas do Surefire:** Extração estruturada dos relatórios de execução (`target/surefire-reports`) para geração do `DEFEITOS.md` no formato exigido pelo professor (*uma linha por falha*).
4. **Criação de Slides de Apoio para a Arguição:** Elaboração de roteiro de slides em HTML e Markdown como material de estudo e síntese para a arguição individual presencial.
5. **Análise de Portabilidade e Reprodutibilidade:** Verificação de potenciais dependências de ambiente (como fuso horário/timezone e codificação de caracteres) na execução dos testes.

---

## 2. O que foi aproveitado
- **Aceleração da Escrita Mecânica dos Testes:** Redução de tempo na digitação repetitiva de métodos de teste parametrizados e blocos de asserção do JUnit 5.
- **Configuração Determinística no Maven:** Definição explícita de `<argLine>-Dfile.encoding=UTF-8</argLine>` e `<user.timezone>UTC</user.timezone>` no `pom.xml`, assegurando que a suíte rode exatamente igual em qualquer sistema operacional ou configuração regional de JVM.
- **Estruturação Tabular:** As tabelas consolidadas em Markdown para o `PLANO-DE-TESTES.md` e o `DEFEITOS.md`.
- **Esqueleto Visual dos Slides:** A base em HTML/CSS para apresentação interativa rápida sem dependência de softwares de terceiros.

---

## 3. O que teve de ser corrigido / ajustado pela equipe (Intervenção Crítica Humana)

O papel ativo da equipe foi indispensável para evitar que erros de interpretação, alucinações e vícios da IA comprometessem a entrega:

1. **Veto Rigoroso a Especulações de Caixa-Branca:**
   - *Erro da IA:* A IA inicialmente tentou explicar a causa das falhas inferindo defeitos de implementação interna do JAR decompilado (ex.: "O Defeito Estrutural Estarrecedor: Loop iniciando em `i = 1`").
   - *Correção da Equipe:* A equipe vetou sumariamente qualquer menção à estrutura interna do código. O trabalho é estritamente caixa-preta: o resultado esperado deriva exclusivamente do Anexo A e a causa da falha no código é escopo da Fase 2.

2. **Modularização Arquitetural da Suíte (Arquivos por Método):**
   - *Erro da IA:* A IA gerou a princípio um único arquivo monolítico de testes (`RegrasBibliotecaTest.java`).
   - *Correção da Equipe:* A equipe exigiu a refatoração completa em 7 arquivos Java independentes (`PrazoDiasTest.java`, `DataDevolucaoTest.java`, etc.). Essa decisão é estratégica para a arguição: quando o professor questionar um método específico, a equipe abre diretamente a classe correspondente na IDE.

3. **Poda de Testes Artificiais Fora da Especificação:**
   - *Erro da IA:* A IA inseriu testes de fronteira de "virada de mês" e "virada de ano" em `dataDevolucao`.
   - *Correção da Equipe:* A equipe constatou que `dataDevolucao` manipula `java.time.LocalDate` (calendário civil ISO-8601 padrão) e que não há regras de negócio no Anexo A relacionadas a transições de mês ou ano (apenas dias da semana: sábado normal, domingo postergado para segunda). Esses testes artificiais foram eliminados.

4. **Fundamentação Matemática das Fronteiras de Multa (30/31 e 33/34 dias):**
   - *Intervenção da Equipe:* A equipe exigiu a justificativa técnica exata de onde saíram os testes com 30 e 31 dias em `multaCentavos`. O método recebe apenas um inteiro (`int diasAtraso`), sem envolver datas. A equipe demonstrou a derivação formal do teto de R$ 50,00 (5000 centavos):
     $$\text{Faixa 1: } 10 \text{ dias} \times \text{R\$} 1,00 = \text{R\$} 10,00 \text{ (1000 centavos)}$$
     $$\text{Faixa 2: } \text{Restam R\$} 40,00 \div \text{R\$} 2,00/\text{dia} = 20 \text{ dias}$$
     $$\text{Fronteira Exata do Teto (Aluno): } 10 + 20 = \mathbf{30\text{ dias}} \quad (\text{AVL: } 30 \text{ e } 31)$$
     $$\text{Fronteira para Professor (com 3 dias de carência): } 3 + 30 = \mathbf{33\text{ dias}} \quad (\text{AVL: } 33 \text{ e } 34)$$

5. **Correção Conceitual de Técnicas (PE vs AVL):**
   - *Erro da IA:* A IA rotulou como `[AVL]` testes de `multaTotalCentavos` cujos valores estavam no interior de classes de equivalência (ex.: `[40]` para teto individual e `[30, 30, 30]` gerando 15000 centavos para o teto global).
   - *Correção da Equipe:* Como 40 e 15000 não são valores limites adjacentes imediatos, foram corretamente reclassificados como **`[PE]`** (representantes válidos de partição).

6. **Eliminação de Redundâncias e Casos Compostos:**
   - *Correção da Equipe:* A equipe removeu o teste redundante `ExemploTest.java` (duplicação de `CT-PD-01`) e o caso `CT-PE-15` (que testava simultaneamente ativos no limite, multa excedente e atraso ativo). Pelo princípio de Glenford Myers (ortogonalidade em testes de caixa-preta), condições inválidas devem ser testadas isoladamente para evitar mascaramento mútuo de defeitos.

7. **Rastreabilidade e Unificação de Identificadores (Padrão 1:1):**
   - *Erro da IA:* A IA usava prefixos inconsistentes (`CE-` em `DEFEITOS.md` e `CT-` no código e no `PLANO-DE-TESTES.md`), com numeração divergente em `dataDevolucao`.
   - *Correção da Equipe:* Todos os identificadores foram sincronizados sob o formato unificado **`CT-XX-NN`**, e etiquetas `[PE]` ou `[AVL]` foram inseridas em 100% dos `@DisplayName` da suíte.

8. **Reformulação Visual e de Usabilidade dos Slides:**
   - *Erro da IA:* A IA havia gerado slides com LaTeX cru não renderizado (`$(3 \times 3)...$`), baixa legibilidade ("cinza no cinza") e azul fluorescente incômodo.
   - *Correção da Equipe:* A equipe solicitou a reconstrução do design para uma identidade visual de alto contraste no padrão GitHub Dark (`#0d1117`, `#161b22`, texto `#f0f6fc` e títulos `#58a6ff`), fórmulas em texto/HTML limpo, cards padronizados no formato objetivo (*Entrada / Esperado / Obtido*) e caixas com dicas conceituais para a arguição presencial.

9. **Preservação e Isolamento do Kit Original:**
   - *Decisão da Equipe:* A equipe garantiu que a pasta original compartilhada pelo professor ([`kit-fase1/`](file:///home/jlevigt/Documents/academico/ifce/cadeiras/es/trabalho-final/kit-fase1)) fosse preservada **100% intocada**, criando um repositório Git isolado e independente para a entrega oficial ([`es-trabalho-final-fase-1/`](file:///home/jlevigt/Documents/academico/ifce/cadeiras/es/trabalho-final/es-trabalho-final-fase-1)).

---

## 4. Conclusão sobre a Autoria e Domínio para a Arguição

O uso de IA no projeto cumpriu estritamente o papel de assistente de produtividade. Todas as decisões de modelagem de teste, oráculos de especificação, seleção de técnicas e arquitetura da suíte foram deliberadas, validadas e compreendidas pela equipe. Cada integrante está plenamente capacitado a justificar qualquer linha de teste ou documento entregue na arguição presencial perante o professor.
