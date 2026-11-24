package br.edu.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Exemplo de teste. O resultado esperado vem da ESPECIFICAÇÃO (Anexo A do enunciado),
 * nunca do que o sistema devolve. Se o teste falhar, vocês podem ter encontrado um defeito.
 */
class ExemploTest {

    private final RegrasBiblioteca regras = new RegrasBiblioteca();

    @Test
    void alunoTemPrazoDe14Dias() {
        assertEquals(14, regras.prazoDias(TipoUsuario.ALUNO));
    }
}
