package com.lucas.appgestaopessoal.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdGeneratorTest {

    @BeforeEach
    void setUp() {
        IdGenerator.resetId();
    }

    @Test
    void deveGerarIdsSequenciaisCorretamente() {
        // Arrange & Act
        int id1 = IdGenerator.generateNewId(); // Primeiro ID gerado
        int id2 = IdGenerator.generateNewId(); // Segundo ID gerado
        int id3 = IdGenerator.generateNewId(); // Terceiro ID gerado

        // Assert
        assertEquals(1, id1, "O primeiro ID gerado deve ser 1.");
        assertEquals(2, id2, "O segundo ID gerado deve ser 2.");
        assertEquals(3, id3, "O terceiro ID gerado deve ser 3.");

        // Adicionalmente, você pode verificar que o ID continua incrementando
        int id4 = IdGenerator.generateNewId();
        assertEquals(4, id4, "O ID deve continuar incrementando.");
    }

    @Test
    void deveResetarIdCorretamente() {
        // Arrange
        IdGenerator.generateNewId(); // Gera ID 1
        IdGenerator.generateNewId(); // Gera ID 2
        assertEquals(3, IdGenerator.generateNewId(), "Deve gerar o ID 3 antes do reset."); // Gera ID 3

        // Act
        IdGenerator.resetId(); // Reseta o gerador de IDs

        // Assert
        int idAposReset = IdGenerator.generateNewId();
        assertEquals(1, idAposReset, "O ID gerado após o reset deve ser 1.");

        // Verifica se a sequência continua corretamente após o reset
        int proximoIdAposReset = IdGenerator.generateNewId();
        assertEquals(2, proximoIdAposReset, "O próximo ID após o reset deve ser 2.");
    }

    @Test
    void deveComecarDoIdInicialAposCadaExecucaoDeTeste() {
        // Este teste serve para confirmar que o @BeforeEach está funcionando corretamente.
        // Se este teste falhar, significa que o resetId() no setUp não está limpando o estado.

        // Arrange & Act (apenas gerar o primeiro ID)
        int initialId = IdGenerator.generateNewId();

        // Assert
        assertEquals(1, initialId, "Cada teste deve começar com o ID resetado para 1.");
        // Não é necessário chamar resetId() aqui, pois o @BeforeEach fará isso para o próximo teste.
    }
}
