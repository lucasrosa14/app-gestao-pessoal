package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    protected int id;
    protected String descricao;
    protected LocalDate dataVencimento;
    protected Prioridade prioridade;
    protected StatusTarefa status;
    protected LocalDate dataCriacao;

    public Tarefa(int id, String descricao, LocalDate dataVencimento, Prioridade prioridade, StatusTarefa status, LocalDate dataCriacao) {
        this.id = id;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
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

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public boolean isConcluido() {
        return this.status == StatusTarefa.CONCLUIDA;
    }

    public void concluir(){
        this.status = StatusTarefa.CONCLUIDA;
    }

    public void setPrioridade(Prioridade novaPrioridade){
        this.prioridade = novaPrioridade;

    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString(){

        String vencimentoFormatado = (dataVencimento != null) ? dataVencimento.format(DATE_FORMATTER) : "N/A";
        String criacaoFormatada = (dataCriacao != null) ? dataCriacao.format(DATE_FORMATTER) : "N/A";

        return  "ID: " +
                this.id +
                ", Descrição: " + this.descricao +
                ", Vencimento: " + vencimentoFormatado +
                ", Prioridade: " + this.prioridade +
                ", Status: " + this.status +
                ", Cadastrada em: " + criacaoFormatada;

    }
}
