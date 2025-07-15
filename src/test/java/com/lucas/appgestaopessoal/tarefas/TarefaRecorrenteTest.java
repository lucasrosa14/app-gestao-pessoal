package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TarefaRecorrenteTest {

    @BeforeEach
    void setUp() {
        IdGenerator.resetId();
    }


    @Test
    void deveCriarTarefaRecorrenteComAtributosCorretos() {
        // Arrange
        int id = IdGenerator.generateNewId();
        String descricao = "Reunião Semanal";
        LocalDate primeiraOcorrenciaBase = LocalDate.now(); // Primeira ocorrência base é HOJE
        Prioridade prioridade = Prioridade.ALTA;
        FrequenciaRecorrencia frequencia = FrequenciaRecorrencia.SEMANAL;
        LocalDate dataInicioRecorrencia = LocalDate.now(); // Início da recorrência é HOJE
        LocalDate dataFimRecorrencia = LocalDate.now().plusMonths(6); // 6 meses a partir de HOJE

        // Act
        // O construtor internamente chamará calcularProximaOcorrencia()
        TarefaRecorrente tarefa = new TarefaRecorrente(id, descricao, primeiraOcorrenciaBase, prioridade,
                frequencia, dataInicioRecorrencia, dataFimRecorrencia);

        // Assert
        assertEquals(id, tarefa.getId());
        assertEquals(descricao, tarefa.getDescricao());
        assertEquals(prioridade, tarefa.getPrioridade());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus()); // Deve ser PENDENTE inicialmente
        assertEquals(LocalDate.now(), tarefa.getDataCriacao()); // Data de criação deve ser LocalDate.now()

        assertEquals(frequencia, tarefa.getFrequencia());
        assertEquals(dataInicioRecorrencia, tarefa.getDataInicioRecorrencia());
        assertEquals(dataFimRecorrencia, tarefa.getDataFimRecorrencia());
        assertEquals(primeiraOcorrenciaBase, tarefa.getPrimeiraOcorrencia());

        // ProximaOcorrencia: Se HOJE é 12/07 e a tarefa é SEMANAL a partir de 12/07, a próxima ocorrência é 12/07.
        // Se HOJE é 12/07 e a primeira ocorrência foi 10/07 (passado), ele avança.
        // Aqui, como primeiraOcorrenciaBase e dataInicioRecorrencia são HOJE, a proximaOcorrencia também é HOJE.
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());
        assertEquals(LocalDate.now(), tarefa.getDataVencimento()); // getDataVencimento é o mesmo que proximaOcorrencia
    }

    @Test
    void deveCriarTarefaRecorrenteComRecorrenciaInfinita() {
        // Arrange
        int id = IdGenerator.generateNewId();
        String descricao = "Tarefa Diária Infinita";
        LocalDate primeiraOcorrenciaBase = LocalDate.now();
        FrequenciaRecorrencia frequencia = FrequenciaRecorrencia.DIARIA;
        LocalDate dataInicioRecorrencia = LocalDate.now();

        // Act
        TarefaRecorrente tarefa = new TarefaRecorrente(id, descricao, primeiraOcorrenciaBase, Prioridade.BAIXA,
                frequencia, dataInicioRecorrencia, null); // dataFimRecorrencia = null

        // Assert
        assertNull(tarefa.getDataFimRecorrencia()); // Deve ser nulo
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia()); // Próxima ocorrência é hoje
    }

    @Test
    void deveAlterarFrequenciaERecalcularProximaOcorrencia() {
        // Arrange
        // Tarefa diária a partir de HOJE.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Diária -> Mensal", LocalDate.now(), Prioridade.MEDIA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), null);
        // No momento da criação, a próxima ocorrência DIÁRIA é HOJE.
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());

        // Act
        tarefa.setFrequencia(FrequenciaRecorrencia.MENSAL);

        // Assert
        assertEquals(FrequenciaRecorrencia.MENSAL, tarefa.getFrequencia());
        // A próxima ocorrência MENSAL a partir de HOJE será HOJE.
        // (Porque o método calcularProximaOcorrencia() não avança se o tempDate já é hoje ou futuro)
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());
    }

    @Test
    void deveAlterarDataInicioRecorrenciaERecalcularProximaOcorrencia() {
        // Arrange
        // Tarefa diária com primeira ocorrência HOJE
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Ajuste Início", LocalDate.now(), Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), null);
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());

        // Act: Mudar o início para uma data futura (amanhã).
        LocalDate dataInicioFutura = LocalDate.now().plusDays(1);
        tarefa.setDataInicioRecorrencia(dataInicioFutura);

        // Assert
        assertEquals(dataInicioFutura, tarefa.getDataInicioRecorrencia());
        // A proxima ocorrencia deve ser a nova data de início, pois ela é futura.
        assertEquals(dataInicioFutura, tarefa.getProximaOcorrencia());
    }

    @Test
    void deveAlterarDataFimRecorrenciaEInvalidarSeForNoPassado() {
        // Arrange
        // Tarefa diária a partir de HOJE, sem data fim inicialmente.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Ajuste Fim", LocalDate.now(), Prioridade.MEDIA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), null);
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());

        // Act: Mudar o fim para ontem.
        tarefa.setDataFimRecorrencia(LocalDate.now().minusDays(1)); // Data final é ontem

        // Assert
        assertEquals(LocalDate.now().minusDays(1), tarefa.getDataFimRecorrencia());
        // Como a data de fim é anterior à próxima ocorrência (HOJE), a tarefa deve estar concluída/finalizada
        assertNull(tarefa.getProximaOcorrencia(), "Próxima ocorrência deve ser nula se data fim é passada.");
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus(), "Status deve ser CONCLUIDA se não há mais ocorrências.");
        assertNull(tarefa.getDataVencimento(), "Data de vencimento do pai deve ser nula.");
    }

    @Test
    void deveAlterarPrimeiraOcorrenciaERecalcular() {
        // Arrange
        // Tarefa semanal com primeira ocorrência HOJE
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Nova Primeira", LocalDate.now(), Prioridade.BAIXA,
                FrequenciaRecorrencia.SEMANAL, LocalDate.now(), null);
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());

        // Act: Mudar a primeira ocorrência para amanhã.
        LocalDate novaPrimeiraOcorrencia = LocalDate.now().plusDays(1);
        tarefa.setPrimeiraOcorrencia(novaPrimeiraOcorrencia);

        // Assert
        assertEquals(novaPrimeiraOcorrencia, tarefa.getPrimeiraOcorrencia());
        // A próxima ocorrência deve ser a nova primeira ocorrência, pois ela é futura.
        assertEquals(novaPrimeiraOcorrencia, tarefa.getProximaOcorrencia());
    }


    @Test
    void deveCalcularProximaOcorrenciaDiariaParaHoje() {
        // Arrange
        // Hoje é HOJE. Primeira ocorrência é HOJE. Início da recorrência é HOJE.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Diaria Hoje", LocalDate.now(), Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), null);

        // Assert
        // A próxima ocorrência deve ser hoje, pois a primeira ocorrência é hoje e não passou.
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void devePularOcorrenciasPassadasEDefinirProximaDiaria() {
        // Arrange
        // Hoje é HOJE. Primeira ocorrência é HOJE-5 dias.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Diaria Passada", LocalDate.now().minusDays(5), Prioridade.MEDIA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now().minusDays(5), null);

        // Assert
        // Deve pular as 5 ocorrências passadas e definir a próxima ocorrência como HOJE
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveCalcularProximaOcorrenciaSemanal() {
        // Arrange
        // Hoje é HOJE. Primeira ocorrência base é HOJE-2 dias.
        // Se hoje é Sábado (12/07), HOJE-2 dias é Quinta (10/07).
        // A próxima ocorrência semanal a partir de 10/07 será 17/07.
        // Para ser robusto, o ideal é testar com um dia da semana que seja anterior a hoje.
        LocalDate dataBase = LocalDate.now().minusDays(2); // Ex: Se hoje é Sábado, dataBase é Quinta
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Semanal", dataBase, Prioridade.ALTA,
                FrequenciaRecorrencia.SEMANAL, dataBase, null);

        // Assert
        assertEquals(dataBase.plusWeeks(1), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveCalcularProximaOcorrenciaQuinzenal() {

        LocalDate dataBase = LocalDate.now().minusDays(10);
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Quinzenal", dataBase, Prioridade.URGENTE,
                FrequenciaRecorrencia.QUINZENAL, dataBase, null);

        assertEquals(LocalDate.now().plusDays(5), tarefa.getProximaOcorrencia(),
                "A próxima ocorrência quinzenal deve ser 5 dias a partir de hoje.");
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus(),
                "O status da tarefa deve ser PENDENTE.");
    }

    @Test
    void deveCalcularProximaOcorrenciaMensal() {
        // Arrange
        // Hoje é HOJE. Primeira ocorrência base é HOJE-1 mês.
        LocalDate dataBase = LocalDate.now().minusMonths(1);
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Mensal", dataBase, Prioridade.MEDIA,
                FrequenciaRecorrencia.MENSAL, dataBase, null);

        // Assert
        assertEquals(dataBase.plusMonths(1), tarefa.getProximaOcorrencia()); // Será LocalDate.now()
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveCalcularProximaOcorrenciaAnual() {
        // Arrange
        // Hoje é HOJE. Primeira ocorrência base é HOJE-1 ano.
        LocalDate dataBase = LocalDate.now().minusYears(1);
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Anual", dataBase, Prioridade.BAIXA,
                FrequenciaRecorrencia.ANUAL, dataBase, null);

        // Assert
        assertEquals(dataBase.plusYears(1), tarefa.getProximaOcorrencia()); // Será LocalDate.now()
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveMarcarConcluidaSeProximaOcorrenciaForAposDataFimNoCalculoInicial() {
        // Arrange
        // Hoje é HOJE. Data de fim é HOJE-1. Primeira ocorrência é HOJE-5.
        // A lógica deve avançar e perceber que já passou do fim.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Ja terminou", LocalDate.now().minusDays(5), Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)); // Fim: ontem

        // Assert
        assertNull(tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
        assertNull(tarefa.getDataVencimento());
    }

    @Test
    void deveLidarComDataInicioRecorrenciaFuturaNoCalculoInicial() {
        // Arrange
        LocalDate dataInicioFutura = LocalDate.now().plusDays(5); // Início em 5 dias
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Começa Em Breve", LocalDate.now(), Prioridade.MEDIA,
                FrequenciaRecorrencia.DIARIA, dataInicioFutura, null);

        // Assert
        // A próxima ocorrência deve ser a data de início da recorrência, pois ela é futura.
        assertEquals(dataInicioFutura, tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveAvancarProximaOcorrenciaDiariaAoConcluir() {
        // Arrange
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Diaria para concluir", LocalDate.now(), Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), null);
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());

        // Act
        tarefa.concluir();

        // Assert
        assertEquals(LocalDate.now().plusDays(1), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus()); // Volta a ser pendente para a próxima ocorrência
        assertEquals(LocalDate.now().plusDays(1), tarefa.getDataVencimento()); // dataVencimento do pai atualizada
    }

    @Test
    void deveAvancarProximaOcorrenciaSemanalAoConcluir() {
        // Arrange
        LocalDate inicioSemanal = LocalDate.now(); // Ex: 12/07 (Sábado)
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Semanal para concluir", inicioSemanal, Prioridade.MEDIA,
                FrequenciaRecorrencia.SEMANAL, inicioSemanal, null);
        assertEquals(inicioSemanal, tarefa.getProximaOcorrencia());

        // Act
        tarefa.concluir();

        // Assert
        // A próxima ocorrência semanal a partir de hoje será HOJE+7 dias.
        assertEquals(inicioSemanal.plusWeeks(1), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveAvancarProximaOcorrenciaQuinzenalAoConcluir() {
        // Arrange
        LocalDate inicioQuinzenal = LocalDate.now();
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Quinzenal para concluir", inicioQuinzenal, Prioridade.ALTA,
                FrequenciaRecorrencia.QUINZENAL, inicioQuinzenal, null);
        assertEquals(inicioQuinzenal, tarefa.getProximaOcorrencia());

        // Act
        tarefa.concluir();

        // Assert
        assertEquals(inicioQuinzenal.plusDays(15), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveAvancarProximaOcorrenciaMensalAoConcluir() {
        // Arrange
        LocalDate inicioMensal = LocalDate.now();
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Mensal para concluir", inicioMensal, Prioridade.URGENTE,
                FrequenciaRecorrencia.MENSAL, inicioMensal, null);
        assertEquals(inicioMensal, tarefa.getProximaOcorrencia());

        // Act
        tarefa.concluir();

        // Assert
        assertEquals(inicioMensal.plusMonths(1), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveAvancarProximaOcorrenciaAnualAoConcluir() {
        // Arrange
        LocalDate inicioAnual = LocalDate.now();
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Anual para concluir", inicioAnual, Prioridade.BAIXA,
                FrequenciaRecorrencia.ANUAL, inicioAnual, null);
        assertEquals(inicioAnual, tarefa.getProximaOcorrencia());

        // Act
        tarefa.concluir();

        // Assert
        assertEquals(inicioAnual.plusYears(1), tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    void deveMarcarConcluidaSeAtingirDataFimAoConcluir() {
        // Arrange
        // Hoje é HOJE. Data fim é HOJE + 1 dia.
        // A próxima ocorrência inicial é HOJE.
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Termina Amanha", LocalDate.now(), Prioridade.MEDIA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now(), LocalDate.now().plusDays(1)); // Fim: Amanhã
        assertEquals(LocalDate.now(), tarefa.getProximaOcorrencia());

        // Act
        tarefa.concluir(); // Conclui a ocorrência de HOJE, avança para AMANHÃ (dentro do limite)
        assertEquals(LocalDate.now().plusDays(1), tarefa.getProximaOcorrencia()); // Próxima é amanhã
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus()); // Ainda pendente para amanhã

        tarefa.concluir(); // Conclui AMANHÃ, tenta avançar para DEPOIS DE AMANHÃ, que está fora do limite
        // Assert
        assertNull(tarefa.getProximaOcorrencia(), "Próxima ocorrência deve ser nula se ultrapassar data fim.");
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus(), "Status deve ser CONCLUIDA se não há mais ocorrências.");
        assertNull(tarefa.getDataVencimento(), "Data de vencimento do pai deve ser nula.");
    }

    @Test
    void naoDeveAvancarSeJaNaoHouverProximaOcorrencia() {
        // Arrange
        // Cria uma tarefa que já está no fim da recorrência (ou já foi concluída/passada)
        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Ja Concluida", LocalDate.now().minusDays(5), Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)); // Fim: Ontem
        assertNull(tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());

        // Act
        tarefa.concluir(); // Tenta concluir novamente

        // Assert
        assertNull(tarefa.getProximaOcorrencia(), "Não deve avançar se já não há próxima ocorrência.");
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus(), "Status deve permanecer CONCLUIDA.");
    }

    @Test
    void toStringDeveFormatarCorretamenteTarefaRecorrente() {
        // Arrange
        LocalDate dataPrimeira = LocalDate.now().minusDays(5);
        LocalDate dataInicio = LocalDate.now().minusDays(3);
        LocalDate dataFim = LocalDate.now().plusMonths(2);

        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Projeto A", dataPrimeira, Prioridade.ALTA,
                FrequenciaRecorrencia.MENSAL, dataInicio, dataFim);

        // A proxima ocorrência será a primeira ocorrência mensal após dataInicio que seja >= HOJE
        // Se HOJE é 12/07, dataInicio é 09/07. Primeira ocorrência Mensal a partir de 09/07 é 09/07.
        // Se ela for concluída, avança para 09/08.
        // O toString deve refletir o estado atual da tarefa.
        String expectedString = String.format(
                "ID: %d, Descrição: Projeto A, Prioridade: ALTA, Status: PENDENTE, Frequência: MENSAL, Início Recorrência: %s, Primeira Ocorrência: %s, Fim Recorrência: %s, Próxima Ocorrência: %s, Cadastrada em: %s",
                tarefa.getId(),
                dataInicio.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                dataPrimeira.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                dataFim.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                tarefa.getProximaOcorrencia().format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                LocalDate.now().format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER)
        );

        // Act
        String actualString = tarefa.toString();

        // Assert
        assertEquals(expectedString, actualString, "O toString() deve formatar a tarefa recorrente corretamente.");
    }

    @Test
    void toStringDeveIndicarFinalizadaQuandoNaoHaProximaOcorrencia() {
        // Arrange
        // Tarefa que já terminou
        LocalDate dataPrimeira = LocalDate.now().minusYears(1);
        LocalDate dataFim = LocalDate.now().minusDays(1); // Fim ontem

        TarefaRecorrente tarefa = new TarefaRecorrente(IdGenerator.generateNewId(), "Tarefa Já Finalizada", dataPrimeira, Prioridade.BAIXA,
                FrequenciaRecorrencia.DIARIA, dataPrimeira, dataFim);

        // Assert que ela realmente está finalizada
        assertNull(tarefa.getProximaOcorrencia());
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());

        String expectedString = String.format(
                "ID: %d, Descrição: Tarefa Já Finalizada, Prioridade: BAIXA, Status: CONCLUIDA, Frequência: DIARIA, Início Recorrência: %s, Primeira Ocorrência: %s, Fim Recorrência: %s, Próxima Ocorrência: FINALIZADA, Cadastrada em: %s",
                tarefa.getId(),
                dataPrimeira.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                dataPrimeira.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                dataFim.format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER),
                LocalDate.now().format(com.lucas.appgestaopessoal.util.DateTimeFormatterUtil.DATE_FORMATTER)
        );

        // Act
        String actualString = tarefa.toString();

        // Assert
        assertEquals(expectedString, actualString, "O toString() deve indicar 'FINALIZADA' quando não há próxima ocorrência.");
    }
}