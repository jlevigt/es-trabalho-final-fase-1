# Relatório de Defeitos Encontrados — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Regra de Avaliação:** O resultado esperado vem sempre da especificação (Anexo A). Um teste que falha revela um defeito do sistema sob teste, comprovando a eficácia da suíte de testes de caixa-preta. Todos os IDs de teste (`CT-XX-NN`) estão estritamente sincronizados com o código-fonte da suíte JUnit 5 e com o [`PLANO-DE-TESTES.md`](PLANO-DE-TESTES.md).

---

| Método | Entrada | Resultado Esperado | Resultado Obtido | Caso de Teste & Técnica |
|---|---|---|---|---|
| `prazoDias` | `tipo = VISITANTE` | `7` | `10` | `CT-PD-03 [PE]` (Prazo para tipo VISITANTE) |
| `dataDevolucao` | `retirada = 2026-05-01, tipo = PROFESSOR` | `2026-06-01` | `2026-05-31` | `CT-DD-06 [AVL]` (Limite domingo para professor posterga para segunda) |
| `dataDevolucao` | `retirada = 2026-03-05, tipo = VISITANTE` | `2026-03-12` | `2026-03-15` | `CT-DD-07 [PE]` (Prazo visitante 7 dias caindo em dia útil) |
| `dataDevolucao` | `retirada = 2026-03-07, tipo = VISITANTE` | `2026-03-14` | `2026-03-17` | `CT-DD-08 [AVL]` (Limite sábado para visitante mantém sábado) |
| `dataDevolucao` | `retirada = 2026-03-08, tipo = VISITANTE` | `2026-03-16` | `2026-03-19` | `CT-DD-09 [AVL]` (Limite domingo para visitante posterga para segunda) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = -1` | `IllegalArgumentException` | Nenhuma exceção lançada | `CT-MC-01 [AVL]` (Fronteira inferior inválida: `dias = -1`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 11` | `1200` | `1100` | `CT-MC-08 [AVL]` (Limite inferior faixa 2: `dias = 11`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 31` | `5000` | `5200` | `CT-MC-11 [AVL]` (Limite superior do teto de R$ 50,00: `dias = 31`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 100` | `5000` | `19000` | `CT-MC-12 [PE]` (Valor representativo muito acima do teto) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 3` | `0` | `100` | `CT-MC-14 [AVL]` (Limite superior da carência de 3 dias: `dias = 3`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 4` | `100` | `200` | `CT-MC-15 [AVL]` (Limite inferior de cobrança para professor: `dias = 4`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 8` | `500` | `600` | `CT-MC-16 [PE]` (Valor médio na faixa 1 para professor: 5º dia cobrado) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 13` | `1000` | `1100` | `CT-MC-17 [AVL]` (Limite superior faixa 1 professor: `dias = 13`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 14` | `1200` | `1400` | `CT-MC-18 [AVL]` (Limite inferior faixa 2 professor: `dias = 14`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 33` | `5000` | `5200` | `CT-MC-19 [AVL]` (Limite exato do teto professor: 3 carência + 30 cobrados) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 34` | `5000` | `5400` | `CT-MC-20 [AVL]` (Limite superior do teto professor: `dias = 34`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 3, multa = 0, atraso = false` | `false` | `true` | `CT-PE-07 [AVL]` (Limite de empréstimos ativos para aluno: `ativos = 3`) |
| `podeEmprestar` | `tipo = PROFESSOR, ativos = 8, multa = 0, atraso = false` | `false` | `true` | `CT-PE-12 [AVL]` (Limite de empréstimos ativos para professor: `ativos = 8`) |
| `podeEmprestar` | `tipo = VISITANTE, ativos = 1, multa = 0, atraso = false` | `false` | `true` | `CT-PE-14 [AVL]` (Limite de empréstimos ativos para visitante: `ativos = 1`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 1001, atraso = false` | `false` | `true` | `CT-PE-09 [AVL]` (Limite superior de multa pendente: `multa = 1001`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 0, atraso = true` | `false` | `true` | `CT-PE-10 [PE]` (Bloqueio por atraso ativo pendente) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = -1, atraso = false` | `IllegalArgumentException` | Nenhuma exceção lançada | `CT-PE-02 [AVL]` (Fronteira de multa negativa inválida: `multa = -1`) |
| `podeRenovar` | `tipo = VISITANTE, renovacoes = 0, reserva = false, atraso = false` | `false` | `true` | `CT-PR-03 [PE]` (Regra estrita: visitante nunca renova) |
| `podeRenovar` | `tipo = PROFESSOR, renovacoes = 3, reserva = false, atraso = false` | `false` | `true` | `CT-PR-10 [AVL]` (Limite máximo de renovações para professor: `renovacoes = 3`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [-1]` | `IllegalArgumentException` | Nenhuma exceção lançada | `CT-MT-04 [AVL]` (Fronteira de elemento negativo: `[-1]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5, -2, 3]` | `IllegalArgumentException` | Nenhuma exceção lançada | `CT-MT-05 [PE]` (Elemento negativo em posição interna do array) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5]` | `500` | `0` | `CT-MT-08 [PE]` (Lista unitária na 1ª faixa: `[5]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [40]` | `5000` | `0` | `CT-MT-09 [PE]` (Empréstimo unitário com atraso acima do teto individual: `[40]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [10, 10]` | `2000` | `1000` | `CT-MT-10 [PE]` (Múltiplos elementos abaixo do teto global: `[10, 10]`) |
| `multaTotalCentavos` | `tipo = PROFESSOR, dias = [3, 4]` | `100` | `200` | `CT-MT-11 [PE]` (Múltiplos elementos para professor com carência) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [30, 30]` | `10000` | `5000` | `CT-MT-12 [AVL]` (Fronteira exata do teto global de R$ 100,00: `[30, 30]`) |
