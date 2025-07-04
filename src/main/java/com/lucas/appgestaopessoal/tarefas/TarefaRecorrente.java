package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TarefaRecorrente extends Tarefa {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private FrequenciaRecorrencia frequencia;
    private LocalDate dataInicioRecorrencia;
    private LocalDate dataFimRecorrencia;
    private LocalDate proximaOcorrencia;
    private LocalDate dataPrimeiraOcorrencia;

    // Construtor para TarefaRecorrente
    public TarefaRecorrente(int id, String descricao, LocalDate dataVencimentoInicialDigitada, Prioridade prioridade,
                            FrequenciaRecorrencia frequencia, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia) {
        super(id, descricao, dataVencimentoInicialDigitada, prioridade, StatusTarefa.PENDENTE, LocalDate.now()); // Tarefas recorrentes começam como não concluídas

        this.frequencia = frequencia;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.dataPrimeiraOcorrencia = dataVencimentoInicialDigitada;
        this.proximaOcorrencia = dataVencimentoInicialDigitada; // Inicia com a data de vencimento fornecida

        // **IMPORTANTE:** Garante que a primeira ocorrência esteja no futuro (ou hoje)
        // se a data de vencimento inicial for no passado.
        ajustarProximaOcorrenciaParaFuturo();
    }

    // GETTERS
    public LocalDate getDataPrimeiraOcorrencia() {
        return dataPrimeiraOcorrencia;
    }

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

    @Override
    public void concluir() {
        super.concluir(); // Marca a ocorrência atual como concluída (Tarefa.concluido = true)

        // Se a próxima ocorrência já é nula (recorrência finalizada), não faz nada
        if (this.proximaOcorrencia == null) {
            super.setStatus(StatusTarefa.CONCLUIDA);
            return;
        }

        // 1. Calcula a próxima ocorrência a partir da atual
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

        // 2. Define a data limite inferior: deve ser hoje OU a data de início da recorrência (se esta for futura)
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimiteInferior = hoje;
        if (this.dataInicioRecorrencia != null && this.dataInicioRecorrencia.isAfter(hoje)) {
            dataLimiteInferior = this.dataInicioRecorrencia;
        }

        // 3. Avança a próxima ocorrência calculada até que esteja no futuro (ou hoje)
        // E também que esteja na ou após a data de início da recorrência.
        while (proximaOcorrenciaCalculada.isBefore(dataLimiteInferior)) {
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
                super.setStatus(StatusTarefa.CONCLUIDA);
                return;
            }
        }

        // 4. Verifica se a nova ocorrência ultrapassa a data de fim da recorrência (após todos os avanços)
        if (dataFimRecorrencia != null && proximaOcorrenciaCalculada.isAfter(dataFimRecorrencia)) {
            this.proximaOcorrencia = null; // Sinaliza que não há mais ocorrências
            this.setDataVencimento(null); // Remove a data de vencimento da tarefa pai
            super.setStatus(StatusTarefa.CONCLUIDA);
            return;
        }

        // 5. Atualiza a próxima ocorrência e o status da tarefa para a nova iteração
        this.proximaOcorrencia = proximaOcorrenciaCalculada;
        this.setDataVencimento(this.proximaOcorrencia); // Atualiza a data de vencimento da tarefa pai
        this.setStatus(StatusTarefa.PENDENTE); // Marca como não concluída para a nova ocorrência
    }

    private void ajustarProximaOcorrenciaParaFuturo() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimiteInferior = hoje;

        if (this.dataInicioRecorrencia != null && this.dataInicioRecorrencia.isAfter(hoje)) {
            dataLimiteInferior = this.dataInicioRecorrencia;
        }

        while (this.proximaOcorrencia.isBefore(dataLimiteInferior) &&
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", Tipo: Recorrente");
        sb.append(", Frequência: ").append(this.frequencia);
        if (this.dataPrimeiraOcorrencia != null) {
            sb.append(", Primeira Ocorrência: ").append(this.dataPrimeiraOcorrencia.format(DATE_FORMATTER));
        }

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