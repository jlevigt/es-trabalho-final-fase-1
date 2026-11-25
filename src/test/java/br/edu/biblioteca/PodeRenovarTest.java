package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: podeRenovar")
class PodeRenovarTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // --- ENTRADAS INVÁLIDAS ---
    @Test
    @DisplayName("CT-PR-01: Renovações feitas negativo (-1) deve lançar IllegalArgumentException para qualquer tipo [AVL]")
    void renovacoesNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeRenovar(TipoUsuario.ALUNO, -1, false, false));
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeRenovar(TipoUsuario.PROFESSOR, -1, false, false));
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeRenovar(TipoUsuario.VISITANTE, -1, false, false));
    }

    @Test
    @DisplayName("CT-PR-02: Tipo nulo deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeRenovar(null, 0, false, false));
    }

    // --- VISITANTE (Nunca renova) ---
    @Test
    @DisplayName("CT-PR-03: Visitante com 0 renovações, sem reserva, sem atraso -> false [PE]")
    void visitanteNuncaRenovaMesmoSemReservaESemAtraso() {
        assertFalse(regras.podeRenovar(TipoUsuario.VISITANTE, 0, false, false));
    }

    // --- ALUNO (Até 2 renovações) ---
    @Test
    @DisplayName("CT-PR-04: Aluno com 0 renovações feitas (vai para a 1ª) -> true [AVL]")
    void alunoRenovacao0() {
        assertTrue(regras.podeRenovar(TipoUsuario.ALUNO, 0, false, false));
    }

    @Test
    @DisplayName("CT-PR-05: Aluno com 1 renovação feita (vai para a 2ª) -> true [AVL]")
    void alunoRenovacao1() {
        assertTrue(regras.podeRenovar(TipoUsuario.ALUNO, 1, false, false));
    }

    @Test
    @DisplayName("CT-PR-06: Aluno com 2 renovações feitas (já atingiu o teto de 2) -> false [AVL]")
    void alunoRenovacao2AtingiuTeto() {
        assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 2, false, false));
    }

    @Test
    @DisplayName("CT-PR-07: Aluno com 3 renovações feitas (acima do teto) -> false [AVL]")
    void alunoRenovacao3AcimaTeto() {
        assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 3, false, false));
    }

    // --- PROFESSOR (Até 3 renovações) ---
    @Test
    @DisplayName("CT-PR-08: Professor com 0 renovações feitas -> true [AVL]")
    void professorRenovacao0() {
        assertTrue(regras.podeRenovar(TipoUsuario.PROFESSOR, 0, false, false));
    }

    @Test
    @DisplayName("CT-PR-09: Professor com 2 renovações feitas (vai para a 3ª) -> true [AVL]")
    void professorRenovacao2() {
        assertTrue(regras.podeRenovar(TipoUsuario.PROFESSOR, 2, false, false));
    }

    @Test
    @DisplayName("CT-PR-10: Professor com 3 renovações feitas (já atingiu o teto de 3) -> false [AVL]")
    void professorRenovacao3AtingiuTeto() {
        assertFalse(regras.podeRenovar(TipoUsuario.PROFESSOR, 3, false, false));
    }

    // --- RESTRIÇÕES DE RESERVA E ATRASO ---
    @Test
    @DisplayName("CT-PR-11: Aluno com renovações = 0 mas obra com reserva -> false [PE]")
    void alunoComReserva() {
        assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, true, false));
    }

    @Test
    @DisplayName("CT-PR-12: Aluno com renovações = 0 mas em atraso -> false [PE]")
    void alunoEmAtraso() {
        assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, false, true));
    }

    @Test
    @DisplayName("CT-PR-13: Aluno com reserva E em atraso -> false [PE]")
    void alunoComReservaEEATraso() {
        assertFalse(regras.podeRenovar(TipoUsuario.ALUNO, 0, true, true));
    }
}
