package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;

import java.time.LocalDate;

import static com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER;

public class Tarefa {

    private final int id;
    private String descricao;
    private LocalDate dataVencimento;
    private Prioridade prioridade;
    private StatusTarefa status;
    private LocalDate dataCriacao;

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

    public boolean isCancelada() {
        return this.status == StatusTarefa.CANCELADA;
    }

    public void setPrioridade(Prioridade novaPrioridade) {
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

    public void concluir() {
        this.status = StatusTarefa.CONCLUIDA;
    }

    public void cancelar() {
        this.status = StatusTarefa.CANCELADA;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId());
        sb.append(", Descrição: ").append(getDescricao());

        String vencimentoFormatado = (dataVencimento != null) ? dataVencimento.format(DATE_FORMATTER) : "N/A";
        sb.append(", Vencimento: ").append(vencimentoFormatado);

        sb.append(", Prioridade: ").append(getPrioridade());
        sb.append(", Status: ").append(getStatus());

        String criacaoFormatada = (dataCriacao != null) ? dataCriacao.format(DATE_FORMATTER) : "N/A";
        sb.append(", Cadastrada em: ").append(criacaoFormatada);

        return sb.toString();

    }
}
