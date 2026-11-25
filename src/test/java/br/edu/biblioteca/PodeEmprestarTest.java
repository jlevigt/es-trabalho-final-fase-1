package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: podeEmprestar")
class PodeEmprestarTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // --- ENTRADAS INVÁLIDAS ---
    @Test
    @DisplayName("CT-PE-01: Empréstimos ativos negativo (-1) deve lançar IllegalArgumentException [AVL]")
    void ativosNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeEmprestar(TipoUsuario.ALUNO, -1, 0, false));
    }

    @Test
    @DisplayName("CT-PE-02: Multa pendente negativa (-1) deve lançar IllegalArgumentException [AVL]")
    void multaNegativaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeEmprestar(TipoUsuario.ALUNO, 0, -1, false));
    }

    @Test
    @DisplayName("CT-PE-03: Tipo nulo deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.podeEmprestar(null, 0, 0, false));
    }

    // --- ALUNO (Limite = 3) ---
    @Test
    @DisplayName("CT-PE-04: Aluno com 0 ativos, 0 multa, sem atraso -> true [PE]")
    void alunoTudoZerado() {
        assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 0, false));
    }

    @Test
    @DisplayName("CT-PE-05: Aluno com 2 ativos (limite - 1), 999 centavos de multa (limite - 1), sem atraso -> true [AVL]")
    void alunoMultaAbaixoDoLimite() {
        assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 2, 999, false));
    }

    @Test
    @DisplayName("CT-PE-06: Aluno com 2 ativos (limite - 1), 1000 centavos de multa (no limite R$ 10,00), sem atraso -> true [AVL]")
    void alunoNoLimiteValido() {
        assertTrue(regras.podeEmprestar(TipoUsuario.ALUNO, 2, 1000, false));
    }

    @Test
    @DisplayName("CT-PE-07: Aluno com 3 ativos (atingiu limite de empréstimos) -> false [AVL]")
    void alunoAtingiuLimiteEmprestimos() {
        assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 3, 0, false));
    }

    @Test
    @DisplayName("CT-PE-08: Aluno com 4 ativos (acima do limite) -> false [AVL]")
    void alunoAcimaDoLimiteEmprestimos() {
        assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 4, 0, false));
    }

    @Test
    @DisplayName("CT-PE-09: Aluno com multa de 1001 centavos (acima de R$ 10,00) -> false [AVL]")
    void alunoMultaAcimaDoLimite() {
        assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 1001, false));
    }

    @Test
    @DisplayName("CT-PE-10: Aluno com atraso ativo -> false [PE]")
    void alunoComAtrasoAtivo() {
        assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 0, 0, true));
    }

    // --- PROFESSOR (Limite = 8) ---
    @Test
    @DisplayName("CT-PE-11: Professor com 7 ativos (limite - 1), multa 1000, sem atraso -> true [AVL]")
    void professorNoLimiteValido() {
        assertTrue(regras.podeEmprestar(TipoUsuario.PROFESSOR, 7, 1000, false));
    }

    @Test
    @DisplayName("CT-PE-12: Professor com 8 ativos (atingiu limite de empréstimos) -> false [AVL]")
    void professorAtingiuLimiteEmprestimos() {
        assertFalse(regras.podeEmprestar(TipoUsuario.PROFESSOR, 8, 0, false));
    }

    // --- VISITANTE (Limite = 1) ---
    @Test
    @DisplayName("CT-PE-13: Visitante com 0 ativos, multa 1000, sem atraso -> true [AVL]")
    void visitanteValido() {
        assertTrue(regras.podeEmprestar(TipoUsuario.VISITANTE, 0, 1000, false));
    }

    @Test
    @DisplayName("CT-PE-14: Visitante com 1 ativo (atingiu limite) -> false [AVL]")
    void visitanteAtingiuLimite() {
        assertFalse(regras.podeEmprestar(TipoUsuario.VISITANTE, 1, 0, false));
    }

    // --- COMBINAÇÃO MÚLTIPLA ---
    @Test
    @DisplayName("CT-PE-15: Múltiplas violações simultâneas (ativos no limite + multa excessiva + atraso) -> false [PE]")
    void multiplasViolacoes() {
        assertFalse(regras.podeEmprestar(TipoUsuario.ALUNO, 3, 1001, true));
    }
}
