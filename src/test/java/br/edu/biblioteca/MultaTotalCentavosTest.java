package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Caixa-Preta: multaTotalCentavos")
class MultaTotalCentavosTest {

    private RegrasBiblioteca regras;

    @BeforeEach
    void setUp() {
        regras = new RegrasBiblioteca();
    }

    // --- ENTRADAS INVÁLIDAS ---
    @Test
    @DisplayName("CT-MT-01: Tipo nulo com lista não vazia deve lançar IllegalArgumentException [PE]")
    void tipoNuloComListaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.multaTotalCentavos(null, new int[]{0}));
    }

    @Test
    @DisplayName("CT-MT-02: Tipo nulo com lista vazia deve lançar IllegalArgumentException [PE]")
    void tipoNuloComListaVaziaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.multaTotalCentavos(null, new int[]{}));
    }

    @Test
    @DisplayName("CT-MT-03: Lista nula deve lançar IllegalArgumentException [PE]")
    void listaNulaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.multaTotalCentavos(TipoUsuario.ALUNO, null));
    }

    @Test
    @DisplayName("CT-MT-04: Lista com elemento negativo [-1] deve lançar IllegalArgumentException [AVL]")
    void elementoNegativoIsoladoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{-1}));
    }

    @Test
    @DisplayName("CT-MT-05: Lista com múltiplos elementos onde um é negativo [5, -2, 3] deve lançar IllegalArgumentException [PE]")
    void elementoNegativoMeioLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
            regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{5, -2, 3}));
    }

    // --- CASO ESPECIAL: LISTA VAZIA ---
    @Test
    @DisplayName("CT-MT-06: Lista vazia deve resultar em total 0 para qualquer tipo [PE]")
    void listaVaziaRetornaZero() {
        assertEquals(0, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{}));
        assertEquals(0, regras.multaTotalCentavos(TipoUsuario.PROFESSOR, new int[]{}));
        assertEquals(0, regras.multaTotalCentavos(TipoUsuario.VISITANTE, new int[]{}));
    }

    // --- LISTAS DE TAMANHO 1 ---
    @Test
    @DisplayName("CT-MT-07: Lista com 1 empréstimo de 0 dias de atraso -> total 0 [AVL]")
    void umEmprestimoSemAtraso() {
        assertEquals(0, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{0}));
    }

    @Test
    @DisplayName("CT-MT-08: Lista com 1 empréstimo de 5 dias para Aluno -> R$ 5,00 (500 centavos) [PE]")
    void umEmprestimoAluno5Dias() {
        assertEquals(500, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{5}));
    }

    @Test
    @DisplayName("CT-MT-09: Empréstimo individual além de R$ 50,00 [40] respeita teto individual de 5000 [PE]")
    void emprestimoIndividualRespeitaTetoIndividual() {
        assertEquals(5000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{40}));
    }

    // --- MÚLTIPLOS EMPRÉSTIMOS ---
    @Test
    @DisplayName("CT-MT-10: Múltiplos empréstimos para Aluno abaixo do teto global [10, 10] -> 2000 centavos [PE]")
    void multiplosEmprestimosAlunoAbaixoDoTeto() {
        assertEquals(2000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{10, 10}));
    }

    @Test
    @DisplayName("CT-MT-11: Múltiplos empréstimos para Professor considerando carência [3, 4] -> 0 + 100 = 100 centavos [PE]")
    void multiplosEmprestimosProfessorCarencia() {
        assertEquals(100, regras.multaTotalCentavos(TipoUsuario.PROFESSOR, new int[]{3, 4}));
    }

    @Test
    @DisplayName("CT-MT-12: Dois empréstimos no teto individual [30, 30] -> exatamente no teto global R$ 100,00 (10000 centavos) [AVL]")
    void somaExatamenteNoTetoGlobal() {
        assertEquals(10000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{30, 30}));
    }

    @Test
    @DisplayName("CT-MT-13: Três empréstimos [30, 30, 30] cuja soma daria 15000 -> limitado ao teto global de 10000 centavos [PE]")
    void somaUltrapassaTetoGlobal() {
        assertEquals(10000, regras.multaTotalCentavos(TipoUsuario.ALUNO, new int[]{30, 30, 30}));
    }
}
