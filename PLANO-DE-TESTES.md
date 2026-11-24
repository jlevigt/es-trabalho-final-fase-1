# Plano de Testes — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Técnicas Empregadas:** Partição de Equivalência (PE) e Análise de Valor-Limite (AVL).

---

## 1. Visão Geral e Critérios

Nesta fase de teste de **caixa-preta**, os casos de teste foram derivados estritamente da especificação funcional (**Anexo A do Enunciado**), sem acesso ao código-fonte da aplicação (`biblioteca-1.0.jar`).

### Técnicas Utilizadas:
1. **Partição de Equivalência (PE):** Divisão dos domínios de entrada e saída em classes equivalentes onde se presume que o sistema apresente comportamento análogo. Divididas em:
   - **Classes Válidas (CV):** Entradas esperadas e tratadas pelas regras de negócio.
   - **Classes Inválidas (CI):** Entradas fora do domínio ou em desconformidade (ex: nulos, negativos), onde se espera lançamento de exceções.
2. **Análise de Valor-Limite (AVL):** Teste sistemático das fronteiras das partições numéricas e temporais (ex: dia anterior, dia exato, dia posterior; valores `limite - 1`, `limite`, `limite + 1`).

---

## 2. Métodos e Especificação dos Testes

---

### Método 1: `public int prazoDias(TipoUsuario tipo)`

#### Regra da Especificação:
- Devolve o prazo de empréstimo:
  - `ALUNO`: 14 dias
  - `PROFESSOR`: 30 dias
  - `VISITANTE`: 7 dias
- Em todos os métodos, `tipo` nulo lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-PD-01 | Válida | Tipo ALUNO | `TipoUsuario.ALUNO` | `14` |
| CE-PD-02 | Válida | Tipo PROFESSOR | `TipoUsuario.PROFESSOR` | `30` |
| CE-PD-03 | Válida | Tipo VISITANTE | `TipoUsuario.VISITANTE` | `7` |
| CE-PD-04 | Inválida | Tipo nulo | `null` | `IllegalArgumentException` |

#### Valores-Limite:
- O domínio do enum é discreto com 3 elementos válidos e `null`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-PD-01 | `prazoAluno()` | CE-PD-01 |
| CT-PD-02 | `prazoProfessor()` | CE-PD-02 |
| CT-PD-03 | `prazoVisitante()` | CE-PD-03 |
| CT-PD-04 | `tipoNuloLancaExcecao()` | CE-PD-04 |

---

### Método 2: `public LocalDate dataDevolucao(LocalDate retirada, TipoUsuario tipo)`

#### Regra da Especificação:
- Soma o prazo do tipo à data de retirada (Aluno: 14 dias, Professor: 30 dias, Visitante: 7 dias).
- Se o resultado cair num domingo, a devolução passa para a segunda-feira seguinte.
- O sábado é dia normal (não posterga).
- `retirada` nula ou `tipo` nulo lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-DD-01 | Válida | Devolução calculada cai em dia útil (segunda a sexta) | `retirada` + prazo cai em quarta | Mesma data calculada |
| CE-DD-02 | Válida | Devolução calculada cai em sábado (dia normal) | `retirada` + prazo cai em sábado | Sábado mantido |
| CE-DD-03 | Válida | Devolução calculada cai em domingo | `retirada` + prazo cai em domingo | Posterga para segunda-feira |
| CE-DD-04 | Válida | Transição de mês e ano | Retirada em fim de mês/ano | Data no mês/ano subsequente |
| CE-DD-05 | Inválida | Data de retirada nula | `retirada = null`, tipo válido | `IllegalArgumentException` |
| CE-DD-06 | Inválida | Tipo de usuário nulo | `retirada` válida, `tipo = null` | `IllegalArgumentException` |
| CE-DD-07 | Inválida | Ambos nulos | `retirada = null, tipo = null` | `IllegalArgumentException` |

#### Valores-Limite:
- **Fronteira Sábado vs Domingo vs Segunda:**
  - `Sábado`: Fronteira imediata antes do domingo (deve permanecer inalterada).
  - `Domingo`: Fronteira crítica (deve adicionar +1 dia).
  - `Segunda-feira`: Fronteira imediata após o domingo (não deve sofrer postergação).
- **Fronteira de Calendário:** Virada de ano (31/12).

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-DD-01 | `alunoDiaUtil()` | CE-DD-01 (Aluno, quarta-feira) |
| CT-DD-02 | `alunoCaindoSabado()` | CE-DD-02, Limite Sábado (Aluno) |
| CT-DD-03 | `alunoCaindoDomingoPassaSegunda()` | CE-DD-03, Limite Domingo (Aluno) |
| CT-DD-04 | `professorDiaUtil()` | CE-DD-01 (Professor, quarta-feira) |
| CT-DD-05 | `professorCaindoSabado()` | CE-DD-02, Limite Sábado (Professor) |
| CT-DD-06 | `professorCaindoDomingoPassaSegunda()` | CE-DD-03, Limite Domingo (Professor) |
| CT-DD-07 | `visitanteDiaUtil()` | CE-DD-01 (Visitante, quinta-feira) |
| CT-DD-08 | `visitanteCaindoSabado()` | CE-DD-02, Limite Sábado (Visitante) |
| CT-DD-09 | `visitanteCaindoDomingoPassaSegunda()` | CE-DD-03, Limite Domingo (Visitante) |
| CT-DD-10 | `viradaDeMes()` | CE-DD-04, Limite virada de mês |
| CT-DD-11 | `viradaDeAno()` | CE-DD-04, Limite virada de ano |
| CT-DD-12 | `retiradaNulaLancaExcecao()` | CE-DD-05 |
| CT-DD-13 | `tipoNuloLancaExcecao()` | CE-DD-06 |
| CT-DD-14 | `ambosNulosLancaExcecao()` | CE-DD-07 |

---

### Método 3: `public int limiteEmprestimos(TipoUsuario tipo)`

#### Regra da Especificação:
- Devolve o total de empréstimos simultâneos permitidos:
  - `ALUNO`: 3
  - `PROFESSOR`: 8
  - `VISITANTE`: 1
- `tipo` nulo lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-LE-01 | Válida | Tipo ALUNO | `TipoUsuario.ALUNO` | `3` |
| CE-LE-02 | Válida | Tipo PROFESSOR | `TipoUsuario.PROFESSOR` | `8` |
| CE-LE-03 | Válida | Tipo VISITANTE | `TipoUsuario.VISITANTE` | `1` |
| CE-LE-04 | Inválida | Tipo nulo | `null` | `IllegalArgumentException` |

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-LE-01 | `limiteAluno()` | CE-LE-01 |
| CT-LE-02 | `limiteProfessor()` | CE-LE-02 |
| CT-LE-03 | `limiteVisitante()` | CE-LE-03 |
| CT-LE-04 | `tipoNuloLancaExcecao()` | CE-LE-04 |

---

### Método 4: `public int multaCentavos(TipoUsuario tipo, int diasAtraso)`

#### Regra da Especificação:
- Professores têm carência: 3 primeiros dias de atraso não são cobrados (0 centavos). Para os demais tipos, todos os dias de atraso são cobrados.
- Do 1º ao 10º dia cobrado: R$ 1,00/dia (100 centavos/dia).
- Do 11º dia cobrado em diante: R$ 2,00/dia (200 centavos/dia).
- Teto: A multa de um empréstimo nunca passa de R$ 50,00 (5000 centavos).
- Dias de atraso negativos (< 0) lançam `IllegalArgumentException`.
- `tipo` nulo lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-MC-01 | Válida | Sem atraso (0 dias) | `diasAtraso = 0` | `0` |
| CE-MC-02 | Válida | Professor na carência (1 a 3 dias) | `PROFESSOR, dias = 1, 2, 3` | `0` |
| CE-MC-03 | Válida | 1ª faixa de cobrança (1º ao 10º dia cobrado) | Aluno `dias = 1..10`, Prof `dias = 4..13` | `diasCobrados * 100` |
| CE-MC-04 | Válida | 2ª faixa de cobrança antes do teto | Aluno `dias = 11..29`, Prof `dias = 14..32` | `1000 + (diasCobrados - 10) * 200` |
| CE-MC-05 | Válida | No teto máximo de R$ 50,00 | Aluno `dias = 30`, Prof `dias = 33` | `5000` |
| CE-MC-06 | Válida | Acima do teto máximo | Aluno `dias > 30`, Prof `dias > 33` | `5000` |
| CE-MC-07 | Inválida | Dias de atraso negativo | `diasAtraso < 0` | `IllegalArgumentException` |
| CE-MC-08 | Inválida | Tipo nulo | `tipo = null` | `IllegalArgumentException` |

#### Valores-Limite:
- **Limite Carência Professor:**
  - `dias = 3` (fronteira superior da carência): 0 centavos.
  - `dias = 4` (1º dia cobrado): 100 centavos.
- **Limite Faixa 1 vs Faixa 2:**
  - Aluno:
    - `dias = 10` (10º dia cobrado, limite sup faixa 1): 1000 centavos.
    - `dias = 11` (11º dia cobrado, limite inf faixa 2): 1200 centavos.
  - Professor:
    - `dias = 13` (10º dia cobrado): 1000 centavos.
    - `dias = 14` (11º dia cobrado): 1200 centavos.
- **Limite Teto R$ 50,00 (5000 centavos):**
  - Aluno:
    - `dias = 29` (limite - 1): 4800 centavos.
    - `dias = 30` (limite exato): 5000 centavos.
    - `dias = 31` (limite + 1): 5000 centavos.
    - `dias = 100` (muito acima): 5000 centavos.
  - Professor:
    - `dias = 32` (29º dia cobrado): 4800 centavos.
    - `dias = 33` (30º dia cobrado, teto exato): 5000 centavos.
    - `dias = 34` (31º dia cobrado, teto aplicado): 5000 centavos.
- **Limite Negativo:** `dias = -1` (fronteira inválida) e `dias = 0` (fronteira válida).

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-MC-01 | `atrasoNegativoLancaExcecao()` | CE-MC-07, Limite `dias = -1` |
| CT-MC-02 | `atrasoNegativoGrandeLancaExcecao()` | CE-MC-07, `dias = -50` |
| CT-MC-03 | `tipoNuloLancaExcecao()` | CE-MC-08 |
| CT-MC-04 | `tipoNuloEAtrasoNegativoLancaExcecao()` | CE-MC-07 e CE-MC-08 |
| CT-MC-05 | `alunoSemAtraso()` | CE-MC-01, Limite `dias = 0` |
| CT-MC-06 | `alunoAtraso1Dia()` | CE-MC-03, Limite `dias = 1` |
| CT-MC-07 | `alunoAtraso5Dias()` | CE-MC-03, Meio faixa 1 |
| CT-MC-08 | `alunoAtraso10Dias()` | CE-MC-03, Limite `dias = 10` |
| CT-MC-09 | `alunoAtraso11Dias()` | CE-MC-04, Limite `dias = 11` |
| CT-MC-10 | `alunoAtraso12DiasExemploEnunciado()` | CE-MC-04, Exemplo enunciado |
| CT-MC-11 | `alunoAtraso29Dias()` | CE-MC-04, Limite `dias = 29` |
| CT-MC-12 | `alunoAtraso30DiasTetoExato()` | CE-MC-05, Limite `dias = 30` |
| CT-MC-13 | `alunoAtraso31DiasTetoAplicado()` | CE-MC-06, Limite `dias = 31` |
| CT-MC-14 | `alunoAtraso100DiasTetoAplicado()` | CE-MC-06, `dias = 100` |
| CT-MC-15 | `professorSemAtraso()` | CE-MC-01, Professor `dias = 0` |
| CT-MC-16 | `professorCarencia(int diasAtraso)` | CE-MC-02, Limite carência `1, 2, 3` |
| CT-MC-17 | `professorAtraso4Dias()` | CE-MC-03, Limite `dias = 4` |
| CT-MC-18 | `professorAtraso8Dias()` | CE-MC-03, Meio faixa 1 |
| CT-MC-19 | `professorAtraso13Dias()` | CE-MC-03, Limite `dias = 13` |
| CT-MC-20 | `professorAtraso14Dias()` | CE-MC-04, Limite `dias = 14` |
| CT-MC-21 | `professorAtraso32Dias()` | CE-MC-04, Limite `dias = 32` |
| CT-MC-22 | `professorAtraso33DiasTetoExato()` | CE-MC-05, Limite `dias = 33` |
| CT-MC-23 | `professorAtraso34DiasTetoAplicado()` | CE-MC-06, Limite `dias = 34` |
| CT-MC-24 | `visitanteAtraso1Dia()` | CE-MC-03, Visitante `dias = 1` |
| CT-MC-25 | `visitanteAtraso12Dias()` | CE-MC-04, Visitante `dias = 12` |

---

### Método 5: `public boolean podeEmprestar(TipoUsuario tipo, int emprestimosAtivos, int multaPendenteCentavos, boolean possuiAtraso)`

#### Regra da Especificação:
- Devolve `true` SOMENTE SE as 3 condições forem satisfeitas cumulativamente:
  1. `emprestimosAtivos < limiteEmprestimos(tipo)`
  2. `multaPendenteCentavos <= 1000` (R$ 10,00)
  3. `!possuiAtraso` (não possui empréstimo em atraso)
- `emprestimosAtivos < 0` ou `multaPendenteCentavos < 0` ou `tipo == null` lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-PE-01 | Válida | Todas as 3 condições satisfeitas | Ativos < limite, multa <= 1000, atraso = false | `true` |
| CE-PE-02 | Válida | Ativos igual ou superior ao limite | `emprestimosAtivos >= limite` | `false` |
| CE-PE-03 | Válida | Multa pendente superior a R$ 10,00 | `multaPendenteCentavos > 1000` | `false` |
| CE-PE-04 | Válida | Possui empréstimo em atraso | `possuiAtraso = true` | `false` |
| CE-PE-05 | Válida | Múltiplas condições violadas simultaneamente | Ativos >= limite + multa > 1000 + atraso | `false` |
| CE-PE-06 | Inválida | Empréstimos ativos negativo | `emprestimosAtivos < 0` | `IllegalArgumentException` |
| CE-PE-07 | Inválida | Multa pendente negativa | `multaPendenteCentavos < 0` | `IllegalArgumentException` |
| CE-PE-08 | Inválida | Tipo de usuário nulo | `tipo = null` | `IllegalArgumentException` |

#### Valores-Limite:
- **`emprestimosAtivos`:**
  - Inválido: `-1`.
  - Válido inicial: `0`.
  - Aluno (limite 3): `2` (true), `3` (false), `4` (false).
  - Professor (limite 8): `7` (true), `8` (false).
  - Visitante (limite 1): `0` (true), `1` (false).
- **`multaPendenteCentavos`:**
  - Inválido: `-1`.
  - Válido: `0`, `999` (true), `1000` (fronteira exata, true), `1001` (fronteira exata, false).

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-PE-01 | `ativosNegativoLancaExcecao()` | CE-PE-06, Limite `-1` |
| CT-PE-02 | `multaNegativaLancaExcecao()` | CE-PE-07, Limite `-1` |
| CT-PE-03 | `tipoNuloLancaExcecao()` | CE-PE-08 |
| CT-PE-04 | `alunoTudoZerado()` | CE-PE-01, Mínimos válidos (0, 0, false) |
| CT-PE-05 | `alunoMultaAbaixoDoLimite()` | CE-PE-01, Limite multa `999` |
| CT-PE-06 | `alunoNoLimiteValido()` | CE-PE-01, Limite ativos `2` e multa `1000` |
| CT-PE-07 | `alunoAtingiuLimiteEmprestimos()` | CE-PE-02, Limite ativos `3` (Aluno) |
| CT-PE-08 | `alunoAcimaDoLimiteEmprestimos()` | CE-PE-02, Limite ativos `4` (Aluno) |
| CT-PE-09 | `alunoMultaAcimaDoLimite()` | CE-PE-03, Limite multa `1001` |
| CT-PE-10 | `alunoComAtrasoAtivo()` | CE-PE-04, Atraso `true` |
| CT-PE-11 | `professorNoLimiteValido()` | CE-PE-01, Limite ativos `7` (Professor) |
| CT-PE-12 | `professorAtingiuLimiteEmprestimos()` | CE-PE-02, Limite ativos `8` (Professor) |
| CT-PE-13 | `visitanteValido()` | CE-PE-01, Limite ativos `0` (Visitante) |
| CT-PE-14 | `visitanteAtingiuLimite()` | CE-PE-02, Limite ativos `1` (Visitante) |
| CT-PE-15 | `multiplasViolacoes()` | CE-PE-05 |

---

### Método 6: `public boolean podeRenovar(TipoUsuario tipo, int renovacoesFeitas, boolean possuiReserva, boolean emAtraso)`

#### Regra da Especificação:
- Visitantes **nunca** renovam (sempre `false`).
- Alunos renovam o mesmo empréstimo **até 2 vezes** (`renovacoesFeitas` 0 e 1 podem; 2 atingiu o teto).
- Professores renovam o mesmo empréstimo **até 3 vezes** (`renovacoesFeitas` 0, 1 e 2 podem; 3 atingiu o teto).
- Em qualquer caso, só renova se `!possuiReserva` e `!emAtraso`.
- `renovacoesFeitas < 0` ou `tipo == null` lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-PR-01 | Válida | Aluno elegível (renovações < 2, sem reserva, sem atraso) | `ALUNO, renovacoes = 0..1` | `true` |
| CE-PR-02 | Válida | Aluno atingiu ou excedeu limite de renovações | `ALUNO, renovacoes >= 2` | `false` |
| CE-PR-03 | Válida | Professor elegível (renovações < 3, sem reserva, sem atraso) | `PROFESSOR, renovacoes = 0..2` | `true` |
| CE-PR-04 | Válida | Professor atingiu ou excedeu limite de renovações | `PROFESSOR, renovacoes >= 3` | `false` |
| CE-PR-05 | Válida | Visitante tentando renovar | `VISITANTE, renovacoes = 0` | `false` |
| CE-PR-06 | Válida | Obra com reserva | `possuiReserva = true` | `false` |
| CE-PR-07 | Válida | Empréstimo em atraso | `emAtraso = true` | `false` |
| CE-PR-08 | Inválida | Renovações feitas negativo | `renovacoesFeitas < 0` | `IllegalArgumentException` |
| CE-PR-09 | Inválida | Tipo nulo | `tipo = null` | `IllegalArgumentException` |

#### Valores-Limite:
- `renovacoesFeitas = -1` (inválido).
- Aluno:
  - `0`: 1ª renovação (true)
  - `1`: 2ª renovação (true)
  - `2`: já renovou 2 vezes, atingiu o teto (false)
  - `3`: acima do teto (false)
- Professor:
  - `2`: 3ª renovação (true)
  - `3`: já renovou 3 vezes, atingiu o teto (false)
  - `4`: acima do teto (false)

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-PR-01 | `renovacoesNegativoLancaExcecao()` | CE-PR-08, Limite `-1` |
| CT-PR-02 | `tipoNuloLancaExcecao()` | CE-PR-09 |
| CT-PR-03 | `visitanteNuncaRenovaMesmoSemReservaESemAtraso()` | CE-PR-05 |
| CT-PR-04 | `alunoRenovacao0()` | CE-PR-01, Limite inferior `0` |
| CT-PR-05 | `alunoRenovacao1()` | CE-PR-01, Limite `1` |
| CT-PR-06 | `alunoRenovacao2AtingiuTeto()` | CE-PR-02, Limite fronteira `2` |
| CT-PR-07 | `alunoRenovacao3AcimaTeto()` | CE-PR-02, Limite `3` |
| CT-PR-08 | `professorRenovacao0()` | CE-PR-03, Limite inferior `0` |
| CT-PR-09 | `professorRenovacao2()` | CE-PR-03, Limite `2` |
| CT-PR-10 | `professorRenovacao3AtingiuTeto()` | CE-PR-04, Limite fronteira `3` |
| CT-PR-11 | `alunoComReserva()` | CE-PR-06 |
| CT-PR-12 | `alunoEmAtraso()` | CE-PR-07 |
| CT-PR-13 | `alunoComReservaEEATraso()` | CE-PR-06 e CE-PR-07 |

---

### Método 7: `public int multaTotalCentavos(TipoUsuario tipo, int[] diasAtrasoPorEmprestimo)`

#### Regra da Especificação:
- Soma a multa de cada empréstimo da lista (cada uma calculada conforme `multaCentavos`).
- O total nunca passa de R$ 100,00 (10000 centavos).
- Lista vazia dá total 0.
- Lista nula ou elemento negativo lançam `IllegalArgumentException`.
- `tipo` nulo lança `IllegalArgumentException`.

#### Classes de Equivalência:
| ID Classe | Tipo | Descrição | Entrada Representativa | Saída Esperada |
|---|---|---|---|---|
| CE-MT-01 | Válida | Lista vazia | `new int[]{}` | `0` |
| CE-MT-02 | Válida | Lista com 1 empréstimo sem atraso | `[0]` | `0` |
| CE-MT-03 | Válida | Lista com 1 empréstimo com atraso | `[5]` | Multa correspondente |
| CE-MT-04 | Válida | Lista com múltiplos empréstimos abaixo do teto global | `[10, 10]` | Soma das multas |
| CE-MT-05 | Válida | Total calculado atinge exatamente o teto de R$ 100,00 | `[30, 30]` (Aluno) | `10000` |
| CE-MT-06 | Válida | Total calculado excede o teto de R$ 100,00 | `[30, 30, 30]` (Aluno) | `10000` |
| CE-MT-07 | Válida | Empréstimo individual além de R$ 50,00 | `[40]` | Aplica teto individual (5000) |
| CE-MT-08 | Inválida | Tipo de usuário nulo | `tipo = null` | `IllegalArgumentException` |
| CE-MT-09 | Inválida | Array nulo | `diasAtrasoPorEmprestimo = null` | `IllegalArgumentException` |
| CE-MT-10 | Inválida | Elemento negativo no array | `[-1]` ou `[5, -2, 3]` | `IllegalArgumentException` |

#### Valores-Limite:
- Elementos: `[-1]` (inválido), `[0]` (mínimo válido).
- Teto global: `10000` centavos (exatamente R$ 100,00) vs `15000` (limitado a 10000).
- Teto individual de cada elemento: `[40]` (5000 centavos).

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste (`RegrasBibliotecaTest.java`) | Classe / Limite Coberto |
|---|---|---|
| CT-MT-01 | `tipoNuloComListaLancaExcecao()` | CE-MT-08 |
| CT-MT-02 | `tipoNuloComListaVaziaLancaExcecao()` | CE-MT-08 |
| CT-MT-03 | `listaNulaLancaExcecao()` | CE-MT-09 |
| CT-MT-04 | `elementoNegativoIsoladoLancaExcecao()` | CE-MT-10, Limite `[-1]` |
| CT-MT-05 | `elementoNegativoMeioLancaExcecao()` | CE-MT-10, Elemento `-2` no meio |
| CT-MT-06 | `listaVaziaRetornaZero()` | CE-MT-01 |
| CT-MT-07 | `umEmprestimoSemAtraso()` | CE-MT-02, Limite `[0]` |
| CT-MT-08 | `umEmprestimoAluno5Dias()` | CE-MT-03, `[5]` |
| CT-MT-09 | `multiplosEmprestimosAlunoAbaixoDoTeto()` | CE-MT-04, `[10, 10]` |
| CT-MT-10 | `multiplosEmprestimosProfessorCarencia()` | CE-MT-04, Carência `[3, 4]` |
| CT-MT-11 | `somaExatamenteNoTetoGlobal()` | CE-MT-05, Teto global exato `[30, 30]` |
| CT-MT-12 | `somaUltrapassaTetoGlobal()` | CE-MT-06, Teto global excedido `[30, 30, 30]` |
| CT-MT-13 | `emprestimoIndividualRespeitaTetoIndividual()` | CE-MT-07, Teto individual `[40]` |
