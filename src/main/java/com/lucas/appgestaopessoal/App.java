package com.lucas.appgestaopessoal;

import com.lucas.appgestaopessoal.tarefas.Tarefa;
import com.lucas.appgestaopessoal.tarefas.GerenciadorTarefas;
import com.lucas.appgestaopessoal.util.Prioridade;
import org.w3c.dom.ls.LSOutput;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("Iniciando os testes do Módulo de Tarefas");

        GerenciadorTarefas gerenciador = new GerenciadorTarefas();
        System.out.println("\n--- Gerenciador de tarefas instanciado ---");

        System.out.println("\n--- Adicionando Tarefas ---");
        gerenciador.adicionarTarefa("Comprar pão e leite", LocalDate.of(2025, 7, 3), Prioridade.ALTA);
        gerenciador.adicionarTarefa("Pagar contas da casa", LocalDate.of(2025, 7, 5), Prioridade.URGENTE);
        gerenciador.adicionarTarefa("Estudar Java por 1 hora", LocalDate.of(2025, 7, 2), Prioridade.MEDIA);
        gerenciador.adicionarTarefa("Responder e-mails pendentes", LocalDate.of(2025, 7, 4), Prioridade.BAIXA);

        System.out.println("\n--- Listando todas as Tarefas ---");
        List<Tarefa> todasAsTarefas = gerenciador.listarTarefas();
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
        Tarefa tarefaEncontrada = gerenciador.buscarTarefaPorId(2);
        if (tarefaEncontrada != null) {
            System.out.println("Tarefa encontrada: " + tarefaEncontrada.getDescricao());
        } else {
            System.out.println("Tarefa com ID 2 não encontrada.");
        }

        System.out.println("\n--- Buscando Tarefa por ID (ID: 99 - inexistente) ---");
        Tarefa tarefaNaoEncontrada = gerenciador.buscarTarefaPorId(99);
        if (tarefaNaoEncontrada != null) {
            System.out.println("Tarefa encontrada: " + tarefaNaoEncontrada.getDescricao());
        } else {
            System.out.println("Tarefa com ID 99 não encontrada.");
        }

        System.out.println("\n--- Marcando Tarefa com ID 1 como Concluída ---");
        gerenciador.concluirTarefa(1);

        System.out.println("\n--- Buscando Tarefas por Texto: 'Java' ---");
        List<Tarefa> tarefasJava = gerenciador.buscarTarefaPorTexto("Java");
        if (tarefasJava.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'Java'.");
        } else {
            for (Tarefa t : tarefasJava) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Buscando Tarefas por Texto: 'contas' ---");
        List<Tarefa> tarefasContas = gerenciador.buscarTarefaPorTexto("contas");
        if (tarefasContas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'contas'.");
        } else {
            for (Tarefa t : tarefasContas) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Buscando Tarefas por Texto: 'projeto' (case-insensitive) ---");
        List<Tarefa> tarefasProjeto = gerenciador.buscarTarefaPorTexto("PROJETO");
        if (tarefasProjeto.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada com 'PROJETO'.");
        } else {
            for (Tarefa t : tarefasProjeto) {
                System.out.println("  Encontrada: ID: " + t.getId() + ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- Listando Tarefas Após Marcar Concluída ---");
        for (Tarefa t : gerenciador.listarTarefas()) {
            System.out.println("ID: " + t.getId() +
                    ", Descrição: " + t.getDescricao() +
                    ", Concluída: " + t.isConcluido());
        }

        System.out.println("\n--- Atualizando Tarefa com ID 3 ---");
        Tarefa tarefaAtualizada = new Tarefa(3, "Estudar Java avançado por 2 horas", LocalDate.of(2025, 7, 7), Prioridade.ALTA);
        gerenciador.atualizarTarefa(tarefaAtualizada);

        System.out.println("\n--- Listando Tarefas Após Atualização ---");
        for (Tarefa t : gerenciador.listarTarefas()) {
            System.out.println("ID: " + t.getId() +
                    ", Descrição: " + t.getDescricao() +
                    ", Vencimento: " + t.getDataVencimento() +
                    ", Prioridade: " + t.getPrioridade());
        }

        // 7. Remover uma tarefa
        System.out.println("\n--- Removendo Tarefa com ID 4 ---");
        gerenciador.removerTarefa(4);

        System.out.println("\n--- Tentando remover tarefa inexistente (ID 100) ---");
        gerenciador.removerTarefa(100);

        System.out.println("\n--- Listando Tarefas Finais ---");
        todasAsTarefas = gerenciador.listarTarefas();
        if (todasAsTarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa restante.");
        } else {
            for (Tarefa t : todasAsTarefas) {
                System.out.println("ID: " + t.getId() +
                        ", Descrição: " + t.getDescricao());
            }
        }

        System.out.println("\n--- TESTE: Visual da Semana");
        List<Tarefa> minhasTarefasDaSemana = gerenciador.listarTarefasDaSemana();
        if (minhasTarefasDaSemana.isEmpty()){
            System.out.println("Nenhuma tarefa para esta semana!");
        } else {
            for (Tarefa t : minhasTarefasDaSemana){
                System.out.println(t);
            }
        }

        System.out.println("\nTestes do Módulo de Tarefas Concluídos.");
    }



}
