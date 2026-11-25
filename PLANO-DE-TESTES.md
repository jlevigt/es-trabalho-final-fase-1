# Plano de Testes — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Técnicas Empregadas:** Partição de Equivalência (PE) e Análise de Valor-Limite (AVL).

---

## 1. Fundamentação Teórica

### 1.1 Partição de Equivalência (PE)
Divide o domínio de entrada e saída em classes onde todos os valores compartilham o mesmo comportamento esperado:
- **Classes Válidas (CV):** Dados operacionais regulares aceitos pelo sistema.
- **Classes Inválidas (CI):** Dados fora do domínio de entrada (nulos, negativos), onde o sistema deve disparar exceções (`IllegalArgumentException`).
- **Critério:** Seleciona-se um valor representativo de cada partição.

### 1.2 Análise de Valor-Limite (AVL)
Testa as fronteiras entre as partições ordenadas, onde a probabilidade estatística de falhas de código é máxima (*off-by-one*, operadores `<` em vez de `<=`, etc.):
- Testa o valor **na fronteira**, **logo antes** e **logo depois**.

---

## 2. Métodos e Especificação dos Testes

---

### Método 1: `public int prazoDias(TipoUsuario tipo)`
**Arquivo de Teste:** `PrazoDiasTest.java`

#### Regra da Especificação (Anexo A):
Devolve o prazo de empréstimo: 14 dias para Aluno, 30 para Professor e 7 para Visitante. Tipo nulo lança `IllegalArgumentException`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada | Saída Esperada |
|---|---|---|---|---|
| CT-PD-01 | `prazoAluno()` | PE | `TipoUsuario.ALUNO` | `14` |
| CT-PD-02 | `prazoProfessor()` | PE | `TipoUsuario.PROFESSOR` | `30` |
| CT-PD-03 | `prazoVisitante()` | PE | `TipoUsuario.VISITANTE` | `7` |
| CT-PD-04 | `tipoNuloLancaExcecao()` | PE | `null` | `IllegalArgumentException` |

---

### Método 2: `public LocalDate dataDevolucao(LocalDate retirada, TipoUsuario tipo)`
**Arquivo de Teste:** `DataDevolucaoTest.java`

#### Regra da Especificação (Anexo A):
Soma o prazo do tipo à data de retirada. Se o resultado coincidir com um domingo, a devolução passa para a segunda-feira seguinte. O sábado é dia normal. Retirada ou tipo nulos lançam `IllegalArgumentException`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada (Retirada, Tipo) | Saída Esperada |
|---|---|---|---|---|
| CT-DD-01 | `alunoDiaUtil()` | PE | Quarta 04/03/2026, ALUNO | Quarta 18/03/2026 |
| CT-DD-02 | `alunoCaindoSabado()` | AVL | Sábado 07/03/2026, ALUNO | Sábado 21/03/2026 (sábado normal) |
| CT-DD-03 | `alunoCaindoDomingoPassaSegunda()` | AVL | Domingo 08/03/2026, ALUNO | Segunda 23/03/2026 (postergado) |
| CT-DD-04 | `professorDiaUtil()` | PE | Segunda 01/06/2026, PROFESSOR | Quarta 01/07/2026 |
| CT-DD-05 | `professorCaindoSabado()` | AVL | Quinta 30/04/2026, PROFESSOR | Sábado 30/05/2026 (sábado normal) |
| CT-DD-06 | `professorCaindoDomingoPassaSegunda()` | AVL | Sexta 01/05/2026, PROFESSOR | Segunda 01/06/2026 (postergado) |
| CT-DD-07 | `visitanteDiaUtil()` | PE | Quinta 05/03/2026, VISITANTE | Quinta 12/03/2026 |
| CT-DD-08 | `visitanteCaindoSabado()` | AVL | Sábado 07/03/2026, VISITANTE | Sábado 14/03/2026 (sábado normal) |
| CT-DD-09 | `visitanteCaindoDomingoPassaSegunda()` | AVL | Domingo 08/03/2026, VISITANTE | Segunda 16/03/2026 (postergado) |
| CT-DD-10 | `retiradaNulaLancaExcecao()` | PE | `null`, ALUNO | `IllegalArgumentException` |
| CT-DD-11 | `tipoNuloLancaExcecao()` | PE | 01/03/2026, `null` | `IllegalArgumentException` |
| CT-DD-12 | `ambosNulosLancaExcecao()` | PE | `null`, `null` | `IllegalArgumentException` |

---

### Método 3: `public int limiteEmprestimos(TipoUsuario tipo)`
**Arquivo de Teste:** `LimiteEmprestimosTest.java`

#### Regra da Especificação (Anexo A):
Devolve o total de empréstimos simultâneos: 3 para Aluno, 8 para Professor e 1 para Visitante. Tipo nulo lança `IllegalArgumentException`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada | Saída Esperada |
|---|---|---|---|---|
| CT-LE-01 | `limiteAluno()` | PE | `TipoUsuario.ALUNO` | `3` |
| CT-LE-02 | `limiteProfessor()` | PE | `TipoUsuario.PROFESSOR` | `8` |
| CT-LE-03 | `limiteVisitante()` | PE | `TipoUsuario.VISITANTE` | `1` |
| CT-LE-04 | `tipoNuloLancaExcecao()` | PE | `null` | `IllegalArgumentException` |

---

### Método 4: `public int multaCentavos(TipoUsuario tipo, int diasAtraso)`
**Arquivo de Teste:** `MultaCentavosTest.java`

#### Regra da Especificação (Anexo A):
- Professores têm carência: 3 primeiros dias de atraso não são cobrados (0 centavos). Demais perfis pagam todos os dias.
- 1º ao 10º dia cobrado: R$ 1,00/dia (100 centavos).
- 11º dia cobrado em diante: R$ 2,00/dia (200 centavos).
- Teto: a multa nunca passa de R$ 50,00 (5000 centavos).
  - *Origem matemática da fronteira de 30 dias (Aluno):* $10 \text{ dias} \times 100 = 1000$ centavos. Restam 4000 centavos na 2ª faixa a 200 centavos/dia = 20 dias. Total de dias para atingir R$ 50,00: $10 + 20 = \mathbf{30\text{ dias}}$.
  - *Origem matemática para Professor:* 3 dias de carência + 30 dias cobrados = $\mathbf{33\text{ dias}}$.
- Atraso negativo ou tipo nulo lança `IllegalArgumentException`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada | Saída Esperada |
|---|---|---|---|---|
| CT-MC-01 | `atrasoNegativoLancaExcecao()` | AVL | ALUNO, `dias = -1` | `IllegalArgumentException` |
| CT-MC-02 | `tipoNuloLancaExcecao()` | PE | `null`, `dias = 5` | `IllegalArgumentException` |
| CT-MC-03 | `tipoNuloEAtrasoNegativoLancaExcecao()` | PE/AVL | `null`, `dias = -1` | `IllegalArgumentException` |
| CT-MC-04 | `alunoSemAtraso()` | AVL | ALUNO, `dias = 0` | `0` |
| CT-MC-05 | `alunoAtraso1Dia()` | AVL | ALUNO, `dias = 1` | `100` |
| CT-MC-06 | `alunoAtraso5Dias()` | PE | ALUNO, `dias = 5` | `500` |
| CT-MC-07 | `alunoAtraso10Dias()` | AVL | ALUNO, `dias = 10` | `1000` |
| CT-MC-08 | `alunoAtraso11Dias()` | AVL | ALUNO, `dias = 11` | `1200` |
| CT-MC-09 | `alunoAtraso12DiasExemploEnunciado()` | PE | ALUNO, `dias = 12` | `1400` |
| CT-MC-10 | `alunoAtraso30DiasTetoExato()` | AVL | ALUNO, `dias = 30` | `5000` (teto exato) |
| CT-MC-11 | `alunoAtraso31DiasTetoAplicado()` | AVL | ALUNO, `dias = 31` | `5000` (teto aplicado) |
| CT-MC-12 | `alunoAtraso100DiasTetoAplicado()` | PE | ALUNO, `dias = 100` | `5000` |
| CT-MC-13 | `professorSemAtraso()` | AVL | PROFESSOR, `dias = 0` | `0` |
| CT-MC-14 | `professorCarencia(int)` | AVL | PROFESSOR, `dias = 1, 2, 3` | `0` (3 execuções) |
| CT-MC-15 | `professorAtraso4Dias()` | AVL | PROFESSOR, `dias = 4` | `100` (1º cobrado) |
| CT-MC-16 | `professorAtraso8Dias()` | PE | PROFESSOR, `dias = 8` | `500` (5º cobrado) |
| CT-MC-17 | `professorAtraso13Dias()` | AVL | PROFESSOR, `dias = 13` | `1000` (10º cobrado) |
| CT-MC-18 | `professorAtraso14Dias()` | AVL | PROFESSOR, `dias = 14` | `1200` (11º cobrado) |
| CT-MC-19 | `professorAtraso33DiasTetoExato()` | AVL | PROFESSOR, `dias = 33` | `5000` (teto exato) |
| CT-MC-20 | `professorAtraso34DiasTetoAplicado()` | AVL | PROFESSOR, `dias = 34` | `5000` (teto aplicado) |
| CT-MC-21 | `visitanteAtraso1Dia()` | AVL | VISITANTE, `dias = 1` | `100` |
| CT-MC-22 | `visitanteAtraso12Dias()` | PE | VISITANTE, `dias = 12` | `1400` |

---

### Método 5: `public boolean podeEmprestar(TipoUsuario, int, int, boolean)`
**Arquivo de Teste:** `PodeEmprestarTest.java`

#### Regra da Especificação (Anexo A):
Retorna `true` SOMENTE SE: (1) `emprestimosAtivos < limite`, (2) `multaPendenteCentavos <= 1000` e (3) `!possuiAtraso`. Negativos ou tipo nulo lançam `IllegalArgumentException`.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada (tipo, ativos, multa, atraso) | Saída Esperada |
|---|---|---|---|---|
| CT-PE-01 | `ativosNegativoLancaExcecao()` | AVL | ALUNO, -1, 0, false | `IllegalArgumentException` |
| CT-PE-02 | `multaNegativaLancaExcecao()` | AVL | ALUNO, 0, -1, false | `IllegalArgumentException` |
| CT-PE-03 | `tipoNuloLancaExcecao()` | PE | `null`, 0, 0, false | `IllegalArgumentException` |
| CT-PE-04 | `alunoTudoZerado()` | PE | ALUNO, 0, 0, false | `true` |
| CT-PE-05 | `alunoMultaAbaixoDoLimite()` | AVL | ALUNO, 2, 999, false | `true` |
| CT-PE-06 | `alunoNoLimiteValido()` | AVL | ALUNO, 2, 1000, false | `true` |
| CT-PE-07 | `alunoAtingiuLimiteEmprestimos()` | AVL | ALUNO, 3, 0, false | `false` |
| CT-PE-08 | `alunoAcimaDoLimiteEmprestimos()` | AVL | ALUNO, 4, 0, false | `false` |
| CT-PE-09 | `alunoMultaAcimaDoLimite()` | AVL | ALUNO, 0, 1001, false | `false` |
| CT-PE-10 | `alunoComAtrasoAtivo()` | PE | ALUNO, 0, 0, true | `false` |
| CT-PE-11 | `professorNoLimiteValido()` | AVL | PROFESSOR, 7, 1000, false | `true` |
| CT-PE-12 | `professorAtingiuLimiteEmprestimos()` | AVL | PROFESSOR, 8, 0, false | `false` |
| CT-PE-13 | `visitanteValido()` | AVL | VISITANTE, 0, 1000, false | `true` |
| CT-PE-14 | `visitanteAtingiuLimite()` | AVL | VISITANTE, 1, 0, false | `false` |

---

### Método 6: `public boolean podeRenovar(TipoUsuario, int, boolean, boolean)`
**Arquivo de Teste:** `PodeRenovarTest.java`

#### Regra da Especificação (Anexo A):
Visitantes nunca renovam. Alunos renovam até 2 vezes (`renovacoesFeitas = 0, 1` podem; `2` já atingiu o teto). Professores renovam até 3 vezes (`renovacoesFeitas = 0, 1, 2` podem; `3` atingiu o teto). Só renova se obra não tiver reserva e não estiver em atraso. Negativo lança exceção.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada (tipo, renovações, reserva, atraso) | Saída Esperada |
|---|---|---|---|---|
| CT-PR-01 | `renovacoesNegativoLancaExcecao()` | AVL | ALUNO/PROF/VIS, -1, false, false | `IllegalArgumentException` |
| CT-PR-02 | `tipoNuloLancaExcecao()` | PE | `null`, 0, false, false | `IllegalArgumentException` |
| CT-PR-03 | `visitanteNuncaRenovaMesmoSemReservaESemAtraso()` | PE | VISITANTE, 0, false, false | `false` |
| CT-PR-04 | `alunoRenovacao0()` | AVL | ALUNO, 0, false, false | `true` |
| CT-PR-05 | `alunoRenovacao1()` | AVL | ALUNO, 1, false, false | `true` |
| CT-PR-06 | `alunoRenovacao2AtingiuTeto()` | AVL | ALUNO, 2, false, false | `false` |
| CT-PR-07 | `alunoRenovacao3AcimaTeto()` | AVL | ALUNO, 3, false, false | `false` |
| CT-PR-08 | `professorRenovacao0()` | AVL | PROFESSOR, 0, false, false | `true` |
| CT-PR-09 | `professorRenovacao2()` | AVL | PROFESSOR, 2, false, false | `true` |
| CT-PR-10 | `professorRenovacao3AtingiuTeto()` | AVL | PROFESSOR, 3, false, false | `false` |
| CT-PR-11 | `alunoComReserva()` | PE | ALUNO, 0, true, false | `false` |
| CT-PR-12 | `alunoEmAtraso()` | PE | ALUNO, 0, false, true | `false` |
| CT-PR-13 | `alunoComReservaEEATraso()` | PE | ALUNO, 0, true, true | `false` |

---

### Método 7: `public int multaTotalCentavos(TipoUsuario, int[])`
**Arquivo de Teste:** `MultaTotalCentavosTest.java`

#### Regra da Especificação (Anexo A):
Soma a multa de cada empréstimo da lista. O total nunca ultrapassa R$ 100,00 (10000 centavos). Lista vazia dá 0. Lista nula, elemento negativo ou tipo nulo lançam exceção.

#### Mapeamento de Testes:
| ID Caso de Teste | Método de Teste | Técnica | Entrada | Saída Esperada |
|---|---|---|---|---|
| CT-MT-01 | `tipoNuloComListaLancaExcecao()` | PE | `null`, `[0]` | `IllegalArgumentException` |
| CT-MT-02 | `tipoNuloComListaVaziaLancaExcecao()` | PE | `null`, `[]` | `IllegalArgumentException` |
| CT-MT-03 | `listaNulaLancaExcecao()` | PE | ALUNO, `null` | `IllegalArgumentException` |
| CT-MT-04 | `elementoNegativoIsoladoLancaExcecao()` | AVL | ALUNO, `[-1]` | `IllegalArgumentException` |
| CT-MT-05 | `elementoNegativoMeioLancaExcecao()` | PE | ALUNO, `[5, -2, 3]` | `IllegalArgumentException` |
| CT-MT-06 | `listaVaziaRetornaZero()` | PE | ALUNO/PROF/VIS, `[]` | `0` |
| CT-MT-07 | `umEmprestimoSemAtraso()` | AVL | ALUNO, `[0]` | `0` |
| CT-MT-08 | `umEmprestimoAluno5Dias()` | PE | ALUNO, `[5]` | `500` |
| CT-MT-09 | `emprestimoIndividualRespeitaTetoIndividual()` | PE | ALUNO, `[40]` | `5000` (teto individual) |
| CT-MT-10 | `multiplosEmprestimosAlunoAbaixoDoTeto()` | PE | ALUNO, `[10, 10]` | `2000` |
| CT-MT-11 | `multiplosEmprestimosProfessorCarencia()` | PE | PROFESSOR, `[3, 4]` | `100` |
| CT-MT-12 | `somaExatamenteNoTetoGlobal()` | AVL | ALUNO, `[30, 30]` | `10000` (teto global exato) |
| CT-MT-13 | `somaUltrapassaTetoGlobal()` | PE | ALUNO, `[30, 30, 30]` | `10000` (teto global excedido) |

---

## 3. Resumo Consolidado da Suíte de Testes

| Arquivo de Teste | Método Coberto | Testes Executados | Defeitos Revelados (Falhas) |
|---|---|:---:|:---:|
| `PrazoDiasTest.java` | `prazoDias` | 4 | 1 |
| `DataDevolucaoTest.java` | `dataDevolucao` | 12 | 4 |
| `LimiteEmprestimosTest.java` | `limiteEmprestimos` | 4 | 0 |
| `MultaCentavosTest.java` | `multaCentavos` | 24 | 11 |
| `PodeEmprestarTest.java` | `podeEmprestar` | 14 | 6 |
| `PodeRenovarTest.java` | `podeRenovar` | 13 | 2 |
| `MultaTotalCentavosTest.java` | `multaTotalCentavos` | 13 | 7 |
| **Total Geral da Suíte** | **7 métodos** | **84** | **31** |

