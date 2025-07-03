package com.lucas.appgestaopessoal;

import com.lucas.appgestaopessoal.tarefas.Tarefa;
import com.lucas.appgestaopessoal.tarefas.GerenciadorTarefas;
import com.lucas.appgestaopessoal.tarefas.TarefaRecorrente;
import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
            System.out.println("2. Adicionar Tarefa Recorrente");
            System.out.println("3. Listar Todas as Tarefas");
            System.out.println("4. Buscar Tarefa por ID");
            System.out.println("5. Buscar Tarefas por Texto");
            System.out.println("6. Concluir Tarefa");
            System.out.println("7. Remover Tarefa");
            System.out.println("8. Atualizar Tarefa");
            System.out.println("9. Visual da Semana");
            System.out.println("10. Sugerir Tarefa Prioritária");
            System.out.println("11. Executar Testes Temporários");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção para Tarefas: ");

            try {
                opcaoTarefas = scanner.nextInt();
                scanner.nextLine();

                switch (opcaoTarefas) {
                    case 1:
                        adicionarTarefaSimples(gerenciadorTarefas, scanner);
                        break;
                    case 2:
                        adicionarTarefaRecorrente(gerenciadorTarefas, scanner);
                        break;
                    case 3:
                        listarTodasTarefas(gerenciadorTarefas);
                        break;
                    case 4:
                        buscarTarefaPorId(gerenciadorTarefas, scanner);
                        break;
                    case 5:
                        buscarTarefasPorTexto(gerenciadorTarefas, scanner);
                        break;
                    case 6:
                        concluirTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 7:
                        removerTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 8:
                        atualizarTarefa(gerenciadorTarefas, scanner);
                        break;
                    case 9:
                        visualDaSemana(gerenciadorTarefas);
                        break;
                    case 10:
                        sugerirTarefa(gerenciadorTarefas);
                        break;
                    case 11:
                        executaTestesTemporarios(gerenciadorTarefas);
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

    private static void adicionarTarefaSimples(GerenciadorTarefas gerenciador, Scanner scanner) {
        System.out.print("Digite a descrição da tarefa simples: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data de vencimento (DD-MM-AAAA): ");
        String dataStr = scanner.nextLine();
        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(dataStr, DATE_FORMATTER);
            if (dataVencimento.isBefore(LocalDate.now())) {
                System.out.println("A data de vencimento não pode ser anterior ao dia de hoje. Tarefa não adicionada.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use DD-MM-AAAA. Tarefa não adicionada.");
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

        gerenciador.adicionarTarefa(descricao, dataVencimento, prioridade);
    }

    private static void adicionarTarefaRecorrente(GerenciadorTarefas gerenciador, Scanner scanner) {
        System.out.print("Digite a descrição da tarefa recorrente: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data da PRIMEIRA ocorrência (vencimento inicial) (DD-MM-AAAA): ");
        String dataVencimentoStr = scanner.nextLine();
        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(dataVencimentoStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use DD-MM-AAAA. Tarefa recorrente não adicionada.");
            return;
        }

        System.out.print("Digite a prioridade (BAIXA, MEDIA, ALTA, URGENTE): ");
        String prioridadeStr = scanner.nextLine().toUpperCase();
        Prioridade prioridade;
        try {
            prioridade = Prioridade.valueOf(prioridadeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Prioridade inválida. Use BAIXA, MEDIA, ALTA ou URGENTE. Tarefa recorrente não adicionada.");
            return;
        }

        System.out.print("Digite a frequência (DIARIA, SEMANAL, QUINZENAL, MENSAL, ANUAL): ");
        String frequenciaStr = scanner.nextLine().toUpperCase();
        FrequenciaRecorrencia frequencia;
        try {
            frequencia = FrequenciaRecorrencia.valueOf(frequenciaStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Frequência inválida. Use DIARIA, SEMANAL, QUINZENAL, MENSAL ou ANUAL. Tarefa recorrente não adicionada.");
            return;
        }

        System.out.print("Digite a data de início da recorrência (DD-MM-AAAA) [Pressione Enter para usar a data da primeira ocorrência]: ");
        String dataInicioRecorrenciaStr = scanner.nextLine();
        LocalDate dataInicioRecorrencia = dataVencimento; // Default
        if (!dataInicioRecorrenciaStr.isBlank()) {
            try {
                dataInicioRecorrencia = LocalDate.parse(dataInicioRecorrenciaStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data de início inválido. Usando a data da primeira ocorrência.");
            }
        }

        System.out.print("Digite a data de fim da recorrência (DD-MM-AAAA) [Pressione Enter para sem data de fim]: ");
        String dataFimRecorrenciaStr = scanner.nextLine();
        LocalDate dataFimRecorrencia = null; // Default: sem data de fim
        if (!dataFimRecorrenciaStr.isBlank()) {
            try {
                dataFimRecorrencia = LocalDate.parse(dataFimRecorrenciaStr, DATE_FORMATTER);
                if (dataFimRecorrencia.isBefore(LocalDate.now())) {
                    System.out.println("A data de fim de recorrência não pode ser anterior ao dia de hoje. Tarefa não adicionada.");
                    return; // Impede que a tarefa seja adicionada
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data de fim inválido. A tarefa não terá data de fim.");
            }
        }

        // Cria a TarefaRecorrente e a adiciona ao gerenciador
        int novoId = IdGenerator.generateNewId();
        TarefaRecorrente novaTarefaRecorrente = new TarefaRecorrente(
                novoId, descricao, dataVencimento, prioridade,
                frequencia, dataInicioRecorrencia, dataFimRecorrencia
        );

        gerenciador.adicionarTarefa(novaTarefaRecorrente);

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

            System.out.print("Digite a NOVA data de vencimento (DD-MM-AAAA) (atual: " + tarefaExistente.getDataVencimento() + ") [Pressione Enter para manter]: ");
            String novaDataStrInput = scanner.nextLine();
            LocalDate novaDataVencimento = tarefaExistente.getDataVencimento();
            if (!novaDataStrInput.isBlank()) {
                try {
                    novaDataVencimento = LocalDate.parse(novaDataStrInput, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use DD-MM-AAAA. Data de vencimento NÃO atualizada.");

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

    private static void executaTestesTemporarios(GerenciadorTarefas gerenciadorTarefas) {
        /* Testes Temporários */


        System.out.println("\n--- Gerenciador de tarefas instanciado ---");

        System.out.println("\n--- Adicionando Tarefas ---");
        gerenciadorTarefas.adicionarTarefa("Comprar pão e leite", LocalDate.of(2025, 7, 3), Prioridade.ALTA);
        gerenciadorTarefas.adicionarTarefa("Pagar contas da casa", LocalDate.of(2025, 7, 5), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Estudar Java por 1 hora", LocalDate.of(2025, 7, 2), Prioridade.MEDIA);
        gerenciadorTarefas.adicionarTarefa("Responder e-mails pendentes", LocalDate.of(2025, 7, 4), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Pagar contas da casa", LocalDate.of(2025, 7, 4), Prioridade.URGENTE);

        System.out.println("\n--- Listando todas as Tarefas ---");
        List<Tarefa> todasAsTarefas = gerenciadorTarefas.listarTarefas();
        if (todasAsTarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Tarefa t : todasAsTarefas) {
                System.out.println("ID: " + t.getId() +
                        ", Descrição: " + t.getDescricao() +
                        ", Vencimento: " + t.getDataVencimento() +
                        ", Prioridade: " + t.getPrioridade() +
                        ", Concluída: " + t.isConcluido());
            }
        }

        System.out.println("\n--- Buscando Tarefa por ID (ID: 2) ---");
        Tarefa tarefaEncontrada = gerenciadorTarefas.buscarTarefaPorId(2);
        if (tarefaEncontrada != null) {
            System.out.println("Tarefa encontrada: " + tarefaEncontrada.getDescricao());
        } else {
            System.out.println("Tarefa com ID 2 não encontrada.");
        }

        System.out.println("\n--- Buscando Tarefa por ID (ID: 99 - inexistente) ---");
        Tarefa tarefaNaoEncontrada = gerenciadorTarefas.buscarTarefaPorId(99);
        if (tarefaNaoEncontrada != null) {
            System.out.println("Tarefa encontrada: " + tarefaNaoEncontrada.getDescricao());
        } else {
            System.out.println("Tarefa com ID 99 não encontrada.");
        }

        System.out.println("\n--- Marcando Tarefa com ID 1 como Concluída ---");
        gerenciadorTarefas.concluirTarefa(1);

        System.out.println("\n--- Buscando Tarefas por Texto: 'Java' ---");
        List<Tarefa> tarefasJava = gerenciadorTarefas.buscarTarefaPorTexto("Java");
        if (tarefasJava.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'Java'.");
        } else {
            for (Tarefa t : tarefasJava) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Buscando Tarefas por Texto: 'contas' ---");
        List<Tarefa> tarefasContas = gerenciadorTarefas.buscarTarefaPorTexto("contas");
        if (tarefasContas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'contas'.");
        } else {
            for (Tarefa t : tarefasContas) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Buscando Tarefas por Texto: 'projeto' (case-insensitive) ---");
        List<Tarefa> tarefasProjeto = gerenciadorTarefas.buscarTarefaPorTexto("PROJETO");
        if (tarefasProjeto.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'PROJETO'.");
        } else {
            for (Tarefa t : tarefasProjeto) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Listando Tarefas Após Marcar Concluída ---");
        for (Tarefa t : gerenciadorTarefas.listarTarefas()) {
            System.out.println("ID: " + t.getId() +
                    ", Descrição: " + t.getDescricao() +
                    ", Concluída: " + t.isConcluido());
        }

        System.out.println("\n--- Atualizando Tarefa com ID 3 ---");
        Tarefa tarefaAtualizada = new Tarefa(3, "Estudar Java avançado por 2 horas", LocalDate.of(2025, 7, 7), Prioridade.ALTA);
        gerenciadorTarefas.atualizarTarefa(tarefaAtualizada);

        System.out.println("\n--- Listando Tarefas Após Atualização ---");
        for (Tarefa t : gerenciadorTarefas.listarTarefas()) {
            System.out.println("ID: " + t.getId() +
                    ", Descrição: " + t.getDescricao() +
                    ", Vencimento: " + t.getDataVencimento() +
                    ", Prioridade: " + t.getPrioridade());
        }

        // 7. Remover uma tarefa
        System.out.println("\n--- Removendo Tarefa com ID 4 ---");
        gerenciadorTarefas.removerTarefa(4);

        System.out.println("\n--- Tentando remover tarefa inexistente (ID 100) ---");
        gerenciadorTarefas.removerTarefa(100);

        System.out.println("\n--- Listando Tarefas Finais ---");
        todasAsTarefas = gerenciadorTarefas.listarTarefas();
        if (todasAsTarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa restante.");
        } else {
            for (Tarefa t : todasAsTarefas) {
                System.out.println("ID: " + t.getId() +
                        ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\nTestes do Módulo de Tarefas Concluídos.");

        System.out.println("\n--- TESTE: Visual da Semana");
        List<Tarefa> minhasTarefasDaSemana = gerenciadorTarefas.listarTarefasDaSemana();
        if (minhasTarefasDaSemana.isEmpty()) {
            System.out.println("Nenhuma tarefa para esta semana!");
        } else {
            for (Tarefa t : minhasTarefasDaSemana) {
                System.out.println(t);
            }
        }

        System.out.println("\n---  Teste: Sugestão de Tarefa Prioritária ---");
        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        if (tarefaSugerida.isPresent()) {
            System.out.println("Sua próxima tarefa prioritária sugerida é:");
            System.out.println(tarefaSugerida.get());
        } else {
            System.out.println("Não há tarefas pendentes para sugerir no momento");
        }

        System.out.println("\n--- Testando Tarefa Recorrente ---");
        // ID será 6 se você adicionou as 5 tarefas anteriores
        gerenciadorTarefas.adicionarTarefa(new TarefaRecorrente(
                IdGenerator.generateNewId(), // Novo ID
                "Pagar aluguel",
                LocalDate.of(2025, 7, 5), // Vence dia 5 de Julho
                Prioridade.URGENTE,
                FrequenciaRecorrencia.MENSAL,
                LocalDate.of(2025, 1, 5), // Começou em 5 de Janeiro
                LocalDate.of(2025, 12, 31)
        ));

        Tarefa tarefaAluguel = gerenciadorTarefas.buscarTarefaPorId(6); // Supondo ID 6
        if (tarefaAluguel != null) {
            System.out.println("Tarefa de Aluguel ANTES de concluir: " + tarefaAluguel);
            tarefaAluguel.concluir(); // Conclui a tarefa de aluguel
            System.out.println("Tarefa de Aluguel DEPOIS de concluir: " + tarefaAluguel);
        }

        System.out.println("\nTestes do Módulo de Tarefas Concluídos.");


        /* Fim dos testes temporários */
    }

}

