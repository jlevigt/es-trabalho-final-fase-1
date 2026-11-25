---
marp: true
theme: default
paginate: true
header: "Engenharia de Software — IFCE | Fase 1: Testes Caixa-Preta"
footer: "Projeto Biblioteca — Sistema de Testes"
---

# Teste de Software — Sistema de Biblioteca
## Fase 1: Guia de Arguição & Roteiro de Testes Caixa-Preta

**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia do Ceará (IFCE)  
**Disciplina:** Engenharia de Software  
**Data:** 24 de novembro de 2026  
**Repositório Oficial:** [`jlevigt/es-trabalho-final-fase-1`](https://github.com/jlevigt/es-trabalho-final-fase-1)

---

# 1. Fundamentos: Partição de Equivalência (PE) vs Análise de Valor-Limite (AVL)

### Partição de Equivalência (PE)
- Divide as entradas em classes onde o sistema deve apresentar o mesmo comportamento.
- **Classes Válidas:** Dados operacionais que seguem o fluxo de negócio.
- **Classes Inválidas:** Dados fora do domínio (nulos, negativos) que devem disparar exceção (`IllegalArgumentException`).
- **Critério:** Testa-se um elemento representativo qualquer de cada partição.

### Análise de Valor-Limite (AVL)
- Investiga as **fronteiras** entre partições ordenadas (numéricas ou cronológicas).
- Quase todos os bugs de programação decorrem de erros de fronteira (`<` vs `<=`, `+1` ou `-1`).
- **Critério:** Testa-se sistematicamente: (1) no limite exato, (2) logo antes, e (3) logo depois.

---

# 2. Panorama Geral: 84 Testes da Suíte

Os testes estão separados em arquivos dedicados por método em `src/test/java/br/edu/biblioteca/`:

| Arquivo de Teste | Método Sob Teste | Fundamentação Combinatória | Testes | Falhas Esperadas |
|---|---|---|---|---|
| `PrazoDiasTest.java` | `prazoDias` | 3 válidas + 1 inválida (null) | **4** | 1 |
| `DataDevolucaoTest.java` | `dataDevolucao` | (3 tipos &times; 3 dias calendário) + 3 nulos | **12** | 4 |
| `LimiteEmprestimosTest.java` | `limiteEmprestimos` | 3 válidas + 1 inválida (null) | **4** | 0 |
| `MultaCentavosTest.java` | `multaCentavos` | 3 inválidas + 9 Aluno + 10 Professor + 2 Visitante | **24** | 11 |
| `PodeEmprestarTest.java` | `podeEmprestar` | 3 inválidas + 7 Aluno + 2 Prof + 2 Vis | **14** | 6 |
| `PodeRenovarTest.java` | `podeRenovar` | 2 inválidas + 1 Vis + 4 Aluno + 3 Prof + 3 restrições | **13** | 2 |
| `MultaTotalCentavosTest.java` | `multaTotalCentavos` | 5 inválidas + 1 vazia + 3 unitárias + 4 múltiplos | **13** | 7 |
| **Total Geral** | — | — | **84** | **31** |

---

# 3. Método 1: `prazoDias(TipoUsuario tipo)`
**Arquivo:** `PrazoDiasTest.java`

### Regra da Especificação (Anexo A)
Devolve o prazo de empréstimo: 14 dias para Aluno, 30 para Professor e 7 para Visitante. Tipo nulo lança `IllegalArgumentException`.

### De onde saíram os testes?
- Domínio discreto do Enum (técnica 100% PE):
  - 3 partições válidas: `ALUNO`, `PROFESSOR`, `VISITANTE`.
  - 1 partição inválida: `null`.
- **Cálculo:** 3 válidas + 1 inválida = **4 testes**.

### Exemplo Objetivo de Verificação
```text
Entrada:  tipo = TipoUsuario.VISITANTE
Esperado: 7 dias
Obtido:   10 dias
```
- **Indicador:** 4 testes | 3 conformes | **1 falha esperada** (revela defeito na implementação de Visitante).

---

# 4. Método 2: `dataDevolucao(LocalDate retirada, TipoUsuario tipo)`
**Arquivo:** `DataDevolucaoTest.java`

### Regra da Especificação (Anexo A)
Soma o prazo do tipo à retirada. Se o resultado for domingo, a devolução passa para a segunda-feira seguinte. O sábado é dia normal. Retirada ou tipo nulos lançam `IllegalArgumentException`.

### De onde saíram os testes?
- Cada tipo (Aluno, Professor, Visitante) testado nos 3 dias-chave de calendário:
  - Dia útil (PE: mantém dia) &bull; Sábado normal (AVL: mantém sábado) &bull; Domingo (AVL: posterga para segunda)
- 3 nulos: retirada nula, tipo nulo, ambos nulos.
- **Cálculo:** (3 tipos &times; 3 dias) + 3 nulos = **12 testes**.

### Exemplo Objetivo de Verificação
```text
Entrada:  retirada = 01/05/2026 (sexta), tipo = PROFESSOR (+30 dias = 31/05/2026, domingo)
Esperado: 01/06/2026 (segunda-feira seguinte)
Obtido:   31/05/2026 (domingo mantido sem postergação)
```
- **Indicador:** 12 testes | 8 conformes | **4 falhas esperadas** (1 direta no domingo de Professor + 3 decorrentes de `prazoDias` em Visitante).

---

# 5. Método 3: `limiteEmprestimos(TipoUsuario tipo)`
**Arquivo:** `LimiteEmprestimosTest.java`

### Regra da Especificação (Anexo A)
Devolve o limite de empréstimos simultâneos: 3 para Aluno, 8 para Professor e 1 para Visitante. Tipo nulo lança `IllegalArgumentException`.

### De onde saíram os testes?
- Domínio discreto do Enum (técnica 100% PE):
  - 3 partições válidas: `ALUNO` (3), `PROFESSOR` (8), `VISITANTE` (1).
  - 1 partição inválida: `null`.
- **Cálculo:** 3 válidas + 1 inválida = **4 testes**.

### Exemplo Objetivo de Verificação (Conforme)
```text
Entrada:  tipo = TipoUsuario.PROFESSOR
Esperado: 8
Obtido:   8 (Conforme com a especificação)
```
- **Indicador:** 4 testes | **4 conformes** | **0 falhas** (método 100% em conformidade).

---

# 6. Método 4: `multaCentavos` — Regras e Matemática do Teto
**Arquivo:** `MultaCentavosTest.java`

### Regra da Especificação (Anexo A)
- Professores têm 3 dias de atraso isentos (carência). Demais pagam todos.
- Alíquotas: 1º ao 10º dia cobrado = R$ 1,00/dia. 11º em diante = R$ 2,00/dia.
- Teto: a multa nunca passa de R$ 50,00 (5000 centavos). Atraso negativo lança exceção.

### De onde saíram os 30 dias do teto? (Matemática da AVL)
Os dias 30 e 31 **não são prazo de empréstimo**, mas a fronteira exata do teto monetário de R$ 50,00:
- Primeiros 10 dias cobrados: 10 &times; R$ 1,00 = R$ 10,00 (1000 centavos).
- Faltam R$ 40,00 (4000 centavos) na 2ª faixa a R$ 2,00/dia.
- 40 / 2 = 20 dias na 2ª faixa.
- Total para atingir R$ 50,00: 10 + 20 = **30 dias de atraso** (para Aluno).
- Para Professor: 3 dias carência + 30 cobrados = **33 dias de atraso**.

---

# 7. Método 4: `multaCentavos` — Falhas Observadas
**Arquivo:** `MultaCentavosTest.java`

### Exemplos Objetivos de Verificação

**1. Fronteira de Carência (Professor):**
```text
Entrada:  tipo = PROFESSOR, diasAtraso = 3
Esperado: 0 centavos (limite da carência)
Obtido:   100 centavos
```

**2. Transição de Faixa no 11º Dia Cobrado:**
```text
Entrada:  tipo = ALUNO, diasAtraso = 11
Esperado: 1200 centavos (10 × 100 + 1 × 200)
Obtido:   1100 centavos
```

**3. Fronteira do Teto de R$ 50,00:**
```text
Entrada:  tipo = ALUNO, diasAtraso = 31
Esperado: 5000 centavos (teto de R$ 50,00)
Obtido:   5200 centavos
```

- **Indicador:** 24 testes | 13 conformes | **11 falhas esperadas** (carência, 11º dia, teto e negativo).

---

# 8. Método 5: `podeEmprestar(TipoUsuario, int, int, boolean)`
**Arquivo:** `PodeEmprestarTest.java`

### Regra da Especificação (Anexo A)
Retorna `true` SOMENTE SE: (1) `emprestimosAtivos < limite`, (2) `multaPendenteCentavos <= 1000` (R$ 10,00) e (3) `!possuiAtraso`. Negativos ou nulo lançam exceção.

### De onde saíram os testes?
- 3 inválidas + 7 Aluno (0, multas 999/1000/1001, ativos 2/3/4, atraso true) + 2 Professor + 2 Visitante = **14 testes**.

### Exemplos Objetivos de Verificação
```text
Entrada:  ALUNO, ativos = 3, multa = 0, atraso = false
Esperado: false (atingiu o limite de 3 empréstimos; a regra exige MENOS que o limite)
Obtido:   true
```

```text
Entrada:  ALUNO, ativos = 0, multa = 1001, atraso = false
Esperado: false (multa excede R$ 10,00)
Obtido:   true
```

- **Indicador:** 14 testes | 8 conformes | **6 falhas esperadas** (ativos no limite, tolerância de multa, atraso e negativos).

---

# 9. Método 6: `podeRenovar(TipoUsuario, int, boolean, boolean)`
**Arquivo:** `PodeRenovarTest.java`

### Regra da Especificação (Anexo A)
- Visitantes nunca renovam.
- Alunos renovam até 2 vezes (feitas = 0 e 1 podem renovar; 2 já atingiu o teto).
- Professores renovam até 3 vezes (feitas = 0, 1 e 2 podem; 3 já atingiu o teto).
- Só renova se obra não tiver reserva e não estiver em atraso.

### De onde saíram os testes?
- 2 inválidas + 1 Visitante + 4 Aluno (0, 1, 2, 3) + 3 Professor (0, 2, 3) + 3 restrições = **13 testes**.

### Exemplos Objetivos de Verificação
```text
Entrada:  VISITANTE, renovacoes = 0, reserva = false, atraso = false
Esperado: false (visitante nunca renova)
Obtido:   true
```

```text
Entrada:  PROFESSOR, renovacoes = 3, reserva = false, atraso = false
Esperado: false (já renovou 3 vezes, atingiu o teto)
Obtido:   true
```

- **Indicador:** 13 testes | 11 conformes | **2 falhas esperadas** (permissão indevida para Visitante e 4ª renovação de Professor).

---

# 10. Método 7: `multaTotalCentavos(TipoUsuario, int[])`
**Arquivo:** `MultaTotalCentavosTest.java`

### Regra da Especificação (Anexo A)
Soma a multa individual de cada empréstimo. O total nunca passa de R$ 100,00 (10000 centavos). Lista vazia dá 0. Elemento negativo ou lista nula lança exceção.

### De onde saíram os testes?
- 5 inválidas (nulos e negativos) + 1 lista vazia + 3 unitárias + 4 múltiplos empréstimos = **13 testes**.

### Exemplos Objetivos de Verificação
```text
Entrada:  ALUNO, diasAtraso = [5] (1 empréstimo)
Esperado: 500 centavos
Obtido:   0 centavos
```

```text
Entrada:  ALUNO, diasAtraso = [10, 10]
Esperado: 2000 centavos (1000 + 1000)
Obtido:   1000 centavos (primeiro elemento ignorado)
```

- **Indicador:** 13 testes | 6 conformes | **7 falhas esperadas** (primeiro elemento ignorado e ausência de exceção em negativos).

---

# 11. Reprodutibilidade de Ambiente

### Independência de Fuso Horário
- O método `dataDevolucao` manipula exclusivamente `java.time.LocalDate`.
- Sob a norma ISO-8601, `LocalDate` é uma data civil pura (Ano, Mês, Dia) sem componente horário. Operações de calendário (`plusDays`, `getDayOfWeek`) não sofrem alteração por time zone ou horário de verão.

### Valores Monetários Determinísticos
- Valores operam com inteiros em centavos (`int`), eliminando imprecisões de ponto flutuante e de formatação monetária regional (`Locale`).

### Configuração no `pom.xml`
- O `maven-surefire-plugin` fixa:
  `<user.timezone>UTC</user.timezone>` e `<argLine>-Dfile.encoding=UTF-8</argLine>`.

---

# 12. Conclusão & Preparação para a Arguição

### Entregáveis da Fase 1 Concluídos:
- Repositório Git público no GitHub: [`jlevigt/es-trabalho-final-fase-1`](https://github.com/jlevigt/es-trabalho-final-fase-1).
- Suíte de 84 testes automatizados com arquivos separados por método.
- Documentação técnica completa: `PLANO-DE-TESTES.md`, `DEFEITOS.md` e `USO-DE-IA.md`.

### Raciocínio para a Arguição:
- Cada integrante sabe apontar a origem de cada teste (PE ou AVL).
- Conhecimento exato das fronteiras numéricas (carência de 3 dias, alíquota no 11º dia e tetos de R$ 50,00 e R$ 100,00).
- Preparados para variações hipotéticas de regras que o professor possa questionar.
