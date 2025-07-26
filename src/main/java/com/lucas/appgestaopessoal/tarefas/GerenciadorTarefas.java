package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GerenciadorTarefas {

    private final List<Tarefa> tarefas = new ArrayList<>();

    public Tarefa adicionarTarefa(Tarefa tarefa) {
        tarefa.setId();
        tarefas.add(tarefa);
        return tarefa;
    }

    public List<Tarefa> listarTodasTarefas() {
        return new ArrayList<>(tarefas);
    }

    public Optional<Tarefa> buscarTarefaPorId(int id) {
        return tarefas.stream().filter(t -> t.getId() == id).findFirst();
    }

    public void removerTarefa(int id) {
        tarefas.removeIf(t -> t.getId() == id);
    }

    public Tarefa atualizarTarefa(Tarefa tarefaAtualizada) {
        Optional<Tarefa> opt = buscarTarefaPorId(tarefaAtualizada.getId());
        if (opt.isPresent()) {
            int idx = tarefas.indexOf(opt.get());
            tarefas.set(idx, tarefaAtualizada);
            return tarefaAtualizada;
        }
        return null;
    }

    // Adicione outros métodos conforme necessário, sem interação via Scanner/System.out
}