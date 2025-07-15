package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;

import java.time.LocalDate;

import static com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER;

public class TarefaRecorrente extends Tarefa {

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

    private LocalDate avancarDataPorFrequencia(LocalDate dataBase, FrequenciaRecorrencia freq) {
        if (dataBase == null) {
            // Decida como lidar com dataBase nula aqui, talvez lançar uma exceção
            throw new IllegalArgumentException("Data base para avanço de frequência não pode ser nula.");
        }
        return switch (freq) {
            case DIARIA -> dataBase.plusDays(1);
            case SEMANAL -> dataBase.plusWeeks(1);
            case QUINZENAL -> dataBase.plusDays(15); // Consistente: Duas semanas
            case MENSAL -> dataBase.plusMonths(1);
            case ANUAL -> dataBase.plusYears(1);
        };
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

    @Override
    public void cancelar() {
        // Se a tarefa já está CONCLUIDA (finalizada a recorrência), não faz nada.
        if (getStatus() == StatusTarefa.CONCLUIDA && this.proximaOcorrencia == null) {
            return;
        }

        // Se já está CANCELADA (ocorrencia atual) ou PENDENTE, avança para a próxima ocorrência.
        // A lógica é similar a `concluir()`, mas define o status como CANCELADA para a ocorrência atual
        // e PENDENTE para a próxima (se válida).

        LocalDate hoje = LocalDate.now();

        // Se não há proximaOcorrencia (já terminou), marca como CONCLUIDA e sai
        if (this.proximaOcorrencia == null) {
            super.cancelar(); // Mantém o status CANCELADA ou TRANSICIONA para CONCLUIDA se não há mais
            return;
        }

        // Calcula a próxima ocorrência avançando a partir da atual
        LocalDate novaProximaOcorrencia = avancarDataPorFrequencia(this.proximaOcorrencia, frequencia);

        // Avança até que a novaProximaOcorrencia seja HOJE ou no FUTURO
        while (novaProximaOcorrencia.isBefore(hoje)) {
            if (dataFimRecorrencia != null && novaProximaOcorrencia.isAfter(dataFimRecorrencia)) {
                this.proximaOcorrencia = null;
                super.setDataVencimento(null);
                super.setStatus(StatusTarefa.CONCLUIDA); // Finaliza a recorrência
                return;
            }
            novaProximaOcorrencia = avancarDataPorFrequencia(novaProximaOcorrencia, frequencia);
        }

        // Verifica se a nova ocorrência calculada ultrapassa a data de fim
        if (dataFimRecorrencia != null && novaProximaOcorrencia.isAfter(dataFimRecorrencia)) {
            this.proximaOcorrencia = null;
            super.setDataVencimento(null);
            super.setStatus(StatusTarefa.CONCLUIDA); // A recorrência realmente terminou
        } else {
            // Se encontrou uma próxima ocorrência válida
            this.proximaOcorrencia = novaProximaOcorrencia;
            super.setDataVencimento(this.proximaOcorrencia);
            super.setStatus(StatusTarefa.PENDENTE); // A tarefa volta a ser PENDENTE para a nova ocorrência
        }
    }


    // Lógica principal de cálculo da próxima ocorrência
    private void calcularProximaOcorrencia() {
        // Se a tarefa foi explicitamente marcada como CANCELADA (permanente)
        // ou CONCLUIDA (finalizada), não deve recalcular para uma nova data ativa.
        // Esta é a diferença: se `cancelar()` agora AVANÇA, o `setStatus(CANCELADA)`
        // não deve simplesmente zerar tudo, mas avançar a instância atual.
        // Se você quer um "cancelamento permanente", precisaria de outro método.

        LocalDate hoje = LocalDate.now();
        LocalDate tempDate = dataPrimeiraOcorrencia;

        if (dataInicioRecorrencia != null && dataInicioRecorrencia.isAfter(tempDate)) {
            tempDate = dataInicioRecorrencia;
        }

        while (tempDate.isBefore(hoje)) {
            if (dataFimRecorrencia != null && tempDate.isAfter(dataFimRecorrencia)) {
                this.proximaOcorrencia = null;
                super.setDataVencimento(null);
                super.setStatus(StatusTarefa.CONCLUIDA);
                return;
            }
            tempDate = avancarDataPorFrequencia(tempDate, frequencia);
        }

        if (dataFimRecorrencia != null && tempDate.isAfter(dataFimRecorrencia)) {
            this.proximaOcorrencia = null;
            super.setDataVencimento(null);
            super.setStatus(StatusTarefa.CONCLUIDA);
        } else {
            this.proximaOcorrencia = tempDate;
            super.setDataVencimento(tempDate);
            // O status é PENDENTE, a menos que tenha sido explicitamente CANCELADA ou CONCLUIDA por outro motivo
            if (getStatus() != StatusTarefa.CANCELADA && getStatus() != StatusTarefa.CONCLUIDA) {
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
        proximaOcorrenciaCalculada = switch (frequencia) {
            case DIARIA -> proximaOcorrenciaCalculada.plusDays(1);
            case SEMANAL -> proximaOcorrenciaCalculada.plusWeeks(1);
            case QUINZENAL -> proximaOcorrenciaCalculada.plusDays(15);
            case MENSAL -> proximaOcorrenciaCalculada.plusMonths(1);
            case ANUAL -> proximaOcorrenciaCalculada.plusYears(1);
        };

        // 2. Define a data limite inferior: deve ser hoje OU a data de início da recorrência (se esta for futura)
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimiteInferior = hoje;
        if (this.dataInicioRecorrencia != null && this.dataInicioRecorrencia.isAfter(hoje)) {
            dataLimiteInferior = this.dataInicioRecorrencia;
        }

        // 3. Avança a próxima ocorrência calculada até que esteja no futuro (ou hoje)
        // E também que esteja na ou após a data de início da recorrência.
        while (proximaOcorrenciaCalculada.isBefore(dataLimiteInferior)) {
            proximaOcorrenciaCalculada = switch (frequencia) {
                case DIARIA -> proximaOcorrenciaCalculada.plusDays(1);
                case SEMANAL -> proximaOcorrenciaCalculada.plusWeeks(1);
                case QUINZENAL -> proximaOcorrenciaCalculada.plusDays(15);
                case MENSAL -> proximaOcorrenciaCalculada.plusMonths(1);
                case ANUAL -> proximaOcorrenciaCalculada.plusYears(1);
            };
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
        sb.append(", Cadastrada em: ").append(getDataCriacao().format(DATE_FORMATTER));


        return sb.toString();
    }
}