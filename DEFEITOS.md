# Relatório de Defeitos Encontrados — Fase 1 (Caixa-Preta)

**Projeto:** Sistema de Biblioteca — Engenharia de Software (IFCE)  
**Data:** 24 de novembro de 2026  
**Regra de Ouro:** O resultado esperado vem sempre da especificação (Anexo A). Um teste que falha revela um defeito do sistema sob teste.

---

| Método | Entrada | Resultado Esperado | Resultado Obtido | Classe ou Limite |
|---|---|---|---|---|
| `prazoDias` | `tipo = VISITANTE` | `7` | `10` | CE-PD-03 (Tipo VISITANTE) |
| `dataDevolucao` | `retirada = 2026-05-01, tipo = PROFESSOR` | `2026-06-01` | `2026-05-31` | CE-DD-03 / Limite Domingo (Professor) |
| `dataDevolucao` | `retirada = 2026-03-05, tipo = VISITANTE` | `2026-03-12` | `2026-03-15` | CE-DD-01 / Prazo Visitante (dia útil) |
| `dataDevolucao` | `retirada = 2026-03-07, tipo = VISITANTE` | `2026-03-14` | `2026-03-17` | CE-DD-02 / Limite Sábado (Visitante) |
| `dataDevolucao` | `retirada = 2026-03-08, tipo = VISITANTE` | `2026-03-16` | `2026-03-19` | CE-DD-03 / Limite Domingo (Visitante) |
| `dataDevolucao` | `retirada = 2026-12-31, tipo = VISITANTE` | `2027-01-07` | `2027-01-10` | CE-DD-04 / Limite Virada de Ano |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = -1` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MC-07 / Limite `dias = -1` |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = -50` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MC-07 / Limite negativo grande |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 11` | `1200` | `1100` | CE-MC-04 / Limite inferior da 2ª faixa (`dias = 11`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 31` | `5000` | `5200` | CE-MC-06 / Limite superior do teto (`dias = 31`) |
| `multaCentavos` | `tipo = ALUNO, diasAtraso = 100` | `5000` | `19000` | CE-MC-06 / Valor muito acima do teto (`dias = 100`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 3` | `0` | `100` | CE-MC-02 / Limite superior da carência (`dias = 3`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 4` | `100` | `200` | CE-MC-03 / Limite inferior de cobrança (`dias = 4`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 8` | `500` | `600` | CE-MC-03 / Meio da faixa 1 (Professor) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 13` | `1000` | `1100` | CE-MC-03 / Limite superior da faixa 1 (`dias = 13`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 14` | `1200` | `1400` | CE-MC-04 / Limite inferior da faixa 2 (`dias = 14`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 32` | `4800` | `5000` | CE-MC-04 / Limite anterior ao teto (`dias = 32`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 33` | `5000` | `5200` | CE-MC-05 / Limite exato do teto (`dias = 33`) |
| `multaCentavos` | `tipo = PROFESSOR, diasAtraso = 34` | `5000` | `5400` | CE-MC-06 / Limite superior do teto (`dias = 34`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 3, multa = 0, atraso = false` | `false` | `true` | CE-PE-02 / Limite de ativos (`ativos = 3`) |
| `podeEmprestar` | `tipo = PROFESSOR, ativos = 8, multa = 0, atraso = false` | `false` | `true` | CE-PE-02 / Limite de ativos (`ativos = 8`) |
| `podeEmprestar` | `tipo = VISITANTE, ativos = 1, multa = 0, atraso = false` | `false` | `true` | CE-PE-02 / Limite de ativos (`ativos = 1`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 1001, atraso = false` | `false` | `true` | CE-PE-03 / Limite superior de multa (`multa = 1001`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = 0, atraso = true` | `false` | `true` | CE-PE-04 / Presença de atraso (`possuiAtraso = true`) |
| `podeEmprestar` | `tipo = ALUNO, ativos = 0, multa = -1, atraso = false` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-PE-07 / Limite de multa negativa (`multa = -1`) |
| `podeRenovar` | `tipo = VISITANTE, renovacoes = 0, reserva = false, atraso = false` | `false` | `true` | CE-PR-05 / Visitante tentando renovar |
| `podeRenovar` | `tipo = PROFESSOR, renovacoes = 3, reserva = false, atraso = false` | `false` | `true` | CE-PR-04 / Limite superior de renovações (`renovacoes = 3`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [-1]` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MT-10 / Limite de elemento negativo (`[-1]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5, -2, 3]` | `IllegalArgumentException` | Nenhuma exceção lançada | CE-MT-10 / Elemento negativo interno |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [5]` | `500` | `0` | CE-MT-03 / Lista com 1 elemento (`[5]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [40]` | `5000` | `0` | CE-MT-07 / Teto individual em elemento único (`[40]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [10, 10]` | `2000` | `1000` | CE-MT-04 / Múltiplos elementos (`[10, 10]`) |
| `multaTotalCentavos` | `tipo = PROFESSOR, dias = [3, 4]` | `100` | `200` | CE-MT-04 / Múltiplos elementos com carência (`[3, 4]`) |
| `multaTotalCentavos` | `tipo = ALUNO, dias = [30, 30]` | `10000` | `5000` | CE-MT-05 / Limite exato do teto global (`[30, 30]`) |
