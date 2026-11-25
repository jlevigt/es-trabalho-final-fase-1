package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Testes de Caixa-Preta: multaCentavos")
class MultaCentavosTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // --- PARTIÇÕES INVÁLIDAS ---
    @Test
    @DisplayName("CT-MC-01: Dias de atraso negativo (-1) deve lançar IllegalArgumentException [AVL]")
    void atrasoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(TipoUsuario.ALUNO, -1));
    }

    @Test
    @DisplayName("CT-MC-02: Tipo nulo com atraso válido deve lançar IllegalArgumentException [PE]")
    void tipoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(null, 5));
    }

    @Test
    @DisplayName("CT-MC-03: Tipo nulo e atraso negativo deve lançar IllegalArgumentException [PE/AVL]")
    void tipoNuloEAtrasoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> regras.multaCentavos(null, -1));
    }

    // --- ALUNO (Sem carência, dias cobrados = dias de atraso) ---
    @Test
    @DisplayName("CT-MC-04: Aluno com 0 dias de atraso -> multa 0 [AVL]")
    void alunoSemAtraso() {
        assertEquals(0, regras.multaCentavos(TipoUsuario.ALUNO, 0));
    }

    @Test
    @DisplayName("CT-MC-05: Aluno com 1 dia de atraso (limite inf 1ª faixa) -> R$ 1,00 (100 centavos) [AVL]")
    void alunoAtraso1Dia() {
        assertEquals(100, regras.multaCentavos(TipoUsuario.ALUNO, 1));
    }

    @Test
    @DisplayName("CT-MC-06: Aluno com 5 dias de atraso (valor médio 1ª faixa) -> R$ 5,00 (500 centavos) [PE]")
    void alunoAtraso5Dias() {
        assertEquals(500, regras.multaCentavos(TipoUsuario.ALUNO, 5));
    }

    @Test
    @DisplayName("CT-MC-07: Aluno com 10 dias de atraso (limite sup 1ª faixa) -> R$ 10,00 (1000 centavos) [AVL]")
    void alunoAtraso10Dias() {
        assertEquals(1000, regras.multaCentavos(TipoUsuario.ALUNO, 10));
    }

    @Test
    @DisplayName("CT-MC-08: Aluno com 11 dias de atraso (limite inf 2ª faixa) -> 10*100 + 1*200 = R$ 12,00 (1200 centavos) [AVL]")
    void alunoAtraso11Dias() {
        assertEquals(1200, regras.multaCentavos(TipoUsuario.ALUNO, 11));
    }

    @Test
    @DisplayName("CT-MC-09: Aluno com 12 dias de atraso (exemplo do enunciado) -> 10*100 + 2*200 = R$ 14,00 (1400 centavos) [PE]")
    void alunoAtraso12DiasExemploEnunciado() {
        assertEquals(1400, regras.multaCentavos(TipoUsuario.ALUNO, 12));
    }

    @Test
    @DisplayName("CT-MC-10: Aluno com 30 dias de atraso (limite exato do teto R$ 50,00: 10*100 + 20*200) -> 5000 centavos [AVL]")
    void alunoAtraso30DiasTetoExato() {
        assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 30));
    }

    @Test
    @DisplayName("CT-MC-11: Aluno com 31 dias de atraso (limite + 1 com teto aplicado) -> 5000 centavos [AVL]")
    void alunoAtraso31DiasTetoAplicado() {
        assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 31));
    }

    @Test
    @DisplayName("CT-MC-12: Aluno com 100 dias de atraso (muito acima do teto) -> 5000 centavos [PE]")
    void alunoAtraso100DiasTetoAplicado() {
        assertEquals(5000, regras.multaCentavos(TipoUsuario.ALUNO, 100));
    }

    // --- PROFESSOR (Com carência de 3 dias) ---
    @Test
    @DisplayName("CT-MC-13: Professor com 0 dias de atraso -> multa 0 [AVL]")
    void professorSemAtraso() {
        assertEquals(0, regras.multaCentavos(TipoUsuario.PROFESSOR, 0));
    }

    @ParameterizedTest(name = "CT-MC-14: Professor carência dia {0} -> multa 0 [AVL]")
    @ValueSource(ints = {1, 2, 3})
    void professorCarencia(int diasAtraso) {
        assertEquals(0, regras.multaCentavos(TipoUsuario.PROFESSOR, diasAtraso));
    }

    @Test
    @DisplayName("CT-MC-15: Professor com 4 dias de atraso (1º dia cobrado) -> R$ 1,00 (100 centavos) [AVL]")
    void professorAtraso4Dias() {
        assertEquals(100, regras.multaCentavos(TipoUsuario.PROFESSOR, 4));
    }

    @Test
    @DisplayName("CT-MC-16: Professor com 8 dias de atraso (5º dia cobrado) -> R$ 5,00 (500 centavos) [PE]")
    void professorAtraso8Dias() {
        assertEquals(500, regras.multaCentavos(TipoUsuario.PROFESSOR, 8));
    }

    @Test
    @DisplayName("CT-MC-17: Professor com 13 dias de atraso (10º dia cobrado, limite sup faixa 1) -> 1000 centavos [AVL]")
    void professorAtraso13Dias() {
        assertEquals(1000, regras.multaCentavos(TipoUsuario.PROFESSOR, 13));
    }

    @Test
    @DisplayName("CT-MC-18: Professor com 14 dias de atraso (11º dia cobrado, limite inf faixa 2) -> 1200 centavos [AVL]")
    void professorAtraso14Dias() {
        assertEquals(1200, regras.multaCentavos(TipoUsuario.PROFESSOR, 14));
    }

    @Test
    @DisplayName("CT-MC-19: Professor com 33 dias de atraso (limite exato do teto: 3 carência + 30 cobrados) -> 5000 centavos [AVL]")
    void professorAtraso33DiasTetoExato() {
        assertEquals(5000, regras.multaCentavos(TipoUsuario.PROFESSOR, 33));
    }

    @Test
    @DisplayName("CT-MC-20: Professor com 34 dias de atraso (limite + 1 com teto aplicado) -> 5000 centavos [AVL]")
    void professorAtraso34DiasTetoAplicado() {
        assertEquals(5000, regras.multaCentavos(TipoUsuario.PROFESSOR, 34));
    }

    // --- VISITANTE (Sem carência, equivalente a Aluno) ---
    @Test
    @DisplayName("CT-MC-21: Visitante sem carência com 1 dia de atraso -> R$ 1,00 (100 centavos) [AVL]")
    void visitanteAtraso1Dia() {
        assertEquals(100, regras.multaCentavos(TipoUsuario.VISITANTE, 1));
    }

    @Test
    @DisplayName("CT-MC-22: Visitante sem carência com 12 dias de atraso -> R$ 14,00 (1400 centavos) [PE]")
    void visitanteAtraso12Dias() {
        assertEquals(1400, regras.multaCentavos(TipoUsuario.VISITANTE, 12));
    }
}
