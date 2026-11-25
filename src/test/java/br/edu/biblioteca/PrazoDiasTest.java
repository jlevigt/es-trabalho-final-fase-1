package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: prazoDias")
class PrazoDiasTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    @Test
    @DisplayName("CT-PD-01: Aluno deve ter prazo de 14 dias [PE]")
    void prazoAluno() {
        assertEquals(14, regras.prazoDias(TipoUsuario.ALUNO));
    }

    @Test
    @DisplayName("CT-PD-02: Professor deve ter prazo de 30 dias [PE]")
    void prazoProfessor() {
        assertEquals(30, regras.prazoDias(TipoUsuario.PROFESSOR));
    }

    @Test
    @DisplayName("CT-PD-03: Visitante deve ter prazo de 7 dias [PE]")
    void prazoVisitante() {
        assertEquals(7, regras.prazoDias(TipoUsuario.VISITANTE));
    }

    @Test
    @DisplayName("CT-PD-04: Tipo nulo deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.prazoDias(null));
    }
}
