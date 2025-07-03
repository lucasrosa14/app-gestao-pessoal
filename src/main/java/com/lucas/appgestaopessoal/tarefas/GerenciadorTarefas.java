package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


public class GerenciadorTarefas {

    private List<Tarefa> tarefas;

    public GerenciadorTarefas() {
        this.tarefas = new ArrayList<>();
    }

    public void adicionarTarefa(String descricao, LocalDate dataVencimento, Prioridade prioridade) {
        int novoId = IdGenerator.generateNewId();
        Tarefa tarefa = new Tarefa(novoId, descricao, dataVencimento, prioridade);
        this.tarefas.add(tarefa);
        System.out.println("Tarefa '" + tarefa.getDescricao() + "' adicionada com sucesso!");
    }

    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(this.tarefas);
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
        List<Tarefa> tarefasEncontradas = new ArrayList<>();
        String textoBuscaLowerCase = textoBusca.toLowerCase();

        for (Tarefa tarefa : this.tarefas) {
            if (tarefa.getDescricao().toLowerCase().contains(textoBuscaLowerCase)) {
                tarefasEncontradas.add(tarefa);
            }
        }
        return tarefasEncontradas;
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

    public boolean atualizarTarefa(Tarefa tarefaAtualizada) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getId() == tarefaAtualizada.getId()) {
                tarefas.set(i, tarefaAtualizada);
                System.out.println("Tarefa com ID " + tarefaAtualizada.getId() + " atualizada.");
                return true;
            }
        }
        System.out.println("Tarefa com ID " + tarefaAtualizada.getId() + " não encontrada.");
        return false;
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

