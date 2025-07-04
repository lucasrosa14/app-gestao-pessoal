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
    public TarefaRecorrente(int id, String descricao, LocalDate primeiraOcorrenciaBase, Prioridade prioridade, FrequenciaRecorrencia frequencia, LocalDate dataInicioRecorrencia, LocalDate dataFimRecorrencia) {
        super(id, descricao, primeiraOcorrenciaBase, prioridade, StatusTarefa.PENDENTE, LocalDate.now()); // Chamada ao construtor de Tarefa.
        this.frequencia = frequencia;
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        this.dataFimRecorrencia = dataFimRecorrencia;
        this.dataPrimeiraOcorrencia = primeiraOcorrenciaBase; // Garanta que este campo é definido
        calcularProximaOcorrencia(); // Deve recalcular a próxima ocorrência
    }

    // GETTERS
    public LocalDate getPrimeiraOcorrencia() {
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
    @Override // Use @Override se você sobrescrever setStatus, setDescricao, setPrioridade
    public void setDescricao(String descricao) {
        super.setDescricao(descricao); // Chama o setter da classe pai
    }

    @Override
    public void setPrioridade(Prioridade prioridade) {
        super.setPrioridade(prioridade); // Chama o setter da classe pai
    }

    @Override
    public void setStatus(StatusTarefa status) {
        super.setStatus(status); // Chama o setter da classe pai
    }

    public void setFrequencia(FrequenciaRecorrencia frequencia) {
        this.frequencia = frequencia;
        calcularProximaOcorrencia();
    }

    public void setDataInicioRecorrencia(LocalDate dataInicioRecorrencia) {
        this.dataInicioRecorrencia = dataInicioRecorrencia;
        calcularProximaOcorrencia();
    }

    public void setDataFimRecorrencia(LocalDate dataFimRecorrencia) {
        this.dataFimRecorrencia = dataFimRecorrencia;
        calcularProximaOcorrencia();
    }

    public void setPrimeiraOcorrencia(LocalDate primeiraOcorrencia) {
        this.dataPrimeiraOcorrencia = primeiraOcorrencia;
        calcularProximaOcorrencia();
    }

    private void calcularProximaOcorrencia() {
        LocalDate hoje = LocalDate.now();


        LocalDate dataParaCalcular = dataInicioRecorrencia != null ? dataInicioRecorrencia : dataPrimeiraOcorrencia;


        if (dataParaCalcular.isBefore(hoje)) {
            dataParaCalcular = hoje;
        }

        LocalDate proximaOcorrenciaEncontrada = null;
        LocalDate tempDate = dataPrimeiraOcorrencia;


        while (proximaOcorrenciaEncontrada == null) {

            if (dataFimRecorrencia != null && tempDate.isAfter(dataFimRecorrencia)) {
                break;
            }


            if (!tempDate.isBefore(hoje)) {
                proximaOcorrenciaEncontrada = tempDate;
                break;
            }


            switch (frequencia) {
                case DIARIA:
                    tempDate = tempDate.plusDays(1);
                    break;
                case SEMANAL:
                    tempDate = tempDate.plusWeeks(1);
                    break;
                case QUINZENAL:
                    tempDate = tempDate.plusDays(15);
                    break;
                case MENSAL:
                    tempDate = tempDate.plusMonths(1);
                    break;
                case ANUAL:
                    tempDate = tempDate.plusYears(1);
                    break;
            }
        }


        super.setDataVencimento(proximaOcorrenciaEncontrada);


        if (getStatus() != StatusTarefa.CANCELADA) {
            if (proximaOcorrenciaEncontrada == null) {
                super.setStatus(StatusTarefa.CONCLUIDA);
            } else {
                super.setStatus(StatusTarefa.PENDENTE);
            }
        }
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

    @Override
    public String toString() {
        // Usa o StringBuilder para construir a string de forma eficiente
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId());
        sb.append(", Descrição: ").append(getDescricao());
        sb.append(", Prioridade: ").append(getPrioridade());
        sb.append(", Status: ").append(getStatus());
        sb.append(", Cadastrada em: ").append(getDataCriacao().format(DATE_FORMATTER));
        sb.append(", Frequência: ").append(frequencia);
        sb.append(", Início Recorrência: ").append(dataInicioRecorrencia != null ? dataInicioRecorrencia.format(DATE_FORMATTER) : "N/A");
        sb.append(", Primeira Ocorrência: ").append(dataPrimeiraOcorrencia.format(DATE_FORMATTER));
        sb.append(", Fim Recorrência: ").append(dataFimRecorrencia != null ? dataFimRecorrencia.format(DATE_FORMATTER) : "N/A");

        // Exibe a próxima ocorrência (que é o dataVencimento herdado)
        if (getDataVencimento() != null) { // getDataVencimento() aqui é a próxima ocorrência calculada
            sb.append(", Próxima Ocorrência: ").append(getDataVencimento().format(DATE_FORMATTER));
        } else {
            // Se getDataVencimento for null, significa que não há mais ocorrências
            sb.append(", Próxima Ocorrência: FINALIZADA");
        }

        return sb.toString();
    }
}