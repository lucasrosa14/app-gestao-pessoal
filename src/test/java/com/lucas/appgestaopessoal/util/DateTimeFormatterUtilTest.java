package com.lucas.appgestaopessoal.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeFormatterUtilTest {

    @Test
    void deveFormatarDataCorretamente() {
        // Arrange
        LocalDate data = LocalDate.of(2025, 7, 12); // Ex: 12 de julho de 2025
        String dataEsperada = "12-07-2025";

        // Act
        String dataFormatada = data.format(DateTimeFormatterUtil.DATE_FORMATTER);

        // Assert
        assertNotNull(dataFormatada, "A data formatada não deve ser nula.");
        assertEquals(dataEsperada, dataFormatada, "A data deve ser formatada no padrão 'dd-MM-yyyy'.");
    }

    @Test
    void deveFormatarDataDeInicioDeMes() {
        // Arrange
        LocalDate data = LocalDate.of(2024, 02, 1);
        String dataEsperada = "01-02-2024";

        // Act
        String dataFormatada = data.format(DateTimeFormatterUtil.DATE_FORMATTER);

        // Assert
        assertEquals(dataEsperada, dataFormatada, "Deve formatar corretamente o início de um mês.");
    }

    @Test
    void deveFormatarDataDeFimDeMes() {
        // Arrange
        LocalDate data = LocalDate.of(2023, 12, 31);
        String dataEsperada = "31-12-2023";

        // Act
        String dataFormatada = data.format(DateTimeFormatterUtil.DATE_FORMATTER);

        // Assert
        assertEquals(dataEsperada, dataFormatada, "Deve formatar corretamente o fim de um mês.");
    }

    @Test
    void deveFazerParseDeDataCorretamente() {
        // Arrange
        String dataString = "25-03-2020";
        LocalDate dataEsperada = LocalDate.of(2020, 3, 25);

        // Act
        // Usa o formatador para fazer o parse da string de volta para LocalDate
        LocalDate dataConvertida = LocalDate.parse(dataString, DateTimeFormatterUtil.DATE_FORMATTER);

        // Assert
        assertNotNull(dataConvertida, "A data convertida não deve ser nula.");
        assertEquals(dataEsperada, dataConvertida, "Deve fazer o parse da string no padrão 'dd-MM-yyyy' corretamente.");
    }

    @Test
    void naoDeveFazerParseComFormatoInvalido() {
        // Arrange
        String dataStringInvalida = "2020/03/25"; // Formato diferente do esperado "dd-MM-yyyy"

        // Assert
        // Espera-se que uma DateTimeParseException seja lançada
        assertThrows(DateTimeParseException.class, () -> {
            // Act
            LocalDate.parse(dataStringInvalida, DateTimeFormatterUtil.DATE_FORMATTER);
        }, "Deve lançar DateTimeParseException para formato de data inválido.");
    }
}
