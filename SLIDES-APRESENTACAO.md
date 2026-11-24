---
marp: true
theme: default
paginate: true
header: "Engenharia de Software — IFCE | Fase 1: Testes Caixa-Preta"
footer: "Projeto Biblioteca — Sistema de Testes"
---

# Teste de Software — Sistema de Biblioteca
## Fase 1: Testes Caixa-Preta (Partição de Equivalência e Análise de Valor-Limite)

**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia do Ceará (IFCE)  
**Disciplina:** Engenharia de Software  
**Data:** 24 de novembro de 2026  
**Repositório Oficial:** `jlevigt/es-trabalho-final-fase-1`

---

# 1. Fundamentação e Escopo de Caixa-Preta

- **Diretriz Metodológica:**
  - Na Fase 1, a suíte de testes é elaborada exclusivamente a partir da especificação funcional (**Anexo A do Enunciado**), sem acesso ao código-fonte.
  - O sistema é avaliado exclusivamente como uma caixa-preta via interface pública compilada (`lib/biblioteca-1.0.jar`).
- **Critério de Avaliação das Falhas:**
  - O resultado esperado é definido estritamente pela especificação.
  - A ocorrência de falhas em testes automatizados contra um sistema com defeitos conhecidos constitui o **resultado esperado**, comprovando a capacidade da suíte em identificar divergências funcionais.
- **Configuração de Execução:**
  - O `pom.xml` opera com `<testFailureIgnore>true</testFailureIgnore>`, permitindo a execução integral da suíte e a geração do relatório do Surefire.

---

# 2. Distribuição da Suíte de Testes (91 Casos)

A suíte completa totaliza **91 casos de teste**, distribuídos e fundamentados combinatorialmente entre os métodos:

| Método Sob Teste | Parâmetros | Fórmula de Partições e Limites | Total | Falhas Reveladas |
|---|---|---|---|---|
| `prazoDias` | 1 | $3 \text{ válidas} + 1 \text{ inválida}$ | **4** | 1 |
| `dataDevolucao` | 2 | $(3 \text{ tipos} \times 3 \text{ dias}) + 2 \text{ calendário} + 3 \text{ nulos}$ | **14** | 5 |
| `limiteEmprestimos` | 1 | $3 \text{ válidas} + 1 \text{ inválida}$ | **4** | 0 |
| `multaCentavos` | 2 | $4 \text{ inválidas} + 10 \text{ Aluno} + 11 \text{ Professor} + 2 \text{ Visitante}$ | **27** | 13 |
| `podeEmprestar` | 4 | $3 \text{ inválidas} + 7 \text{ Aluno} + 2 \text{ Prof} + 2 \text{ Vis} + 1 \text{ mult}$ | **15** | 6 |
| `podeRenovar` | 4 | $2 \text{ inválidas} + 1 \text{ Vis} + 4 \text{ Aluno} + 3 \text{ Prof} + 3 \text{ restrições}$ | **13** | 2 |
| `multaTotalCentavos` | 2 | $5 \text{ inválidas} + 1 \text{ vazia} + 3 \text{ unitárias} + 4 \text{ múltiplos}$ | **13** | 7 |
| `ExemploTest` | — | Caso inicial de demonstração | **1** | 0 |
| **Total Geral** | — | — | **91** | **34** |

---

# 3. Método: `prazoDias(TipoUsuario tipo)`

### 1. Regra da Especificação (Anexo A)
Devolve o prazo de empréstimo: 14 dias para Aluno, 30 para Professor e 7 para Visitante. Tipo nulo lança `IllegalArgumentException`.

### 2. Variáveis & Multiplicação dos Casos
- Variável `tipo` (Enum TipoUsuario):
  - 3 partições válidas: `ALUNO` (14), `PROFESSOR` (30), `VISITANTE` (7).
  - 1 partição inválida: `null` (deve lançar exceção).
- **Cálculo:** $3 \text{ válidas} + 1 \text{ inválida} = \mathbf{4\text{ testes}}$.

### 3. Exemplo Objetivo de Verificação
```text
Entrada:  tipo = TipoUsuario.VISITANTE
Esperado: 7 dias
Obtido:   10 dias
```

### 4. Indicador de Conformidade
- Total: **4 testes** | 3 conformes | **1 falha esperada** (revela não conformidade da regra para Visitante).

---

# 4. Método: `dataDevolucao(LocalDate retirada, TipoUsuario tipo)`

### 1. Regra da Especificação (Anexo A)
Soma o prazo do tipo à data de retirada. Se o resultado cair num domingo, a devolução passa para a segunda-feira seguinte. O sábado é dia normal. Data de retirada ou tipo nulos lançam `IllegalArgumentException`.

### 2. Variáveis & Multiplicação dos Casos
- Combinação de `tipo` (3) com dias da semana calculados (3: dia útil, sábado, domingo): $3 \times 3 = 9\text{ testes}$.
- Fronteiras de calendário: virada de mês (1) e virada de ano (1): $2\text{ testes}$.
- Partições inválidas: retirada nula (1) + tipo nulo (1) + ambos nulos (1): $3\text{ testes}$.
- **Cálculo:** $(3 \times 3) + 2 + 3 = \mathbf{14\text{ testes}}$.

### 3. Exemplo Objetivo de Verificação
```text
Entrada:  retirada = 01/05/2026 (sexta-feira), tipo = PROFESSOR (+30 dias = 31/05/2026, domingo)
Esperado: 01/06/2026 (segunda-feira seguinte)
Obtido:   31/05/2026 (domingo mantido sem postergação)
```

### 4. Indicador de Conformidade
- Total: **14 testes** | 9 conformes | **5 falhas esperadas** (1 falha direta em domingo para Professor e 4 falhas decorrentes do prazo divergente de Visitante).

---

# 5. Método: `limiteEmprestimos(TipoUsuario tipo)`

### 1. Regra da Especificação (Anexo A)
Devolve o limite de empréstimos simultâneos: 3 para Aluno, 8 para Professor e 1 para Visitante. Tipo nulo lança `IllegalArgumentException`.

### 2. Variáveis & Multiplicação dos Casos
- Variável `tipo` (Enum TipoUsuario):
  - 3 partições válidas: `ALUNO` (3), `PROFESSOR` (8), `VISITANTE` (1).
  - 1 partição inválida: `null` (deve lançar exceção).
- **Cálculo:** $3 \text{ válidas} + 1 \text{ inválida} = \mathbf{4\text{ testes}}$.

### 3. Exemplo Objetivo de Verificação
```text
Entrada:  tipo = TipoUsuario.PROFESSOR
Esperado: 8
Obtido:   8 (Conforme)
```

### 4. Indicador de Conformidade
- Total: **4 testes** | **4 conformes** | **0 falhas** (método plenamente conforme à especificação).

---

# 6. Método: `multaCentavos(TipoUsuario, int)` — Regras e Casos

### 1. Regra da Especificação (Anexo A)
- Professores têm 3 dias de carência (não cobrados). Demais tipos pagam todos os dias.
- Alíquotas: 1º ao 10º dia cobrado = R$ 1,00/dia (100 centavos); 11º dia cobrado em diante = R$ 2,00/dia (200 centavos).
- Teto: a multa nunca ultrapassa R$ 50,00 (5000 centavos).
- Dias de atraso negativos ou tipo nulo lançam `IllegalArgumentException`.

### 2. Variáveis & Multiplicação dos Casos
- Partições inválidas: atraso negativo (-1 e -50), tipo nulo e ambos inválidos: **4 testes**.
- Aluno (sem carência): 0 atraso, limites faixa 1 (1, 5, 10), limites faixa 2 (11, 12, 29) e limites teto (30, 31, 100): **10 testes**.
- Professor (com carência): 0 atraso, carência (1, 2, 3), limites faixa 1 (4, 8, 13), limites faixa 2 (14, 32) e limites teto (33, 34): **11 testes**.
- Visitante: confirmação de equivalência às alíquotas sem carência (1, 12 dias): **2 testes**.
- **Cálculo:** $4 + 10 + 11 + 2 = \mathbf{27\text{ testes}}$.

---

# 7. Método: `multaCentavos(TipoUsuario, int)` — Falhas Observadas

### Exemplos Objetivos de Verificação

**Exemplo 1 (Fronteira de Carência de Professor):**
```text
Entrada:  tipo = PROFESSOR, diasAtraso = 3
Esperado: 0 centavos (dentro da carência de 3 dias)
Obtido:   100 centavos
```

**Exemplo 2 (Fronteira de Alíquota no 11º Dia Cobrado):**
```text
Entrada:  tipo = ALUNO, diasAtraso = 11
Esperado: 1200 centavos (10 × 100 + 1 × 200)
Obtido:   1100 centavos
```

**Exemplo 3 (Teto Máximo por Empréstimo):**
```text
Entrada:  tipo = ALUNO, diasAtraso = 31
Esperado: 5000 centavos (teto de R$ 50,00)
Obtido:   5200 centavos
```

### Indicador de Conformidade
- Total: **27 testes** | 14 conformes | **13 falhas esperadas** (carência divergente, transição no 11º dia, ausência de teto e ausência de exceção em atraso negativo).

---

# 8. Método: `podeEmprestar(TipoUsuario, int, int, boolean)`

### 1. Regra da Especificação (Anexo A)
Devolve `true` SOMENTE SE as três condições valerem juntas:
1. `emprestimosAtivos < limiteEmprestimos(tipo)`
2. `multaPendenteCentavos <= 1000` (máximo R$ 10,00)
3. `!possuiAtraso` (não possui empréstimo em atraso)
Valores negativos ou tipo nulo lançam `IllegalArgumentException`.

### 2. Variáveis & Multiplicação dos Casos
- 4 variáveis de entrada: `tipo`, `emprestimosAtivos`, `multaPendenteCentavos`, `possuiAtraso`.
- Partições inválidas (ativos negativos, multa negativa, tipo nulo): **3 testes**.
- Aluno (limite 3): zerado, multa 999, ativos 2 e multa 1000, ativos 3, ativos 4, multa 1001, com atraso: **7 testes**.
- Professor (limite 8): 7 ativos (válido), 8 ativos (no limite): **2 testes**.
- Visitante (limite 1): 0 ativos (válido), 1 ativo (no limite): **2 testes**.
- Combinação com múltiplas violações simultâneas: **1 teste**.
- **Cálculo:** $3 + 7 + 2 + 2 + 1 = \mathbf{15\text{ testes}}$.

---

# 9. Método: `podeEmprestar(...)` — Falhas Observadas

### Exemplo Objetivo de Verificação
```text
Entrada:  tipo = ALUNO, emprestimosAtivos = 3, multaPendenteCentavos = 0, possuiAtraso = false
Esperado: false (o usuário atingiu o limite de 3 empréstimos; a regra exige estritamente MENOS)
Obtido:   true
```

```text
Entrada:  tipo = ALUNO, emprestimosAtivos = 0, multaPendenteCentavos = 1001, possuiAtraso = false
Esperado: false (multa de R$ 10,01 excede o teto de R$ 10,00)
Obtido:   true
```

```text
Entrada:  tipo = ALUNO, emprestimosAtivos = 0, multaPendenteCentavos = 0, possuiAtraso = true
Esperado: false (possui atraso registrado)
Obtido:   true
```

### Indicador de Conformidade
- Total: **15 testes** | 9 conformes | **6 falhas esperadas** (fronteiras de limite de empréstimos, tolerância de multa, atraso e valores negativos).

---

# 10. Método: `podeRenovar(TipoUsuario, int, boolean, boolean)`

### 1. Regra da Especificação (Anexo A)
Visitantes nunca renovam. Alunos renovam até 2 vezes (`renovacoesFeitas = 0, 1`). Professores renovam até 3 vezes (`renovacoesFeitas = 0, 1, 2`). Em qualquer caso, só renova se `!possuiReserva` e `!emAtraso`. Negativo lança exceção.

### 2. Variáveis & Multiplicação dos Casos
- Partições inválidas (renovações < 0, tipo nulo): **2 testes**.
- Perfil Visitante (tentativa de renovação inicial): **1 teste**.
- Aluno (fronteiras de renovações 0, 1, 2 [teto], 3 [acima]): **4 testes**.
- Professor (fronteiras de renovações 0, 2, 3 [teto]): **3 testes**.
- Restrições combinadas (com reserva, em atraso, com reserva e atraso): **3 testes**.
- **Cálculo:** $2 + 1 + 4 + 3 + 3 = \mathbf{13\text{ testes}}$.

### 3. Exemplo Objetivo de Verificação
```text
Entrada:  tipo = VISITANTE, renovacoesFeitas = 0, possuiReserva = false, emAtraso = false
Esperado: false (a especificação determina expressamente que visitantes nunca renovam)
Obtido:   true
```

### 4. Indicador de Conformidade
- Total: **13 testes** | 11 conformes | **2 falhas esperadas** (permissão indevida para Visitante e 4ª renovação permitida para Professor).

---

# 11. Método: `multaTotalCentavos(TipoUsuario, int[])`

### 1. Regra da Especificação (Anexo A)
Soma a multa de cada empréstimo da lista (cada uma calculada conforme `multaCentavos`). O total nunca ultrapassa R$ 100,00 (10000 centavos). Lista vazia devolve 0. Lista nula, elemento negativo ou tipo nulo lançam exceção.

### 2. Variáveis & Multiplicação dos Casos
- Partições inválidas (tipo nulo com lista, tipo nulo vazia, lista nula, elemento [-1], elemento interno negativo): **5 testes**.
- Lista vazia (caso especial da especificação): **1 teste**.
- Listas unitárias (tamanho 1): sem atraso [0], com atraso [5], com teto individual [40]: **3 testes**.
- Múltiplos empréstimos: soma regular [10, 10], carência professor [3, 4], teto global exato [30, 30], teto excedido [30, 30, 30]: **4 testes**.
- **Cálculo:** $5 + 1 + 3 + 4 = \mathbf{13\text{ testes}}$.

### 3. Exemplo Objetivo de Verificação
```text
Entrada:  tipo = ALUNO, diasAtrasoPorEmprestimo = [5]
Esperado: 500 centavos
Obtido:   0 centavos
```

### 4. Indicador de Conformidade
- Total: **13 testes** | 6 conformes | **7 falhas esperadas** (não processamento de elemento unitário / primeiro elemento e ausência de exceção para elementos negativos).

---

# 12. Reprodutibilidade de Ambiente e Robustez

- **Independência de Fuso Horário (Timezone):**
  - O método `dataDevolucao` utiliza `java.time.LocalDate`, que opera exclusivamente sobre datas civis (Ano, Mês, Dia) sob o padrão ISO-8601.
  - Aritmética de dias e avaliação de dias da semana não possuem componente horário, sendo imunes a variações de fuso horário local ou horário de verão.
- **Valores Monetários Determinísticos:**
  - Todas as transações financeiras operam com inteiros em centavos (`int`), eliminando imprecisões de arredondamento de ponto flutuante e independendo de configurações de formatação monetária regional (`Locale`).
- **Garantia Adicional no `pom.xml`:**
  - O plugin Surefire foi configurado explicitamente com:
    `<user.timezone>UTC</user.timezone>` e `<file.encoding>UTF-8</file.encoding>`.

---

# 13. Conclusão da Fase 1

- **Entregáveis da Fase 1 Concluídos:**
  - Repositório Git público oficial: [`jlevigt/es-trabalho-final-fase-1`](https://github.com/jlevigt/es-trabalho-final-fase-1).
  - Suíte de testes automatizada cobrindo todas as classes de equivalência e valores-limite dos 7 métodos.
  - Documentação formal entregue: `PLANO-DE-TESTES.md`, `DEFEITOS.md`, `USO-DE-IA.md` e slides de apoio.
- **Prontidão para a Arguição:**
  - Todo o raciocínio fundamentado estritamente na especificação funcional (Anexo A).
  - Cada teste e falha com justificativa rastreável e objetiva.
