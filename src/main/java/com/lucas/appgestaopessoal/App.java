package com.lucas.appgestaopessoal;

import com.lucas.appgestaopessoal.tarefas.Tarefa;
import com.lucas.appgestaopessoal.tarefas.GerenciadorTarefas;
import com.lucas.appgestaopessoal.util.Prioridade;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao App de Gestão Pessoal!");

        GerenciadorTarefas gerenciadorTarefas = new GerenciadorTarefas();
        Scanner scanner = new Scanner(System.in);

        int opcao;

        do {
            System.out.println("--- Menu Principal ---");
            System.out.println("1. Tarefas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        menuTarefas(gerenciadorTarefas, scanner);
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

    private static void menuTarefas(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        int opcaoTarefas;

        do {
            System.out.println("\n--- Menu de Tarefas ---");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Todas as Tarefas");
            System.out.println("3. Buscar Tarefa por ID");
            System.out.println("4. Buscar Tarefas por Texto");
            System.out.println("5. Concluir Tarefa");
            System.out.println("6. Remover Tarefa");
            System.out.println("7. Atualizar Tarefa");
            System.out.println("8. Visual da Semana");
            System.out.println("9. Sugerir Tarefa Prioritária");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção para Tarefas: ");

            try {
                opcaoTarefas = scanner.nextInt();
                ;
                scanner.nextLine();

                switch (opcaoTarefas) {

                    case 1:
                        adicionarTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 2:
                        listarTodasTarefas(gerenciadorTarefas);
                        break;
                    case 3:
                        buscarTarefaPorId(gerenciadorTarefas, scanner);
                        break;
                    case 4:
                        buscarTarefasPorTexto(gerenciadorTarefas, scanner);
                        break;
                    case 5:
                        concluirTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 6:
                        removerTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 7:
                        atualizarTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 8:
                        visualDaSemana(gerenciadorTarefas);
                        break;
                    case 9:
                        sugerirTarefa(gerenciadorTarefas);
                        break;
                    case 0:
                        System.out.println("Voltando ao Menu Principal...");
                        break;
                    default:
                        System.out.println("Opção inválida para Tarefas. Tente novamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                opcaoTarefas = -1;
            }

        } while (opcaoTarefas != 0);
    }

    private static void adicionarTarefa(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        System.out.print("Digite a descrição da tarefa: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data de vencimento (AAAA-MM-DD): ");
        String dataStr = scanner.nextLine();
        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use AAAA-MM-DD. Tarefa não adicionada.");
            return;
        }

        System.out.print("Digite a prioridade (BAIXA, MEDIA, ALTA, URGENTE): ");
        String prioridadeStr = scanner.nextLine().toUpperCase();
        Prioridade prioridade;
        try {
            prioridade = Prioridade.valueOf(prioridadeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Prioridade inválida. Use BAIXA, MEDIA, ALTA ou URGENTE. Tarefa não adicionada.");
            return;
        }

        gerenciadorTarefas.adicionarTarefa(descricao, dataVencimento, prioridade);
    }

    private static void listarTodasTarefas(GerenciadorTarefas gerenciadorTarefas) {
        List<Tarefa> tarefas = gerenciadorTarefas.listarTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            System.out.println("\n--- Lista de Todas as Tarefas ---");
            for (Tarefa tarefa : tarefas) {
                System.out.println(tarefa);
            }
        }
    }

    private static void buscarTarefaPorId(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        System.out.print("Digite o ID da tarefa para buscar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Tarefa tarefa = gerenciadorTarefas.buscarTarefaPorId(id);
            if (tarefa != null) {
                System.out.println("\n--- Tarefa Encontrada ---");
                System.out.println(tarefa);
            } else {
                System.out.println("Tarefa com ID " + id + " não encontrada.");
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private static void buscarTarefasPorTexto(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        System.out.print("Digite o texto para buscar nas descrições: ");
        String textoBusca = scanner.nextLine();

        List<Tarefa> tarefasEncontradas = gerenciadorTarefas.buscarTarefaPorTexto(textoBusca);
        if (tarefasEncontradas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com o texto '" + textoBusca + "'.");
        } else {
            System.out.println("\n--- Tarefas Encontradas por Texto: '" + textoBusca + "' ---");
            for (Tarefa tarefa : tarefasEncontradas) {
                System.out.println(tarefa);
            }
        }
    }

    private static void concluirTarefa(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        listarTodasTarefas(gerenciadorTarefas);
        System.out.print("Digite o ID da tarefa para concluir: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            gerenciadorTarefas.concluirTarefa(id);
        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private static void removerTarefa(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        listarTodasTarefas(gerenciadorTarefas);
        System.out.print("Digite o ID da tarefa para remover: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            gerenciadorTarefas.removerTarefa(id);
        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private static void atualizarTarefa(GerenciadorTarefas gerenciadorTarefas, Scanner scanner) {
        listarTodasTarefas(gerenciadorTarefas);
        System.out.print("Digite o ID da tarefa que deseja atualizar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Tarefa tarefaExistente = gerenciadorTarefas.buscarTarefaPorId(id);
            if (tarefaExistente == null) {
                System.out.println("Tarefa com ID " + id + " não encontrada para atualização.");
                return;
            }

            System.out.print("Digite a NOVA descrição da tarefa (atual: " + tarefaExistente.getDescricao() + ") [Pressione Enter para manter]: ");
            String novaDescricaoInput = scanner.nextLine();
            String novaDescricao = novaDescricaoInput.isBlank() ? tarefaExistente.getDescricao() : novaDescricaoInput;

            System.out.print("Digite a NOVA data de vencimento (AAAA-MM-DD) (atual: " + tarefaExistente.getDataVencimento() + ") [Pressione Enter para manter]: ");
            String novaDataStrInput = scanner.nextLine();
            LocalDate novaDataVencimento = tarefaExistente.getDataVencimento();
            if (!novaDataStrInput.isBlank()) {
                try {
                    novaDataVencimento = LocalDate.parse(novaDataStrInput);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use AAAA-MM-DD. Data de vencimento NÃO atualizada.");

                }
            }

            System.out.print("Digite a NOVA prioridade (BAIXA, MEDIA, ALTA, URGENTE) (atual: " + tarefaExistente.getPrioridade() + ") [Pressione Enter para manter]: ");
            String novaPrioridadeStrInput = scanner.nextLine().toUpperCase();
            Prioridade novaPrioridade = tarefaExistente.getPrioridade();
            if (!novaPrioridadeStrInput.isBlank()) {
                try {
                    novaPrioridade = Prioridade.valueOf(novaPrioridadeStrInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("Prioridade inválida. Use BAIXA, MEDIA, ALTA ou URGENTE. Prioridade NÃO atualizada.");
                }
            }

            Tarefa tarefaAtualizada = new Tarefa(
                    tarefaExistente.getId(),
                    novaDescricao,
                    novaDataVencimento,
                    novaPrioridade,
                    tarefaExistente.isConcluido()
            );

            gerenciadorTarefas.atualizarTarefa(tarefaAtualizada);

        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private static void visualDaSemana(GerenciadorTarefas gerenciadorTarefas) {
        List<Tarefa> tarefasDaSemana = gerenciadorTarefas.listarTarefasDaSemana();
        if (tarefasDaSemana.isEmpty()) {
            System.out.println("Nenhuma tarefa para esta semana.");
        } else {
            for (Tarefa tarefa : tarefasDaSemana) {
                System.out.println(tarefa);
            }
        }
    }

    private static void sugerirTarefa(GerenciadorTarefas gerenciadorTarefas) {
        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        if (tarefaSugerida.isPresent()) {
            System.out.println("\nSua próxima tarefa prioritária sugerida é:");
            System.out.println(tarefaSugerida.get());
        } else {
            System.out.println("Não há tarefas pendentes para sugerir no momento.");
        }
    }
}

