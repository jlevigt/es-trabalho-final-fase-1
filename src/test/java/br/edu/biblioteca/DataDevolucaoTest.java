package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: dataDevolucao")
class DataDevolucaoTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // --- ALUNO (Prazo 14 dias) ---
    @Test
    @DisplayName("CT-DD-01: Aluno soma 14 dias caindo em dia útil (quarta-feira) -> mantém dia [PE]")
    void alunoDiaUtil() {
        LocalDate retirada = LocalDate.of(2026, 3, 4); // Quarta-feira
        assertEquals(LocalDate.of(2026, 3, 18), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
    }

    @Test
    @DisplayName("CT-DD-02: Aluno soma 14 dias caindo em sábado (sábado é normal) -> mantém sábado [AVL]")
    void alunoCaindoSabado() {
        LocalDate retirada = LocalDate.of(2026, 3, 7); // Sábado
        assertEquals(LocalDate.of(2026, 3, 21), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
    }

    @Test
    @DisplayName("CT-DD-03: Aluno soma 14 dias caindo em domingo -> passa para segunda-feira [AVL]")
    void alunoCaindoDomingoPassaSegunda() {
        LocalDate retirada = LocalDate.of(2026, 3, 8); // Domingo + 14 = Domingo 2026-03-22 -> Segunda 2026-03-23
        assertEquals(LocalDate.of(2026, 3, 23), regras.dataDevolucao(retirada, TipoUsuario.ALUNO));
    }

    // --- PROFESSOR (Prazo 30 dias) ---
    @Test
    @DisplayName("CT-DD-04: Professor soma 30 dias caindo em dia útil (quarta-feira) -> mantém dia [PE]")
    void professorDiaUtil() {
        LocalDate retirada = LocalDate.of(2026, 6, 1); // Segunda + 30 = Quarta 2026-07-01
        assertEquals(LocalDate.of(2026, 7, 1), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
    }

    @Test
    @DisplayName("CT-DD-05: Professor soma 30 dias caindo em sábado (sábado é normal) -> mantém sábado [AVL]")
    void professorCaindoSabado() {
        LocalDate retirada = LocalDate.of(2026, 4, 30); // Quinta + 30 = Sábado 2026-05-30
        assertEquals(LocalDate.of(2026, 5, 30), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
    }

    @Test
    @DisplayName("CT-DD-06: Professor soma 30 dias caindo em domingo -> passa para segunda-feira [AVL]")
    void professorCaindoDomingoPassaSegunda() {
        LocalDate retirada = LocalDate.of(2026, 5, 1); // Sexta + 30 = Domingo 2026-05-31 -> Segunda 2026-06-01
        assertEquals(LocalDate.of(2026, 6, 1), regras.dataDevolucao(retirada, TipoUsuario.PROFESSOR));
    }

    // --- VISITANTE (Prazo 7 dias) ---
    @Test
    @DisplayName("CT-DD-07: Visitante soma 7 dias caindo em dia útil (quinta-feira) -> mantém dia [PE]")
    void visitanteDiaUtil() {
        LocalDate retirada = LocalDate.of(2026, 3, 5); // Quinta + 7 = Quinta 2026-03-12
        assertEquals(LocalDate.of(2026, 3, 12), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
    }

    @Test
    @DisplayName("CT-DD-08: Visitante soma 7 dias caindo em sábado (sábado é normal) -> mantém sábado [AVL]")
    void visitanteCaindoSabado() {
        LocalDate retirada = LocalDate.of(2026, 3, 7); // Sábado + 7 = Sábado 2026-03-14
        assertEquals(LocalDate.of(2026, 3, 14), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
    }

    @Test
    @DisplayName("CT-DD-09: Visitante soma 7 dias caindo em domingo -> passa para segunda-feira [AVL]")
    void visitanteCaindoDomingoPassaSegunda() {
        LocalDate retirada = LocalDate.of(2026, 3, 8); // Domingo + 7 = Domingo 2026-03-15 -> Segunda 2026-03-16
        assertEquals(LocalDate.of(2026, 3, 16), regras.dataDevolucao(retirada, TipoUsuario.VISITANTE));
    }

    // --- ENTRADAS INVÁLIDAS ---
    @Test
    @DisplayName("CT-DD-10: Retirada nula deve lançar IllegalArgumentException [PE]")
    void retiradaNulaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(null, TipoUsuario.ALUNO));
    }

    @Test
    @DisplayName("CT-DD-11: Tipo nulo com retirada válida deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        LocalDate retirada = LocalDate.of(2026, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(retirada, null));
    }

    @Test
    @DisplayName("CT-DD-12: Retirada e tipo nulos devem lançar IllegalArgumentException [PE]")
    void ambosNulosLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.dataDevolucao(null, null));
    }
}
