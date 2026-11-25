# Relatório de Defeitos Encontrados — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Regra de Avaliação:** O resultado esperado vem sempre da especificação (Anexo A). Um teste que falha revela um defeito do sistema sob teste, comprovando a eficácia da suíte de testes de caixa-preta.

---

| Método | Entrada | Resultado Esperado | Resultado Obtido | Classe ou Limite |
|---|---|---|---|---|
| `prazoDias` | `tipo = VISITANTE` | `7` | `10` | CE-PD-03 [PE] (Tipo VISITANTE) |
| `dataDevolucao` | `retirada = 2026-05-01, tipo = PROFESSOR` | `2026-06-01` | `2026-05-31` | CE-DD-03 [AVL] (Limite Domingo Professor) |
| `dataDevolucao` | `retirada = 2026-03-05, tipo = VISITANTE` | `2026-03-12` | `2026-03-15` | CE-DD-01 [PE] (Prazo Visitante dia útil) |
| `dataDevolucao` | `retirada = 2026-03-07, tipo = VISITANTE` | `2026-03-14` | `2026-03-17` | CE-DD-02 [AVL] (Limite Sábado Visitante) |
| `dataDevolucao` | `retirada = 2026-03-08, tipo = VISITANTE` | `2026-03-16` | `2026-03-19` | CE-DD-03 [AVL] (Limite Domingo Visitante) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = -1` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MC-01 [AVL] (Limite `dias = -1`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 11` | `1200` | `1100` | CE-MC-08 [AVL] (Limite inferior faixa 2: `dias = 11`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 31` | `5000` | `5200` | CE-MC-11 [AVL] (Limite superior do teto: `dias = 31`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 100` | `5000` | `19000` | CE-MC-12 [PE] (Valor representativo acima do teto) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 3` | `0` | `100` | CE-MC-14 [AVL] (Limite superior da carência: `dias = 3`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 4` | `100` | `200` | CE-MC-15 [AVL] (Limite inferior de cobrança: `dias = 4`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 8` | `500` | `600` | CE-MC-16 [PE] (Meio da faixa 1 Professor) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 13` | `1000` | `1100` | CE-MC-17 [AVL] (Limite superior da faixa 1: `dias = 13`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 14` | `1200` | `1400` | CE-MC-18 [AVL] (Limite inferior da faixa 2: `dias = 14`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 33` | `5000` | `5200` | CE-MC-19 [AVL] (Limite exato do teto: `dias = 33`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 34` | `5000` | `5400` | CE-MC-20 [AVL] (Limite superior do teto: `dias = 34`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 3, multa = 0, atraso = false` | `false` | `true` | CE-PE-07 [AVL] (Limite de ativos: `ativos = 3`) |
| `podeEmprestar` | `tipo = PROFESSOR, ativos = 8, multa = 0, atraso = false` | `false` | `true` | CE-PE-12 [AVL] (Limite de ativos: `ativos = 8`) |
| `podeEmprestar` | `tipo = VISITANTE, ativos = 1, multa = 0, atraso = false` | `false` | `true` | CE-PE-14 [AVL] (Limite de ativos: `ativos = 1`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 1001, atraso = false` | `false` | `true` | CE-PE-09 [AVL] (Limite superior de multa: `multa = 1001`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 0, atraso = true` | `false` | `true` | CE-PE-10 [PE] (Presença de empréstimo em atraso) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = -1, atraso = false` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-PE-02 [AVL] (Limite de multa negativa: `multa = -1`) |
| `podeRenovar` | `tipo = VISITANTE, renovacoes = 0, reserva = false, atraso = false` | `false` | `true` | CE-PR-03 [PE] (Visitante tentando renovar) |
| `podeRenovar` | `tipo = PROFESSOR, renovacoes = 3, reserva = false, atraso = false` | `false` | `true` | CE-PR-10 [AVL] (Limite de renovações: `renovacoes = 3`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [-1]` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MT-04 [AVL] (Limite de elemento negativo: `[-1]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5, -2, 3]` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MT-05 [PE] (Elemento negativo interno no array) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5]` | `500` | `0` | CE-MT-08 [PE] (Lista unitária: `[5]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [40]` | `5000` | `0` | CE-MT-09 [AVL] (Teto individual em lista unitária: `[40]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [10, 10]` | `2000` | `1000` | CE-MT-10 [PE] (Múltiplos elementos: `[10, 10]`) |
| `multaTotalCentavos` | `tipo = PROFESSOR, dias = [3, 4]` | `100` | `200` | CE-MT-11 [PE] (Múltiplos elementos com carência) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [30, 30]` | `10000` | `5000` | CE-MT-12 [AVL] (Limite exato do teto global de R$ 100,00) |
