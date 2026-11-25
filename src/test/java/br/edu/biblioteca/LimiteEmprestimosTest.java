package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: limiteEmprestimos")
class LimiteEmprestimosTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    @Test
    @DisplayName("CT-LE-01: Aluno pode ter até 3 empréstimos simultâneos [PE]")
    void limiteAluno() {
        assertEquals(3, regras.limiteEmprestimos(TipoUsuario.ALUNO));
    }

    @Test
    @DisplayName("CT-LE-02: Professor pode ter até 8 empréstimos simultâneos [PE]")
    void limiteProfessor() {
        assertEquals(8, regras.limiteEmprestimos(TipoUsuario.PROFESSOR));
    }

    @Test
    @DisplayName("CT-LE-03: Visitante pode ter até 1 empréstimo simultâneo [PE]")
    void limiteVisitante() {
        assertEquals(1, regras.limiteEmprestimos(TipoUsuario.VISITANTE));
    }

    @Test
    @DisplayName("CT-LE-04: Tipo nulo deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.limiteEmprestimos(null));
    }
}
