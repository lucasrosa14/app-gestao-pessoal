package com.lucas.appgestaopessoal.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalizeTextoTest {

    @Test
    void deveNormalizarTextoComAcentosECaracteresEspeciais() {
        // Arrange
        String textoOriginal = "Olá, Mundo! Esta é uma Época com Açúcar e Cêcedilha.";
        String textoEsperado = "ola, mundo! esta e uma epoca com acucar e cecedilha.";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "O texto com acentos e caracteres especiais deve ser normalizado corretamente.");
    }

    @Test
    void deveConverterParaMinusculas() {
        // Arrange
        String textoOriginal = "TESTE DE MAIÚSCULAS E minúsculas";
        String textoEsperado = "teste de maiusculas e minusculas";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "O texto deve ser convertido para minúsculas.");
    }

    @Test
    void deveRemoverEspacosExtras() {
        // Arrange
        String textoOriginal = "  Texto com espaços   extras  ";
        String textoEsperado = "texto com espacos extras";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "O método não deve alterar espaços extras.");
    }

    @Test
    void deveLidarComTextoNulo() {
        // Arrange
        String textoOriginal = null;
        String textoEsperado = "";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "Texto nulo deve retornar uma string vazia.");
    }

    @Test
    void deveLidarComTextoVazio() {
        // Arrange
        String textoOriginal = "";
        String textoEsperado = "";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "Texto vazio deve retornar uma string vazia.");
    }

    @Test
    void deveLidarComTextoApenasComNumeros() {
        // Arrange
        String textoOriginal = "12345";
        String textoEsperado = "12345";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "Texto com apenas números deve permanecer inalterado.");
    }

    @Test
    void deveLidarComTextoApenasComCaracteresEspeciaisNaoDiacriticos() {
        // Arrange
        String textoOriginal = "!@#$%^&*()";
        String textoEsperado = "!@#$%^&*()";

        // Act
        String textoNormalizado = NormalizeTexto.normalizeText(textoOriginal);

        // Assert
        assertEquals(textoEsperado, textoNormalizado, "Caracteres especiais não diacríticos devem permanecer inalterados.");
    }
}
