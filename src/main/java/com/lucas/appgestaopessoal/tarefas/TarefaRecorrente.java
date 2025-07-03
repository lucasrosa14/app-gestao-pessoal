package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.Prioridade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TarefaRecorrente extends Tarefa {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private FrequenciaRecorrencia frequencia;
    private LocalDate dataInicioRecorrencia;
    private LocalDate dataFimRecorrencia;
    private LocalDate proximaOcorrencia; // A data da próxima vez que ela deve ser feita

    // Construtor para TarefaRecorrente
    public TarefaRecorrente(int id, String descricao, LocalDate dataVencimento, Prioridade prioridade,
                            FrequenciaRecorrencia frequencia, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia) {
        super(id, descricao, dataVencimento, prioridade, false); // Tarefas recorrentes começam como não concluídas

        this.frequencia = frequencia;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.proximaOcorrencia = dataVencimento; // Inicia com a data de vencimento fornecida

        // **IMPORTANTE:** Garante que a primeira ocorrência esteja no futuro (ou hoje)
        // se a data de vencimento inicial for no passado.
        ajustarProximaOcorrenciaParaFuturo();
    }

    // GETTERS
    public FrequenciaRecorrencia getFrequencia() {
        return frequencia;
    }

    public LocalDate getDataInicioRecorrencia() {
        return dataInicioRecorrencia;
    }

    public LocalDate getDataFimRecorrencia() {
        return dataFimRecorrencia;
    }

    public LocalDate getProximaOcorrencia() {
        return proximaOcorrencia;
    }

    // SETTERS (se forem realmente necessários para modificar a recorrência em tempo de execução)
    public void setFrequencia(FrequenciaRecorrencia frequencia) {
        this.frequencia = frequencia;
    }

    public void setDataInicioRecorrencia(LocalDate dataInicioRecorrencia) {
        this.dataInicioRecorrencia = dataInicioRecorrencia;
    }

    public void setDataFimRecorrencia(LocalDate dataFimRecorrencia) {
        this.dataFimRecorrencia = dataFimRecorrencia;
    }

    public void setProximaOcorrencia(LocalDate proximaOcorrencia) {
        this.proximaOcorrencia = proximaOcorrencia;
    }


    /**
     * Sobrescreve o método 'concluir' da Tarefa pai.
     * Ao concluir uma TarefaRecorrente, ela calcula sua próxima ocorrência
     * e, se a data de término não foi atingida, a tarefa é marcada novamente como não concluída
     * para a próxima iteração.
     */
    @Override
    public void concluir() {
        super.concluir(); // Marca a ocorrência atual como concluída (Tarefa.concluido = true)

        // Se a próxima ocorrência já é nula (recorrência finalizada), não faz nada
        if (this.proximaOcorrencia == null) {
            return;
        }

        // 1. Calcula a próxima data com base na frequência a partir da ocorrência atual
        LocalDate proximaOcorrenciaCalculada = this.proximaOcorrencia;
        switch (frequencia) {
            case DIARIA:
                proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusDays(1);
                break;
            case SEMANAL:
                proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusWeeks(1);
                break;
            case QUINZENAL:
                proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusWeeks(2);
                break;
            case MENSAL:
                proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusMonths(1);
                break;
            case ANUAL:
                proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusYears(1);
                break;
        }

        // 2. Verifica se a nova ocorrência ultrapassa a data de fim da recorrência
        if (dataFimRecorrencia != null && proximaOcorrenciaCalculada.isAfter(dataFimRecorrencia)) {
            // Se ultrapassou, a recorrência terminou.
            this.proximaOcorrencia = null; // Sinaliza que não há mais ocorrências
            this.setDataVencimento(null); // Remove a data de vencimento da tarefa pai
            // A tarefa permanece como concluída (do super.concluir()) para a última ocorrência.
            return;
        }

        // 3. Garante que a próxima ocorrência esteja no futuro (ou hoje)
        // Isso é para casos onde a tarefa foi concluída com atraso e várias ocorrências já teriam passado.
        LocalDate hoje = LocalDate.now();
        while (proximaOcorrenciaCalculada.isBefore(hoje)) {
            switch (frequencia) {
                case DIARIA:
                    proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusDays(1);
                    break;
                case SEMANAL:
                    proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusWeeks(1);
                    break;
                case QUINZENAL:
                    proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusWeeks(2);
                    break;
                case MENSAL:
                    proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusMonths(1);
                    break;
                case ANUAL:
                    proximaOcorrenciaCalculada = proximaOcorrenciaCalculada.plusYears(1);
                    break;
            }
            // Verifica novamente a data de fim dentro do loop, caso pule muitas ocorrências
            if (dataFimRecorrencia != null && proximaOcorrenciaCalculada.isAfter(dataFimRecorrencia)) {
                this.proximaOcorrencia = null;
                this.setDataVencimento(null);
                return;
            }
        }

        // 4. Atualiza a próxima ocorrência e o status da tarefa para a nova iteração
        this.proximaOcorrencia = proximaOcorrenciaCalculada;
        this.setDataVencimento(this.proximaOcorrencia); // Atualiza a data de vencimento da tarefa pai
        this.setConcluido(false); // Marca como não concluída para a nova ocorrência
    }

    /**
     * Método auxiliar para garantir que a proximaOcorrencia esteja no futuro (ou hoje)
     * quando a tarefa é criada ou carregada.
     */
    private void ajustarProximaOcorrenciaParaFuturo() {
        LocalDate hoje = LocalDate.now();
        // Enquanto a próxima ocorrência for anterior a hoje E não tiver passado da data de fim
        while (this.proximaOcorrencia.isBefore(hoje) &&
                (dataFimRecorrencia == null || !this.proximaOcorrencia.isAfter(dataFimRecorrencia))) {
            // Avança a data conforme a frequência
            switch (frequencia) {
                case DIARIA:
                    this.proximaOcorrencia = this.proximaOcorrencia.plusDays(1);
                    break;
                case SEMANAL:
                    this.proximaOcorrencia = this.proximaOcorrencia.plusWeeks(1);
                    break;
                case QUINZENAL:
                    this.proximaOcorrencia = this.proximaOcorrencia.plusWeeks(2);
                    break;
                case MENSAL:
                    this.proximaOcorrencia = this.proximaOcorrencia.plusMonths(1);
                    break;
                case ANUAL:
                    this.proximaOcorrencia = this.proximaOcorrencia.plusYears(1);
                    break;
            }
        }
        // Após o loop, se a data ainda for após a dataFimRecorrencia, significa que a recorrência já terminou
        if (dataFimRecorrencia != null && this.proximaOcorrencia.isAfter(dataFimRecorrencia)) {
            this.proximaOcorrencia = null;
            this.setDataVencimento(null);
        } else {
            // Atualiza a data de vencimento da tarefa pai para a próxima ocorrência
            this.setDataVencimento(this.proximaOcorrencia);
        }
    }


    /**
     * Sobrescreve o toString() para incluir informações de recorrência,
     * e construir a string de forma mais controlada para evitar vírgulas extras.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(super.getId());
        sb.append(", Descrição: ").append(super.getDescricao());
        sb.append(", Prioridade: ").append(super.getPrioridade());
        sb.append(", Concluída: ").append(super.isConcluido());
        sb.append(", Tipo: Recorrente");
        sb.append(", Frequência: ").append(this.frequencia);
        sb.append(", Início Recorrência: ").append(this.dataInicioRecorrencia.format(DATE_FORMATTER));
        if (this.dataFimRecorrencia != null) {
            sb.append(", Fim Recorrência: ").append(this.dataFimRecorrencia.format(DATE_FORMATTER));
        }
        sb.append(", Próxima Ocorrência: ");
        if (this.proximaOcorrencia != null) {
            sb.append(this.proximaOcorrencia.format(DATE_FORMATTER));
        } else {
            sb.append("FINALIZADA"); // Mensagem clara quando a recorrência termina
        }
        return sb.toString();
    }
}