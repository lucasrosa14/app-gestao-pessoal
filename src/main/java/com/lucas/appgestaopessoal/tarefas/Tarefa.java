package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.Prioridade;

import java.time.LocalDate;

public class Tarefa {

    private int id;
    private String descricao;
    private LocalDate dataVencimento;
    private Prioridade prioridade;
    private boolean concluido;

    public Tarefa(int id, String descricao, LocalDate dataVencimento, Prioridade prioridade, boolean concluido) {
        this.id = id;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.prioridade = prioridade;
        this.concluido = concluido;
    }

    public Tarefa(int id, String descricao, LocalDate dataVencimento, Prioridade prioridade) {
        this(id, descricao, dataVencimento, prioridade, false); // Task starts as not concluded
    }


    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public void concluir(){
        this.concluido = true;
    }

    public void setPrioridade(Prioridade novaPrioridade){
        this.prioridade = novaPrioridade;

    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    @Override
    public String toString(){
        String tarefaFormatada = (  "ID: " + this.getId() +
                                    ", Descrição: " + this.getDescricao() +
                                    ", Vencimento: " + this.getDataVencimento() +
                                    ", Prioridade: " + this.getPrioridade() +
                                    ", Concluída: " + this.isConcluido());

        return tarefaFormatada;
    }
}
