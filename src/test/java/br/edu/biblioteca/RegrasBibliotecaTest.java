package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Suíte de Testes Caixa-Preta - RegrasBiblioteca")
class RegrasBibliotecaTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // =========================================================================
    // 1. prazoDias(TipoUsuario tipo)
    // =========================================================================
    @Nested
    @DisplayName("1. prazoDias")
    class PrazoDiasTests {

        @Test
        @DisplayName("CT-PD-01: Aluno deve ter prazo de 14 dias")
        void prazoAluno() {
            assertEquals(14, regras.prazoDias(TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-PD-02: Professor deve ter prazo de 30 dias")
        void prazoProfessor() {
            assertEquals(30, regras.prazoDias(TipoUsuario.PROFESSOR));
        }

        @Test
        @DisplayName("CT-PD-03: Visitante deve ter prazo de 7 dias")
        void prazoVisitante() {
            assertEquals(7, regras.prazoDias(TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-PD-04: Tipo nulo deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.prazoDias(null));
        }
    }

    // =========================================================================
    // 2. dataDevolucao(LocalDate retirada, TipoUsuario tipo)
    // =========================================================================
    @Nested
    @DisplayName("2. dataDevolucao")
    class DataDevolucaoTests {

        @Test
        @DisplayName("CT-DD-01: Aluno soma 14 dias caindo em dia útil (quarta-feira) -> mantém dia")
        void alunoDiaUtil() {
            // Quarta 2026-03-04 + 14 dias = Quarta 2026-03-18
            LocalDate retirada = LocalDate.of(2026, 3, 4);
            assertEquals(LocalDate.of(2026, 3, 18), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-DD-02: Aluno soma 14 dias caindo em sábado (sábado é dia normal) -> mantém sábado")
        void alunoCaindoSabado() {
            // Sábado 2026-03-07 + 14 dias = Sábado 2026-03-21
            LocalDate retirada = LocalDate.of(2026, 3, 7);
            assertEquals(LocalDate.of(2026, 3, 21), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-DD-03: Aluno soma 14 dias caindo em domingo -> passa para segunda-feira")
        void alunoCaindoDomingoPassaSegunda() {
            // Domingo 2026-03-08 + 14 dias = Domingo 2026-03-22 -> Segunda 2026-03-23
            LocalDate retirada = LocalDate.of(2026, 3, 8);
            assertEquals(LocalDate.of(2026, 3, 23), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-DD-04: Professor soma 30 dias caindo em dia útil (quarta-feira) -> mantém dia")
        void professorDiaUtil() {
            // Segunda 2026-06-01 + 30 dias = Quarta 2026-07-01
            LocalDate retirada = LocalDate.of(2026, 6, 1);
            assertEquals(LocalDate.of(2026, 7, 1), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
        }

        @Test
        @DisplayName("CT-DD-05: Professor soma 30 dias caindo em sábado (sábado é dia normal) -> mantém sábado")
        void professorCaindoSabado() {
            // Quinta 2026-04-30 + 30 dias = Sábado 2026-05-30
            LocalDate retirada = LocalDate.of(2026, 4, 30);
            assertEquals(LocalDate.of(2026, 5, 30), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
        }

        @Test
        @DisplayName("CT-DD-06: Professor soma 30 dias caindo em domingo -> passa para segunda-feira")
        void professorCaindoDomingoPassaSegunda() {
            // Sexta 2026-05-01 + 30 dias = Domingo 2026-05-31 -> Segunda 2026-06-01
            LocalDate retirada = LocalDate.of(2026, 5, 1);
            assertEquals(LocalDate.of(2026, 6, 1), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
        }

        @Test
        @DisplayName("CT-DD-07: Visitante soma 7 dias caindo em dia útil (quinta-feira) -> mantém dia")
        void visitanteDiaUtil() {
            // Quinta 2026-03-05 + 7 dias = Quinta 2026-03-12
            LocalDate retirada = LocalDate.of(2026, 3, 5);
            assertEquals(LocalDate.of(2026, 3, 12), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-DD-08: Visitante soma 7 dias caindo em sábado (sábado é dia normal) -> mantém sábado")
        void visitanteCaindoSabado() {
            // Sábado 2026-03-07 + 7 dias = Sábado 2026-03-14
            LocalDate retirada = LocalDate.of(2026, 3, 7);
            assertEquals(LocalDate.of(2026, 3, 14), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-DD-09: Visitante soma 7 dias caindo em domingo -> passa para segunda-feira")
        void visitanteCaindoDomingoPassaSegunda() {
            // Domingo 2026-03-08 + 7 dias = Domingo 2026-03-15 -> Segunda 2026-03-16
            LocalDate retirada = LocalDate.of(2026, 3, 8);
            assertEquals(LocalDate.of(2026, 3, 16), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-DD-10: Virada de mês com 31 dias (31 de janeiro para fevereiro)")
        void viradaDeMes() {
            // Sexta 2026-01-30 + 14 dias = Sexta 2026-02-13
            LocalDate retirada = LocalDate.of(2026, 1, 30);
            assertEquals(LocalDate.of(2026, 2, 13), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-DD-11: Virada de ano (31 de dezembro)")
        void viradaDeAno() {
            // Quinta 2026-12-31 + 7 dias = Quinta 2027-01-07
            LocalDate retirada = LocalDate.of(2026, 12, 31);
            assertEquals(LocalDate.of(2027, 1, 7), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-DD-12: Data de retirada nula deve lançar IllegalArgumentException")
        void retiradaNulaLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(null, TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-DD-13: Tipo nulo com retirada válida deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            LocalDate retirada = LocalDate.of(2026, 3, 1);
            assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(retirada, null));
        }

        @Test
        @DisplayName("CT-DD-14: Retirada e tipo nulos devem lançar IllegalArgumentException")
        void ambosNulosLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(null, null));
        }
    }

    // =========================================================================
    // 3. limiteEmprestimos(TipoUsuario tipo)
    // =========================================================================
    @Nested
    @DisplayName("3. limiteEmprestimos")
    class LimiteEmprestimosTests {

        @Test
        @DisplayName("CT-LE-01: Aluno pode ter até 3 empréstimos simultâneos")
        void limiteAluno() {
            assertEquals(3, regras.limiteEmprestimos(TipoUsuario.ALUNO));
        }

        @Test
        @DisplayName("CT-LE-02: Professor pode ter até 8 empréstimos simultâneos")
        void limiteProfessor() {
            assertEquals(8, regras.limiteEmprestimos(TipoUsuario.PROFESSOR));
        }

        @Test
        @DisplayName("CT-LE-03: Visitante pode ter até 1 empréstimo simultâneo")
        void limiteVisitante() {
            assertEquals(1, regras.limiteEmprestimos(TipoUsuario.VISITANTE));
        }

        @Test
        @DisplayName("CT-LE-04: Tipo nulo deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.limiteEmprestimos(null));
        }
    }

    // =========================================================================
    // 4. multaCentavos(TipoUsuario tipo, int diasAtraso)
    // =========================================================================
    @Nested
    @DisplayName("4. multaCentavos")
    class MultaCentavosTests {

        @Test
        @DisplayName("CT-MC-01: Dias de atraso negativo (-1) deve lançar IllegalArgumentException")
        void atrasoNegativoLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(TipoUsuario.ALUNO, -1));
        }

        @Test
        @DisplayName("CT-MC-02: Dias de atraso negativo grande (-50) deve lançar IllegalArgumentException")
        void atrasoNegativoGrandeLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(TipoUsuario.PROFESSOR, -50));
        }

        @Test
        @DisplayName("CT-MC-03: Tipo nulo com atraso válido deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(null, 5));
        }

        @Test
        @DisplayName("CT-MC-04: Tipo nulo e atraso negativo deve lançar IllegalArgumentException")
        void tipoNuloEAtrasoNegativoLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(null, -1));
        }

        @Test
        @DisplayName("CT-MC-05: Aluno com 0 dias de atraso -> multa 0")
        void alunoSemAtraso() {
            assertEquals(0, regras.multaCentavos(TipoUsuario.ALUNO, 0));
        }

        @Test
        @DisplayName("CT-MC-06: Aluno com 1 dia de atraso (fronteira inf 1ª faixa) -> R$ 1,00 (100 centavos)")
        void alunoAtraso1Dia() {
            assertEquals(100, regras.multaCentavos(TipoUsuario.ALUNO, 1));
        }

        @Test
        @DisplayName("CT-MC-07: Aluno com 5 dias de atraso (valor médio 1ª faixa) -> R$ 5,00 (500 centavos)")
        void alunoAtraso5Dias() {
            assertEquals(500, regras.multaCentavos(TipoUsuario.ALUNO, 5));
        }

        @Test
        @DisplayName("CT-MC-08: Aluno com 10 dias de atraso (fronteira sup 1ª faixa) -> R$ 10,00 (1000 centavos)")
        void alunoAtraso10Dias() {
            assertEquals(1000, regras.multaCentavos(TipoUsuario.ALUNO, 10));
        }

        @Test
        @DisplayName("CT-MC-09: Aluno com 11 dias de atraso (fronteira inf 2ª faixa) -> 10*100 + 1*200 = R$ 12,00 (1200 centavos)")
        void alunoAtraso11Dias() {
            assertEquals(1200, regras.multaCentavos(TipoUsuario.ALUNO, 11));
        }

        @Test
        @DisplayName("CT-MC-10: Aluno com 12 dias de atraso (exemplo do enunciado) -> 10*100 + 2*200 = R$ 14,00 (1400 centavos)")
        void alunoAtraso12DiasExemploEnunciado() {
            assertEquals(1400, regras.multaCentavos(TipoUsuario.ALUNO, 12));
        }

        @Test
        @DisplayName("CT-MC-11: Aluno com 29 dias de atraso (logo antes do teto) -> 1000 + 19*200 = R$ 48,00 (4800 centavos)")
        void alunoAtraso29Dias() {
            assertEquals(4800, regras.multaCentavos(TipoUsuario.ALUNO, 29));
        }

        @Test
        @DisplayName("CT-MC-12: Aluno com 30 dias de atraso (fronteira exata do teto R$ 50,00) -> 5000 centavos")
        void alunoAtraso30DiasTetoExato() {
            assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 30));
        }

        @Test
        @DisplayName("CT-MC-13: Aluno com 31 dias de atraso (logo após o teto) -> limitado a R$ 50,00 (5000 centavos)")
        void alunoAtraso31DiasTetoAplicado() {
            assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 31));
        }

        @Test
        @DisplayName("CT-MC-14: Aluno com 100 dias de atraso -> limitado a R$ 50,00 (5000 centavos)")
        void alunoAtraso100DiasTetoAplicado() {
            assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 100));
        }

        @Test
        @DisplayName("CT-MC-15: Professor com 0 dias de atraso -> multa 0")
        void professorSemAtraso() {
            assertEquals(0, regras.multaCentavos(TipoUsuario.PROFESSOR, 0));
        }

        @ParameterizedTest(name = "CT-MC-16: Professor carência dia {0} -> multa 0")
        @ValueSource(ints = {1, 2, 3})
        void professorCarencia(int diasAtraso) {
            assertEquals(0, regras.multaCentavos(TipoUsuario.PROFESSOR, diasAtraso));
        }

        @Test
        @DisplayName("CT-MC-17: Professor com 4 dias de atraso (1º dia cobrado) -> R$ 1,00 (100 centavos)")
        void professorAtraso4Dias() {
            assertEquals(100, regras.multaCentavos(TipoUsuario.PROFESSOR, 4));
        }

        @Test
        @DisplayName("CT-MC-18: Professor com 8 dias de atraso (5º dia cobrado) -> R$ 5,00 (500 centavos)")
        void professorAtraso8Dias() {
            assertEquals(500, regras.multaCentavos(TipoUsuario.PROFESSOR, 8));
        }

        @Test
        @DisplayName("CT-MC-19: Professor com 13 dias de atraso (10º dia cobrado) -> 10*100 = R$ 10,00 (1000 centavos)")
        void professorAtraso13Dias() {
            assertEquals(1000, regras.multaCentavos(TipoUsuario.PROFESSOR, 13));
        }

        @Test
        @DisplayName("CT-MC-20: Professor com 14 dias de atraso (11º dia cobrado) -> 1000 + 1*200 = R$ 12,00 (1200 centavos)")
        void professorAtraso14Dias() {
            assertEquals(1200, regras.multaCentavos(TipoUsuario.PROFESSOR, 14));
        }

        @Test
        @DisplayName("CT-MC-21: Professor com 32 dias de atraso (29 dias cobrados) -> 1000 + 19*200 = R$ 48,00 (4800 centavos)")
        void professorAtraso32Dias() {
            assertEquals(4800, regras.multaCentavos(TipoUsuario.PROFESSOR, 32));
        }

        @Test
        @DisplayName("CT-MC-22: Professor com 33 dias de atraso (30 dias cobrados, teto exato) -> R$ 50,00 (5000 centavos)")
        void professorAtraso33DiasTetoExato() {
            assertEquals(5000, regras.multaCentavos(TipoUsuario.PROFESSOR, 33));
        }

        @Test
        @DisplayName("CT-MC-23: Professor com 34 dias de atraso (acima do teto) -> limitado a R$ 50,00 (5000 centavos)")
        void professorAtraso34DiasTetoAplicado() {
            assertEquals(5000, regras.multaCentavos(TipoUsuario.PROFESSOR, 34));
        }

        @Test
        @DisplayName("CT-MC-24: Visitante sem carência com 1 dia de atraso -> R$ 1,00 (100 centavos)")
        void visitanteAtraso1Dia() {
            assertEquals(100, regras.multaCentavos(TipoUsuario.VISITANTE, 1));
        }

        @Test
        @DisplayName("CT-MC-25: Visitante sem carência com 12 dias de atraso -> R$ 14,00 (1400 centavos)")
        void visitanteAtraso12Dias() {
            assertEquals(1400, regras.multaCentavos(TipoUsuario.VISITANTE, 12));
        }
    }

    // =========================================================================
    // 5. podeEmprestar(TipoUsuario tipo, int emprestimosAtivos, int multaPendenteCentavos, boolean possuiAtraso)
    // =========================================================================
    @Nested
    @DisplayName("5. podeEmprestar")
    class PodeEmprestarTests {

        @Test
        @DisplayName("CT-PE-01: Empréstimos ativos negativo (-1) deve lançar IllegalArgumentException")
        void ativosNegativoLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeEmprestar(TipoUsuario.ALUNO, -1, 0, false));
        }

        @Test
        @DisplayName("CT-PE-02: Multa pendente negativa (-1) deve lançar IllegalArgumentException")
        void multaNegativaLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeEmprestar(TipoUsuario.ALUNO, 0, -1, false));
        }

        @Test
        @DisplayName("CT-PE-03: Tipo nulo deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeEmprestar(null, 0, 0, false));
        }

        // Cenários Aluno (limite = 3)
        @Test
        @DisplayName("CT-PE-04: Aluno com 0 ativos, 0 multa, sem atraso -> true")
        void alunoTudoZerado() {
            assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 0, false));
        }

        @Test
        @DisplayName("CT-PE-05: Aluno com 2 ativos (limite - 1), 999 centavos de multa, sem atraso -> true")
        void alunoMultaAbaixoDoLimite() {
            assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 2, 999, false));
        }

        @Test
        @DisplayName("CT-PE-06: Aluno com 2 ativos (limite - 1), 1000 centavos de multa (no limite R$ 10,00), sem atraso -> true")
        void alunoNoLimiteValido() {
            assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 2, 1000, false));
        }

        @Test
        @DisplayName("CT-PE-07: Aluno com 3 ativos (atingiu limite de empréstimos) -> false")
        void alunoAtingiuLimiteEmprestimos() {
            assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 3, 0, false));
        }

        @Test
        @DisplayName("CT-PE-08: Aluno com 4 ativos (acima do limite) -> false")
        void alunoAcimaDoLimiteEmprestimos() {
            assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 4, 0, false));
        }

        @Test
        @DisplayName("CT-PE-09: Aluno com multa de 1001 centavos (acima de R$ 10,00) -> false")
        void alunoMultaAcimaDoLimite() {
            assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 1001, false));
        }

        @Test
        @DisplayName("CT-PE-10: Aluno com atraso ativo -> false")
        void alunoComAtrasoAtivo() {
            assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 0, true));
        }

        // Cenários Professor (limite = 8)
        @Test
        @DisplayName("CT-PE-11: Professor com 7 ativos (limite - 1), multa 1000, sem atraso -> true")
        void professorNoLimiteValido() {
            assertTrue(regras.podeEmprestar(TipoUsuario.PROFESSOR, 7, 1000, false));
        }

        @Test
        @DisplayName("CT-PE-12: Professor com 8 ativos (atingiu limite de empréstimos) -> false")
        void professorAtingiuLimiteEmprestimos() {
            assertFalse(regras.podeEmprestar(TipoUsuario.PROFESSOR, 8, 0, false));
        }

        // Cenários Visitante (limite = 1)
        @Test
        @DisplayName("CT-PE-13: Visitante com 0 ativos, multa 1000, sem atraso -> true")
        void visitanteValido() {
            assertTrue(regras.podeEmprestar(TipoUsuario.VISITANTE, 0, 1000, false));
        }

        @Test
        @DisplayName("CT-PE-14: Visitante com 1 ativo (atingiu limite) -> false")
        void visitanteAtingiuLimite() {
            assertFalse(regras.podeEmprestar(TipoUsuario.VISITANTE, 1, 0, false));
        }

        @Test
        @DisplayName("CT-PE-15: Múltiplas violações (ativos atingidos + multa excessiva + com atraso) -> false")
        void multiplasViolacoes() {
            assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 3, 1001, true));
        }
    }

    // =========================================================================
    // 6. podeRenovar(TipoUsuario tipo, int renovacoesFeitas, boolean possuiReserva, boolean emAtraso)
    // =========================================================================
    @Nested
    @DisplayName("6. podeRenovar")
    class PodeRenovarTests {

        @Test
        @DisplayName("CT-PR-01: Renovações feitas negativo (-1) deve lançar IllegalArgumentException para qualquer tipo")
        void renovacoesNegativoLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeRenovar(TipoUsuario.ALUNO, -1, false, false));
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeRenovar(TipoUsuario.PROFESSOR, -1, false, false));
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeRenovar(TipoUsuario.VISITANTE, -1, false, false));
        }

        @Test
        @DisplayName("CT-PR-02: Tipo nulo deve lançar IllegalArgumentException")
        void tipoNuloLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.podeRenovar(null, 0, false, false));
        }

        // Visitantes NUNCA renovam
        @Test
        @DisplayName("CT-PR-03: Visitante com 0 renovações, sem reserva, sem atraso -> false")
        void visitanteNuncaRenovaMesmoSemReservaESemAtraso() {
            assertFalse(regras.podeRenovar(TipoUsuario.VISITANTE, 0, false, false));
        }

        // Alunos renovam até 2 vezes (feitas = 0 ou 1 -> true; 2 -> false)
        @Test
        @DisplayName("CT-PR-04: Aluno com 0 renovações feitas (vai para a 1ª), sem reserva, sem atraso -> true")
        void alunoRenovacao0() {
            assertTrue(regras.podeRenovar(TipoUsuario.ALUNO, 0, false, false));
        }

        @Test
        @DisplayName("CT-PR-05: Aluno com 1 renovação feita (vai para a 2ª), sem reserva, sem atraso -> true")
        void alunoRenovacao1() {
            assertTrue(regras.podeRenovar(TipoUsuario.ALUNO, 1, false, false));
        }

        @Test
        @DisplayName("CT-PR-06: Aluno com 2 renovações feitas (já atingiu o teto de 2), sem reserva, sem atraso -> false")
        void alunoRenovacao2AtingiuTeto() {
            assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 2, false, false));
        }

        @Test
        @DisplayName("CT-PR-07: Aluno com 3 renovações feitas (acima do teto) -> false")
        void alunoRenovacao3AcimaTeto() {
            assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 3, false, false));
        }

        // Professores renovam até 3 vezes (feitas = 0, 1 ou 2 -> true; 3 -> false)
        @Test
        @DisplayName("CT-PR-08: Professor com 0 renovações feitas -> true")
        void professorRenovacao0() {
            assertTrue(regras.podeRenovar(TipoUsuario.PROFESSOR, 0, false, false));
        }

        @Test
        @DisplayName("CT-PR-09: Professor com 2 renovações feitas (vai para a 3ª) -> true")
        void professorRenovacao2() {
            assertTrue(regras.podeRenovar(TipoUsuario.PROFESSOR, 2, false, false));
        }

        @Test
        @DisplayName("CT-PR-10: Professor com 3 renovações feitas (já atingiu o teto de 3) -> false")
        void professorRenovacao3AtingiuTeto() {
            assertFalse(regras.podeRenovar(TipoUsuario.PROFESSOR, 3, false, false));
        }

        // Restrições de reserva e atraso
        @Test
        @DisplayName("CT-PR-11: Aluno elegível por renovações mas com obra reservada -> false")
        void alunoComReserva() {
            assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, true, false));
        }

        @Test
        @DisplayName("CT-PR-12: Aluno elegível por renovações mas em atraso -> false")
        void alunoEmAtraso() {
            assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, false, true));
        }

        @Test
        @DisplayName("CT-PR-13: Aluno com reserva E em atraso -> false")
        void alunoComReservaEEATraso() {
            assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, true, true));
        }
    }

    // =========================================================================
    // 7. multaTotalCentavos(TipoUsuario tipo, int[] diasAtrasoPorEmprestimo)
    // =========================================================================
    @Nested
    @DisplayName("7. multaTotalCentavos")
    class MultaTotalCentavosTests {

        @Test
        @DisplayName("CT-MT-01: Tipo nulo com lista não vazia deve lançar IllegalArgumentException")
        void tipoNuloComListaLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.multaTotalCentavos(null, new int[]{0}));
        }

        @Test
        @DisplayName("CT-MT-02: Tipo nulo com lista vazia deve lançar IllegalArgumentException")
        void tipoNuloComListaVaziaLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.multaTotalCentavos(null, new int[]{}));
        }

        @Test
        @DisplayName("CT-MT-03: Lista nula deve lançar IllegalArgumentException")
        void listaNulaLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.multaTotalCentavos(TipoUsuario.ALUNO, null));
        }

        @Test
        @DisplayName("CT-MT-04: Lista com elemento negativo [-1] deve lançar IllegalArgumentException")
        void elementoNegativoIsoladoLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{-1}));
        }

        @Test
        @DisplayName("CT-MT-05: Lista com múltiplos elementos onde um é negativo [5, -2, 3] deve lançar IllegalArgumentException")
        void elementoNegativoMeioLancaExcecao() {
            assertThrows(IllegalArgumentException.class, () ->
                regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{5, -2, 3}));
        }

        @Test
        @DisplayName("CT-MT-06: Lista vazia deve resultar em total 0 para qualquer tipo")
        void listaVaziaRetornaZero() {
            assertEquals(0, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{}));
            assertEquals(0, regras.multaTotalCentavos(TipoUsuario.PROFESSOR, new int[]{}));
            assertEquals(0, regras.multaTotalCentavos(TipoUsuario.VISITANTE, new int[]{}));
        }

        @Test
        @DisplayName("CT-MT-07: Lista com 1 empréstimo de 0 dias de atraso -> total 0")
        void umEmprestimoSemAtraso() {
            assertEquals(0, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{0}));
        }

        @Test
        @DisplayName("CT-MT-08: Lista com 1 empréstimo de 5 dias para Aluno -> R$ 5,00 (500 centavos)")
        void umEmprestimoAluno5Dias() {
            assertEquals(500, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{5}));
        }

        @Test
        @DisplayName("CT-MT-09: Lista com múltiplos empréstimos para Aluno [10, 10] -> 1000 + 1000 = 2000 centavos")
        void multiplosEmprestimosAlunoAbaixoDoTeto() {
            assertEquals(2000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{10, 10}));
        }

        @Test
        @DisplayName("CT-MT-10: Múltiplos empréstimos para Professor considerando carência [3, 4] -> 0 + 100 = 100 centavos")
        void multiplosEmprestimosProfessorCarencia() {
            assertEquals(100, regras.multaTotalCentavos(TipoUsuario.PROFESSOR, new int[]{3, 4}));
        }

        @Test
        @DisplayName("CT-MT-11: Dois empréstimos no teto individual [30, 30] para Aluno -> 5000 + 5000 = exatamente R$ 100,00 (10000 centavos)")
        void somaExatamenteNoTetoGlobal() {
            assertEquals(10000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{30, 30}));
        }

        @Test
        @DisplayName("CT-MT-12: Três empréstimos [30, 30, 30] cuja soma daria 15000 -> limitado a R$ 100,00 (10000 centavos)")
        void somaUltrapassaTetoGlobal() {
            assertEquals(10000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{30, 30, 30}));
        }

        @Test
        @DisplayName("CT-MT-13: Empréstimo individual além de R$ 50,00 [40] respeita teto individual de 5000")
        void emprestimoIndividualRespeitaTetoIndividual() {
            assertEquals(5000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{40}));
        }
    }
}
