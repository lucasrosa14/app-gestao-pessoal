package com.lucas.appgestaopessoal;

import com.lucas.appgestaopessoal.habitos.GerenciadorHabitos;
import com.lucas.appgestaopessoal.tarefas.GerenciadorTarefas;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao App de Gestão Pessoal!");
        GerenciadorTarefas gerenciadorTarefas = new GerenciadorTarefas();
        GerenciadorHabitos gerenciadorHabitos = new GerenciadorHabitos();
        Scanner scanner = new Scanner(System.in);

        int opcao;

        do {
            System.out.println("--- Menu Principal ---");
            System.out.println("1. Tarefas");
            System.out.println("2. Hábitos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        gerenciadorTarefas.exibirMenu(scanner);
                        break;
                    case 2:
                        gerenciadorHabitos.exibirMenu(scanner);
                        break;
                    case 0:
                        System.out.println("Encerrando aplicação.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.  Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1;
            }


        } while (opcao != 0);

        scanner.close();
    }

}

