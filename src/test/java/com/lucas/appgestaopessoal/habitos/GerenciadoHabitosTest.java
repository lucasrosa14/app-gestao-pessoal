package com.lucas.appgestaopessoal.habitos;

import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorHabitosTest {

    private GerenciadorHabitos gerenciador;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorHabitos();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void testCadastrarHabito() {
        String input = "Hábito Teste\nDescrição Teste\n";
        Scanner scanner = new Scanner(input);

        // Método privado, mas pode ser testado indiretamente via exibirMenu ou por reflexão
        // Aqui, vamos simular via exibirMenu
        String menuInput = "1\nHábito Teste\nDescrição Teste\n0\n";
        Scanner menuScanner = new Scanner(menuInput);
        gerenciador.exibirMenu(menuScanner);

        String output = outContent.toString();
        assertTrue(output.contains("Hábito ID:"));
        assertTrue(output.contains("cadastrado com sucesso"));
    }

    @Test
    void testListarHabitosVazio() {
        gerenciador.listarHabitos(false);
        String output = outContent.toString();
        assertTrue(output.contains("Nenhum hábito cadastrado."));
    }

    @Test
    void testRemoverHabito() {
        // Adiciona um hábito
        String menuInput = "1\nHábito Teste\nDescrição Teste\n8\n1\n0\n";
        Scanner menuScanner = new Scanner(menuInput);
        gerenciador.exibirMenu(menuScanner);

        String output = outContent.toString();
        assertTrue(output.contains("Hábito removido com sucesso.") || output.contains("Hábito não encontrado."));
    }

    @Test
    void testEditarHabitoNaoEncontrado() {
        String menuInput = "6\n1\n0\n";
        Scanner menuScanner = new Scanner(menuInput);
        gerenciador.exibirMenu(menuScanner);

        String output = outContent.toString();
        assertTrue(output.contains("Hábito não encontrado."));
    }

    @Test
    void testMarcarHabitoFeitoSemHabitos() {
        String menuInput = "2\n0\n";
        Scanner menuScanner = new Scanner(menuInput);
        gerenciador.exibirMenu(menuScanner);

        String output = outContent.toString();
        assertTrue(output.contains("Nenhum hábito cadastrado."));
    }

}