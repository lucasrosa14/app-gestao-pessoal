package com.lucas.appgestaopessoal.habitos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GerenciadorHabitos {

    private List<Habito> habitos;

    public GerenciadorHabitos() {
        this.habitos = new ArrayList<>();

    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- Menu de Hábitos ---");
            System.out.println("1. Cadastrar hábito");
            System.out.println("2. Listar hábitos");
            System.out.println("3. Marcar hábito feito hoje");
            System.out.println("4. Ver progresso do hábito");
            System.out.println("5. Desativar hábito");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarHabito(scanner);
                case 2 -> listarHabitos();
                case 3 -> marcarHabitoFeito(scanner);
                case 4 -> verProgresso(scanner);
                case 5 -> desativarHabito(scanner);
                case 0 -> System.out.println("Saindo do módulo de hábitos...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private void cadastrarHabito(Scanner scanner) {
        System.out.print("Nome do hábito: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição do hábito: ");
        String descricao = scanner.nextLine();

        Habito habito = new Habito(nome, descricao);

        habitos.add(habito);
        System.out.println("Hábito cadastrado com sucesso!");
    }

    private void listarHabitos() {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\n--- Lista de Hábitos ---");
        for (Habito h : habitos) {
            System.out.println(h.getId() + ": " + h.getNome() + " - Ativo: " + (h.isAtivo() ? "Sim" : "Não"));
        }
    }

    private void marcarHabitoFeito(Scanner scanner) {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\nHábitos disponíveis:");
        listarHabitos();

        System.out.print("ID do hábito que deseja marcar como feito hoje: ");System.out.print("ID do hábito: ");
        int id = scanner.nextInt();

        Optional<Habito> habitoOpt = encontrarPorId(id);
        if (habitoOpt.isPresent()) {
            Habito habito = habitoOpt.get();
            habito.getDiasFeitos().add(LocalDate.now());
            System.out.println("Hábito marcado como feito hoje.");
        } else {
            System.out.println("Hábito não encontrado.");
        }
    }

    private void verProgresso(Scanner scanner) {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\nHábitos disponíveis:");
        listarHabitos();

        System.out.print("ID do hábito para ver o progresso: ");
        int id = scanner.nextInt();

        Optional<Habito> habitoOpt = encontrarPorId(id);
        if (habitoOpt.isPresent()) {
            Habito habito = habitoOpt.get();
            System.out.println("Progresso do hábito '" + habito.getNome() + "':");
            System.out.println("Feito em " + habito.getDiasFeitos().size() + " dia(s).");
            for (LocalDate data : habito.getDiasFeitos()) {
                System.out.println("- " + data);
            }
        } else {
            System.out.println("Hábito não encontrado.");
        }
    }

    private void desativarHabito(Scanner scanner) {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\nHábitos disponíveis:");
        listarHabitos();

        System.out.print("ID do hábito para desativar: ");System.out.print("ID do hábito: ");
        int id = scanner.nextInt();

        Optional<Habito> habitoOpt = encontrarPorId(id);
        if (habitoOpt.isPresent()) {
            habitoOpt.get().setAtivo(false);
            System.out.println("Hábito desativado.");
        } else {
            System.out.println("Hábito não encontrado.");
        }
    }

    private Optional<Habito> encontrarPorId(int id) {
        return habitos.stream().filter(h -> h.getId() == id).findFirst();
    }
}
