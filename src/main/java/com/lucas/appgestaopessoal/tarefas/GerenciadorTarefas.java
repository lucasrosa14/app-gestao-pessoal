package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;
import static com.lucas.appgestaopessoal.util.NormalizeTexto.normalizeText;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;




public class GerenciadorTarefas {

    private List<Tarefa> tarefas;

    public GerenciadorTarefas() {
        this.tarefas = new ArrayList<>();
    }

    public Tarefa adicionarTarefa(String descricao, LocalDate dataVencimento, Prioridade prioridade) {
        int id = IdGenerator.generateNewId();
        Tarefa tarefa = new Tarefa(id, descricao, dataVencimento, prioridade, StatusTarefa.PENDENTE, LocalDate.now());
        this.tarefas.add(tarefa);
        System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + tarefa.getId() + "') adicionada com sucesso!");
        return tarefa;
    }

    public Tarefa adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
        System.out.println("Tarefa '" + tarefa.getDescricao() + "' (ID: " + tarefa.getId() + ") adicionada com sucesso!");
        return tarefa;
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
                .sorted(Comparator
                        .comparing(Tarefa::getDataVencimento, Comparator.reverseOrder())
                )
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
                .filter(tarefa -> normalizeText(tarefa.getDescricao()).toLowerCase().contains(textoBuscaNormalizado))
                .collect(Collectors.toList());
    }

    public boolean removerTarefa(int id) {
        Tarefa tarefaParaRemover = buscarTarefaPorId(id);
        if (tarefaParaRemover != null) {
            this.tarefas.remove(tarefaParaRemover);
            System.out.println("Tarefa com ID " + id + " removida com sucesso!");
            return true;
        }
        System.out.println("Tarefa com ID " + id + " não encontrada.");
        return false;
    }

    public boolean concluirTarefa(int id) {
        Tarefa tarefa = buscarTarefaPorId(id);
        if (tarefa != null) {
            tarefa.concluir();
            System.out.println("Tarefa '" + tarefa.getDescricao() + "' concluída!");
            return true;
        }
        System.out.println("Tarefa com ID " + id + " não encontrada.");
        return false;
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
            if (!tarefa.isConcluido()) {
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
}

