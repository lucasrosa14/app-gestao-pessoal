package com.lucas.appgestaopessoal.habitos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GerenciadorHabitos {

    private final List<Habito> habitos;

    public GerenciadorHabitos() {
        this.habitos = new ArrayList<>();

    }

    public void exibirMenu(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- Menu de Hábitos ---");
            System.out.println("1. Cadastrar hábito");
            System.out.println("2. Marcar hábito feito hoje");
            System.out.println("3. Listar Hábitos ativos");
            System.out.println("4. Listar todos os Hábitos");
            System.out.println("5. Ver histórico do Hábito");
            System.out.println("6. Editar Hábito");
            System.out.println("7. Alterar status do Hábito");
            System.out.println("8. Remover Hábito");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarHabito(scanner);
                case 2 -> marcarHabitoFeito(scanner);
                case 3 -> listarHabitos(true);
                case 4 -> listarHabitos(false);
                case 5 -> verProgresso(scanner);
                case 6 -> editarHabito(scanner);
                case 7 -> alterarStatusHabito(scanner);
                case 8 -> removerHabito(scanner);
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
        System.out.println("Hábito ID: " + habito.getId() + " cadastrado com sucesso!");
    }

    public void listarHabitos(boolean somenteAtivos) {
        List<Habito> listaFiltrada = somenteAtivos
                ? habitos.stream().filter(Habito::isAtivo).toList()
                : habitos;

        if (listaFiltrada.isEmpty()) {
            System.out.println(somenteAtivos ? "Nenhum hábito ativo encontrado." : "Nenhum hábito cadastrado.");
            return;
        }

        System.out.println(somenteAtivos ? "\n--- Hábitos Ativos ---" : "\n--- Todos os Hábitos ---");
        for (Habito h : listaFiltrada) {
            System.out.println(h.getId() + ": " + h.getNome() + " - Ativo: " + (h.isAtivo() ? "Sim" : "Não"));
        }
    }

    private void marcarHabitoFeito(Scanner scanner) {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\nHábitos disponíveis:");
        listarHabitos(true);

        System.out.print("ID do hábito que deseja marcar como feito hoje: ");
        System.out.print("ID do hábito: ");
        int id = scanner.nextInt();

        Optional<Habito> habitoOpt = encontrarPorId(id);
        if (habitoOpt.isPresent()) {
            Habito habito = habitoOpt.get();
            habito.getDiasFeitos().add(LocalDate.now());
            System.out.println("Hábito ID: " + habito.getId() + " marcado como feito hoje.");
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
        listarHabitos(false);

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

    private void alterarStatusHabito(Scanner scanner) {
        if (habitos.isEmpty()) {
            System.out.println("Nenhum hábito cadastrado.");
            return;
        }

        System.out.println("\nHábitos disponíveis:");
        listarHabitos(false);  // Mostra todos (ativos e inativos)

        System.out.print("ID do hábito: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // consumir o enter

        Optional<Habito> habitoOpt = encontrarPorId(id);
        if (habitoOpt.isEmpty()) {
            System.out.println("Hábito não encontrado.");
            return;
        }

        Habito habito = habitoOpt.get();
        String statusAtual = habito.isAtivo() ? "ATIVO" : "INATIVO";
        System.out.println("Hábito selecionado: " + habito.getNome() + " (Status atual: " + statusAtual + ")");

        System.out.print("Deseja (A)tivar ou (D)esativar este hábito? ");
        String escolha = scanner.nextLine().trim().toUpperCase();

        if ("A".equals(escolha)) {
            if (habito.isAtivo()) {
                System.out.println("O hábito já está ativo.");
            } else {
                habito.setAtivo(true);
                System.out.println("Hábito ativado.");
            }
        } else if ("D".equals(escolha)) {
            if (!habito.isAtivo()) {
                System.out.println("O hábito já está desativado.");
            } else {
                habito.setAtivo(false);
                System.out.println("Hábito desativado.");
            }
        } else {
            System.out.println("Opção inválida. Nenhuma alteração foi feita.");
        }
    }

    public void editarHabito(Scanner scanner) {
        listarHabitos(false);
        System.out.print("Digite o ID do hábito que deseja editar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        Habito habito = habitos.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);

        if (habito == null) {
            System.out.println("Hábito não encontrado.");
            return;
        }

        System.out.print("Novo nome (atual: " + habito.getNome() + "): ");
        habito.setNome(scanner.nextLine());

        System.out.print("Nova descrição (atual: " + habito.getDescricaoHabito() + "): ");
        habito.setDescricaoHabito(scanner.nextLine());

        System.out.println("Hábito atualizado com sucesso.");
    }

    public void removerHabito(Scanner scanner) {
        listarHabitos(false);
        System.out.print("Digite o ID do hábito que deseja remover: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        boolean removido = habitos.removeIf(h -> h.getId() == id);

        if (removido) {
            System.out.println("Hábito removido com sucesso.");
        } else {
            System.out.println("Hábito não encontrado.");
        }
    }

    private Optional<Habito> encontrarPorId(int id) {
        return habitos.stream().filter(h -> h.getId() == id).findFirst();
    }
}
