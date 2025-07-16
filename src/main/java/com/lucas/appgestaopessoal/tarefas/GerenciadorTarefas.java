package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.lucas.appgestaopessoal.util.NormalizeTexto.normalizeText;


public class GerenciadorTarefas {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final List<Tarefa> tarefas;

    public GerenciadorTarefas() {
        this.tarefas = new ArrayList<>();
    }

    // Dentro da classe GerenciadorTarefas

    public void exibirMenu(Scanner scanner) {
        int opcaoTarefas;

        do {
            System.out.println("\n--- Menu de Tarefas ---");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Adicionar Tarefa Recorrente");
            System.out.println("3. Listar Todas as Tarefas");
            System.out.println("4. Listar Tarefas Simples");
            System.out.println("5. Listar Tarefas Recorrentes");
            System.out.println("6. Listar Tarefas Pendentes");
            System.out.println("7. Listar Tarefas Concluídas");
            System.out.println("8. Listar Tarefas Canceladas");
            System.out.println("9. Listar Tarefas por Prioridade");
            System.out.println("10. Listar Tarefas por Vencimento");
            System.out.println("11. Buscar Tarefa por ID");
            System.out.println("12. Buscar Tarefas por Texto");
            System.out.println("13. Concluir/Cancelar Tarefa");
            System.out.println("14. Excluir Tarefa");
            System.out.println("15. Atualizar Tarefa");
            System.out.println("16. Visual da Semana");
            System.out.println("17. Sugerir Tarefa Prioritária");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção para Tarefas: ");

            try {
                opcaoTarefas = scanner.nextInt();
                scanner.nextLine();

                switch (opcaoTarefas) {
                    case 1:
                        adicionarTarefaSimples(scanner);
                        break;
                    case 2:
                        adicionarTarefaRecorrente(scanner);
                        break;
                    case 3:
                        listarTarefas(scanner, "todas");
                        break;
                    case 4:
                        listarTarefas(scanner, "simples");
                        break;
                    case 5:
                        listarTarefas(scanner, "recorrentes");
                        break;
                    case 6:
                        listarTarefas(scanner, "pendentes");
                        break;
                    case 7:
                        listarTarefas(scanner, "concluidas");
                        break;
                    case 8:
                        listarTarefas(scanner, "canceladas");
                        break;
                    case 9:
                        listarTarefasPorPrioridade(scanner);
                        break;
                    case 10:
                        listarTarefasPorVencimento(scanner);
                        break;
                    case 11:
                        buscarTarefaPorId(scanner);
                        break;
                    case 12:
                        buscarTarefasPorTexto(scanner);
                        break;
                    case 13:
                        concluirOuCancelarTarefa(scanner);
                        break;
                    case 14:
                        removerTarefa(scanner);
                        break;
                    case 15:
                        atualizarTarefa(scanner);
                        break;
                    case 16:
                        visualDaSemana();
                        break;
                    case 17:
                        sugerirTarefa();
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


    public Tarefa adicionarTarefa(String descricao, LocalDate dataVencimento, Prioridade prioridade) {
        int id = IdGenerator.generateNewId();
        Tarefa tarefa = new Tarefa(id, descricao, dataVencimento, prioridade, StatusTarefa.PENDENTE, LocalDate.now());
        this.tarefas.add(tarefa);
        System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + tarefa.getId() + "') adicionada com sucesso!");
        return tarefa;
    }

    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
        System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + tarefa.getId() + ") adicionada com sucesso!");
    }

    public List<Tarefa> listarTodasTarefas() {
        return new ArrayList<>(this.tarefas);
    }

    public List<Tarefa> listarTarefasSimples() {
        return tarefas.stream()
                .filter(tarefa -> !(tarefa instanceof TarefaRecorrente))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasRecorrentes() {
        return tarefas.stream()
                .filter(tarefa -> tarefa instanceof TarefaRecorrente)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasPendentes() {
        return tarefas.stream()
                .filter(tarefa -> tarefa.getStatus() == StatusTarefa.PENDENTE)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasConcluidas() {
        return tarefas.stream()
                .filter(tarefa -> tarefa.getStatus() == StatusTarefa.CONCLUIDA)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasCanceladas() {
        return tarefas.stream()
                .filter(tarefa -> tarefa.getStatus() == StatusTarefa.CANCELADA)
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasOrdenadasPorVencimento() {
        return tarefas.stream()
                .sorted(Comparator.comparing(Tarefa::getDataVencimento))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasOrdenadasPorPrioridade() {
        return tarefas.stream()
                .sorted(Comparator
                        .comparing(Tarefa::getPrioridade)
                )
                .collect(Collectors.toList());
    }

    public Tarefa buscarTarefaPorId(int id) {
        for (Tarefa tarefa : this.tarefas) {
            if (tarefa.getId() == id) {
                return tarefa;
            }
        }
        return null;
    }

    public List<Tarefa> buscarTarefaPorTexto(String textoBusca) {
        String textoBuscaNormalizado = normalizeText(textoBusca);

        return tarefas.stream()
                .filter(tarefa -> normalizeText(tarefa.getDescricao()).contains(textoBuscaNormalizado))
                .collect(Collectors.toList());
    }

    public void removerTarefa(int id) {
        Tarefa tarefaParaRemover = buscarTarefaPorId(id);
        if (tarefaParaRemover != null) {
            this.tarefas.remove(tarefaParaRemover);
            System.out.println("Tarefa com ID " + id + " removida com sucesso!");
            return;
        }
        System.out.println("Tarefa com ID " + id + " não encontrada.");
    }

    public boolean concluirTarefa(int id) {
        Tarefa tarefa = buscarTarefaPorId(id);
        if (tarefa instanceof TarefaRecorrente) {
            // Buscar todas as ocorrências pendentes da mesma tarefa (mesma descrição)
            List<TarefaRecorrente> ocorrenciasPendentes = tarefas.stream()
                    .filter(t -> t instanceof TarefaRecorrente)
                    .map(t -> (TarefaRecorrente) t)
                    .filter(t -> t.getDescricao().equals(tarefa.getDescricao()))
                    .filter(t -> t.getStatus() == StatusTarefa.PENDENTE)
                    .sorted(Comparator.comparing(Tarefa::getDataVencimento).thenComparing(Tarefa::getId))
                    .collect(Collectors.toList());

            if (ocorrenciasPendentes.isEmpty()) {
                System.out.println("Nenhuma ocorrência pendente encontrada para essa tarefa recorrente.");
                return false;
            }

            // Concluir a ocorrência mais antiga
            TarefaRecorrente maisAntiga = ocorrenciasPendentes.get(0);
            maisAntiga.concluirComRegistro(this.tarefas);
            System.out.println("Ocorrência concluída: " + maisAntiga.getDescricao() + " (ID: " + maisAntiga.getId() + ")");
            return true;
        } else if (tarefa != null) {
            tarefa.concluir();
            System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") marcada como CONCLUÍDA.");
            return true;
        } else {
            System.out.println("Tarefa com ID " + id + " não encontrada.");
            return false;
        }
    }


    public boolean cancelarTarefa(int id) {
        Tarefa tarefa = buscarTarefaPorId(id);
        if (tarefa instanceof TarefaRecorrente) {
            // Buscar todas as ocorrências pendentes da mesma tarefa (mesma descrição)
            List<TarefaRecorrente> ocorrenciasPendentes = tarefas.stream()
                    .filter(t -> t instanceof TarefaRecorrente)
                    .map(t -> (TarefaRecorrente) t)
                    .filter(t -> t.getDescricao().equals(tarefa.getDescricao()))
                    .filter(t -> t.getStatus() == StatusTarefa.PENDENTE)
                    .sorted(Comparator.comparing(Tarefa::getDataVencimento).thenComparing(Tarefa::getId))
                    .collect(Collectors.toList());

            if (ocorrenciasPendentes.isEmpty()) {
                System.out.println("Nenhuma ocorrência pendente encontrada para essa tarefa recorrente.");
                return false;
            }

            // Cancelar a ocorrência mais antiga
            TarefaRecorrente maisAntiga = ocorrenciasPendentes.get(0);
            maisAntiga.cancelarComRegistro(this.tarefas);
            System.out.println("Ocorrência cancelada: " + maisAntiga.getDescricao() + " (ID: " + maisAntiga.getId() + ")");
            return true;
        } else if (tarefa != null) {
            tarefa.cancelar();
            System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") marcada como CANCELADA.");
            return true;
        } else {
            System.out.println("Tarefa com ID " + id + " não encontrada.");
            return false;
        }
    }

    public void atualizarTarefa(Tarefa tarefaAtualizada) {
        Tarefa tarefaExistente = buscarTarefaPorId(tarefaAtualizada.getId());
        if (tarefaExistente != null) {
            tarefaAtualizada.setStatus(tarefaExistente.getStatus());

            int index = tarefas.indexOf(tarefaExistente);
            tarefas.set(index, tarefaAtualizada);
            System.out.println("Tarefa com ID " + tarefaAtualizada.getId() + " atualizada com sucesso!");
        } else {
            System.out.println("Tarefa com ID " + tarefaAtualizada.getId() + " não encontrada para atualização.");
        }
    }

    public List<Tarefa> listarTarefasDaSemana() {
        List<Tarefa> tarefasDaSemana = new ArrayList<>();

        LocalDate hoje = LocalDate.now();
        LocalDate inicioDaSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate fimDaSemana = inicioDaSemana.plusDays(6);

        System.out.println("--- Tarefas da Semana (De " + inicioDaSemana.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")) +
                " a " + fimDaSemana.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")) + ") ---");

        for (Tarefa tarefa : this.tarefas) {
            LocalDate dataVencimento = tarefa.getDataVencimento();
            if (!dataVencimento.isBefore(inicioDaSemana) && !dataVencimento.isAfter(fimDaSemana)) {
                tarefasDaSemana.add(tarefa);
            }
        }

        return tarefasDaSemana;


    }

    public Optional<Tarefa> sugerirTarefaPrioritaria() {
        List<Tarefa> tarefasPendentes = new ArrayList<>();

        for (Tarefa tarefa : this.tarefas) {
            if (!tarefa.isConcluido() && !tarefa.isCancelada()) {
                tarefasPendentes.add(tarefa);
            }
        }

        if (tarefasPendentes.isEmpty()) {
            return Optional.empty();
        }

        Tarefa tarefaSugerida = Collections.min(tarefasPendentes, (t1, t2) -> {

            int comparacaoPrioridade = Integer.compare(t1.getPrioridade().ordinal(), t2.getPrioridade().ordinal());

            if (comparacaoPrioridade != 0) {
                return comparacaoPrioridade;
            } else {
                return t1.getDataVencimento().compareTo(t2.getDataVencimento());
            }
        });
        return Optional.of(tarefaSugerida);
    }

    private void adicionarTarefaSimples(Scanner scanner) {
        System.out.print("Digite a descrição da tarefa simples: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data de vencimento (DD-MM-AAAA) [Pressione ENTER para data atual]: ");
        String dataStr = scanner.nextLine();
        LocalDate dataVencimento;

        if (dataStr.isBlank()) { // Se a string da data estiver vazia (usuário apertou Enter)
            dataVencimento = LocalDate.now(); // Define a data de vencimento como o dia atual
            System.out.println("Data de vencimento definida para HOJE: " + dataVencimento.format(DATE_FORMATTER));
        } else {
            try {
                dataVencimento = LocalDate.parse(dataStr, DATE_FORMATTER);
                if (!dataVencimento.isAfter(LocalDate.now().minusDays(1))) {
                    System.out.println("A data de vencimento não pode ser anterior ao dia de hoje. Tarefa não adicionada.");
                    return;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use DD-MM-AAAA. Tarefa não adicionada.");
                return;
            }
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

        this.adicionarTarefa(descricao, dataVencimento, prioridade);
    }

    private void adicionarTarefaRecorrente(Scanner scanner) {
        System.out.print("Digite a descrição da tarefa recorrente: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a data da PRIMEIRA ocorrência (DD-MM-AAAA): ");
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

        this.adicionarTarefa(novaTarefaRecorrente);

    }

    private void listarTarefas(Scanner scanner, String tipoLista) {
        List<? extends Tarefa> listaParaExibir;
        String tituloLista;

        switch (tipoLista) {
            case "todas":
                listaParaExibir = this.listarTodasTarefas();
                tituloLista = "Todas as Tarefas";
                break;
            case "simples":
                listaParaExibir = this.listarTarefasSimples();
                tituloLista = "Apenas Tarefas Simples";
                break;
            case "recorrentes":
                listaParaExibir = this.listarTarefasRecorrentes();
                tituloLista = "Apenas Tarefas Recorrentes";
                break;
            case "pendentes":
                listaParaExibir = this.listarTarefasPendentes();
                tituloLista = "Apenas Tarefas Pendentes";
                break;
            case "concluidas":
                listaParaExibir = this.listarTarefasConcluidas();
                tituloLista = "Apenas Tarefas Concluídas";
                break;
            case "canceladas":
                listaParaExibir = this.listarTarefasCanceladas();
                tituloLista = "Apenas Tarefas Canceladas";
                break;
            default:
                System.out.println("Tipo de lista inválido.");
                return;
        }

        System.out.println("\n--- Lista de " + tituloLista + " ---");
        if (listaParaExibir.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Tarefa tarefa : listaParaExibir) {
                System.out.println(tarefa);
            }
        }
    }

    private void listarTarefasPorVencimento(Scanner scanner) {
        System.out.println("\n--- Tarefas Ordenadas por Vencimento ---");
        List<Tarefa> tarefasOrdenadas = this.listarTarefasOrdenadasPorVencimento();

        if (tarefasOrdenadas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            tarefasOrdenadas.forEach(System.out::println);
        }
    }

    private void listarTarefasPorPrioridade(Scanner scanner) {
        System.out.println("\n--- Tarefas Ordenadas por Prioridade ---");
        List<Tarefa> tarefasOrdenadas = this.listarTarefasOrdenadasPorPrioridade();

        if (tarefasOrdenadas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            tarefasOrdenadas.forEach(System.out::println);
        }
    }

    private void buscarTarefaPorId(Scanner scanner) {
        System.out.print("Digite o ID da tarefa para buscar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Tarefa tarefa = this.buscarTarefaPorId(id);
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

    private void buscarTarefasPorTexto(Scanner scanner) {
        System.out.print("Digite o texto para buscar nas descrições: ");
        String textoBusca = scanner.nextLine();

        List<Tarefa> tarefasEncontradas = this.buscarTarefaPorTexto(textoBusca);
        if (tarefasEncontradas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com o texto '" + textoBusca + "'.");
        } else {
            System.out.println("\n--- Tarefas Encontradas por Texto: '" + textoBusca + "' ---");
            for (Tarefa tarefa : tarefasEncontradas) {
                System.out.println(tarefa);
            }
        }
    }

    private void concluirOuCancelarTarefa(Scanner scanner) {
        listarTarefas(scanner, "todas");
        System.out.print("Digite o ID da tarefa para concluir/cancelar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Tarefa tarefa = this.buscarTarefaPorId(id);
            if (tarefa == null) {
                System.out.println("Tarefa com ID " + id + " não encontrada.");
                return;
            }
            System.out.println("Tarefa selecioda: " + tarefa.getDescricao() + " (Status atual: " + tarefa.getStatus() + ")");
            System.out.print("Deseja (C)oncluir ou (X)Cancelar esta tarefa? (C/X): ");
            String acao = scanner.nextLine().trim().toUpperCase();

            if ("C".equals(acao)) {
                if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
                    System.out.println("Tarefa já está CONCLUÍDA.");
                } else {
                    if (tarefa.getStatus() == StatusTarefa.CANCELADA) {
                        System.out.print("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") está com status CANCELADA. Deseja alterar o status para CONCLUIDA? (S/N): ");
                        String confirma = scanner.nextLine().trim().toUpperCase();
                        if ("S".equals(confirma)) {
                            if (tarefa instanceof TarefaRecorrente tr) {
                                tr.concluirComRegistro(this.tarefas);
                                System.out.println("Tarefa recorrente marcada como concluída e nova ocorrência adicionada.");
                            } else {
                                tarefa.concluir();
                                System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") marcada como CONCLUÍDA.");
                            }

                        } else if ("N".equals(confirma)) {
                            System.out.println("Status da tarafa '" + tarefa.getDescricao() + "' (ID: " + id + ") não foi alterado.");
                        } else {
                            System.out.println("Opção inválida. Nenhuma ação realizada.");
                        }
                    } else {
                        if (tarefa instanceof TarefaRecorrente tr) {
                            tr.concluirComRegistro(this.tarefas);
                            System.out.println("Tarefa recorrente marcada como concluída e nova ocorrência adicionada.");
                        } else {
                            tarefa.concluir();
                            System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") marcada como CONCLUÍDA.");
                        }


                    }
                }
            } else if ("X".equals(acao)) {
                if (tarefa.getStatus() == StatusTarefa.CANCELADA) {
                    System.out.println("Tarefa já está CANCELADA.");
                } else {
                    if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
                        System.out.print("Tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") está com status CONCLUIDA. Deseja alterar o status para CANCELADA? (S/N): ");
                        String confirma = scanner.nextLine().trim().toUpperCase();
                        if ("S".equals(confirma)) {
                            cancelarTarefa(id); // ✅ CHAMA O MÉTODO COMPLETO!
                        } else if ("N".equals(confirma)) {
                            System.out.println("Status da tarefa '" + tarefa.getDescricao() + "' (ID: " + id + ") não foi alterado.");
                        } else {
                            System.out.println("Opção inválida. Nenhuma ação realizada.");
                        }
                    } else {
                        cancelarTarefa(id); // ✅ CHAMA O MÉTODO COMPLETO!
                    }
                }
            } else {
                System.out.println("Opção inválida. Nenhuma ação realizada.");
            }

        } catch (
                InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private void removerTarefa(Scanner scanner) {
        listarTarefas(scanner, "todas");
        System.out.print("Digite o ID da tarefa para remover: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            this.removerTarefa(id);
        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private void atualizarTarefa(Scanner scanner) {
        listarTarefas(scanner, "todas");
        System.out.print("Digite o ID da tarefa que deseja atualizar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Tarefa tarefaExistente = this.buscarTarefaPorId(id);
            if (tarefaExistente == null) {
                System.out.println("Tarefa com ID " + id + " não encontrada para atualização.");
                return;
            }


            System.out.print("Digite a NOVA descrição da tarefa (atual: " + tarefaExistente.getDescricao() + ") [Pressione Enter para manter]: ");
            String novaDescricaoInput = scanner.nextLine();
            String novaDescricao = novaDescricaoInput.isBlank() ? tarefaExistente.getDescricao() : novaDescricaoInput;

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


            if (tarefaExistente instanceof TarefaRecorrente trExistente) {


                trExistente.setDescricao(novaDescricao);
                trExistente.setPrioridade(novaPrioridade);


                System.out.print("Digite a NOVA frequência (DIARIA, SEMANAL, QUINZENAL, MENSAL, ANUAL) (atual: " + trExistente.getFrequencia() + ") [Pressione Enter para manter]: ");
                String novaFrequenciaStr = scanner.nextLine().toUpperCase();
                if (!novaFrequenciaStr.isBlank()) {
                    try {
                        trExistente.setFrequencia(FrequenciaRecorrencia.valueOf(novaFrequenciaStr));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Frequência inválida. Frequência NÃO atualizada.");
                    }
                }

                System.out.print("Digite a NOVA data da PRIMEIRA OCORRÊNCIA (DD-MM-AAAA) (atual: " + trExistente.getPrimeiraOcorrencia().format(DATE_FORMATTER) + ") [Pressione Enter para manter]: ");
                String novaPrimeiraOcorrenciaStr = scanner.nextLine();
                if (!novaPrimeiraOcorrenciaStr.isBlank()) {
                    try {
                        LocalDate novaPrimeiraOcorrencia = LocalDate.parse(novaPrimeiraOcorrenciaStr, DATE_FORMATTER);
                        trExistente.setPrimeiraOcorrencia(novaPrimeiraOcorrencia);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Primeira Ocorrência NÃO atualizada.");
                    }
                }

                System.out.print("Digite a NOVA data de INÍCIO da recorrência (DD-MM-AAAA) (atual: " + (trExistente.getDataInicioRecorrencia() != null ? trExistente.getDataInicioRecorrencia().format(DATE_FORMATTER) : "N/A") + ") [Pressione Enter para manter]: ");
                String novaDataInicioRecorrenciaStr = scanner.nextLine();
                if (!novaDataInicioRecorrenciaStr.isBlank()) {
                    try {
                        LocalDate novaDataInicioRecorrencia = LocalDate.parse(novaDataInicioRecorrenciaStr, DATE_FORMATTER);
                        trExistente.setDataInicioRecorrencia(novaDataInicioRecorrencia);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Data de Início da Recorrência NÃO atualizada.");
                    }
                }

                System.out.print("Digite a NOVA data de FIM da recorrência (DD-MM-AAAA) (atual: " + (trExistente.getDataFimRecorrencia() != null ? trExistente.getDataFimRecorrencia().format(DATE_FORMATTER) : "N/A") + ") [Pressione Enter para manter/Remover data de fim]: ");
                String novaDataFimRecorrenciaStr = scanner.nextLine();
                if (!novaDataFimRecorrenciaStr.isBlank()) {
                    try {
                        LocalDate novaDataFimRecorrencia = LocalDate.parse(novaDataFimRecorrenciaStr, DATE_FORMATTER);

                        if (!novaDataFimRecorrencia.isAfter(LocalDate.now().minusDays(1))) {
                            System.out.println("A data de fim de recorrência não pode ser anterior ao dia de hoje. Data de fim NÃO atualizada.");
                        } else {
                            trExistente.setDataFimRecorrencia(novaDataFimRecorrencia);
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Data de Fim da Recorrência NÃO atualizada.");
                    }
                } else {
                    trExistente.setDataFimRecorrencia(null);
                }


                this.atualizarTarefa(trExistente);

            } else {
                LocalDate novaDataVencimento = tarefaExistente.getDataVencimento();
                System.out.print("Digite a NOVA data de vencimento (DD-MM-AAAA) (atual: " + tarefaExistente.getDataVencimento().format(DATE_FORMATTER) + ") [Pressione Enter para manter]: ");
                String novaDataStrInput = scanner.nextLine();
                if (!novaDataStrInput.isBlank()) {
                    try {
                        novaDataVencimento = LocalDate.parse(novaDataStrInput, DATE_FORMATTER);
                        if (!novaDataVencimento.isAfter(LocalDate.now().minusDays(1))) {
                            System.out.println("A nova data de vencimento não pode ser anterior ao dia de hoje. Data de vencimento NÃO atualizada.");
                            novaDataVencimento = tarefaExistente.getDataVencimento();
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de data inválido. Use DD-MM-AAAA. Data de vencimento NÃO atualizada.");
                        novaDataVencimento = tarefaExistente.getDataVencimento();
                    }
                }

                StatusTarefa novoStatus = tarefaExistente.getStatus();
                System.out.print("Digite o NOVO status (PENDENTE, CONCLUIDA, CANCELADA) (atual: " + tarefaExistente.getStatus() + ") [Pressione Enter para manter]: ");
                String novoStatusStrInput = scanner.nextLine().toUpperCase();
                if (!novoStatusStrInput.isBlank()) {
                    try {
                        novoStatus = StatusTarefa.valueOf(novoStatusStrInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Status inválido. Use PENDENTE, CONCLUIDA ou CANCELADA. Status NÃO atualizado.");
                    }
                }


                Tarefa tarefaSimplesAtualizada = new Tarefa(
                        tarefaExistente.getId(),
                        novaDescricao,
                        novaDataVencimento,
                        novaPrioridade,
                        novoStatus,
                        tarefaExistente.getDataCriacao()
                );
                this.atualizarTarefa(tarefaSimplesAtualizada);
            }

        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Por favor, digite um número inteiro.");
            scanner.nextLine();
        }
    }

    private void visualDaSemana() {
        List<Tarefa> tarefasDaSemana = this.listarTarefasDaSemana();
        if (tarefasDaSemana.isEmpty()) {
            System.out.println("Nenhuma tarefa para esta semana.");
        } else {
            for (Tarefa tarefa : tarefasDaSemana) {
                System.out.println(tarefa);
            }
        }
    }

    private void sugerirTarefa() {
        Optional<Tarefa> tarefaSugerida = this.sugerirTarefaPrioritaria();

        if (tarefaSugerida.isPresent()) {
            System.out.println("\nSua próxima tarefa prioritária sugerida é:");
            System.out.println(tarefaSugerida.get());
        } else {
            System.out.println("Não há tarefas pendentes para sugerir no momento.");
        }
    }

}

