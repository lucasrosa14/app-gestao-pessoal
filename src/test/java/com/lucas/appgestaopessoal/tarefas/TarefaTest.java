package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TarefaTest {

    private Tarefa tarefa;
    private final int TEST_ID = 1;
    private final String TEST_DESCRICAO = "Comprar pão";
    private final LocalDate TEST_DATA_VENCIMENTO = LocalDate.of(2025, 7, 15);
    private final Prioridade TEST_PRIORIDADE = Prioridade.MEDIA;
    private final StatusTarefa TEST_STATUS = StatusTarefa.PENDENTE;
    private final LocalDate TEST_DATA_CRIACAO = LocalDate.of(2025, 7, 10);

    // Configura uma nova tarefa antes de cada teste
    @BeforeEach
    void setUp() {
        tarefa = new Tarefa(TEST_ID, TEST_DESCRICAO, TEST_DATA_VENCIMENTO, TEST_PRIORIDADE, TEST_STATUS, TEST_DATA_CRIACAO);
    }

    @Test
    void deveCriarTarefaComAtributosCorretos() {
        // Arrange (já feito no setUp) & Act (construção da Tarefa)

        // Assert
        assertEquals(TEST_ID, tarefa.getId(), "O ID deve ser o mesmo que o passado no construtor.");
        assertEquals(TEST_DESCRICAO, tarefa.getDescricao(), "A descrição deve ser a mesma que a passada no construtor.");
        assertEquals(TEST_DATA_VENCIMENTO, tarefa.getDataVencimento(), "A data de vencimento deve ser a mesma que a passada no construtor.");
        assertEquals(TEST_PRIORIDADE, tarefa.getPrioridade(), "A prioridade deve ser a mesma que a passada no construtor.");
        assertEquals(TEST_STATUS, tarefa.getStatus(), "O status deve ser o mesmo que o passado no construtor.");
        assertEquals(TEST_DATA_CRIACAO, tarefa.getDataCriacao(), "A data de criação deve ser a mesma que a passada no construtor.");
    }

    @Test
    void deveAlterarDescricaoCorretamente() {
        // Arrange (tarefa configurada no setUp)
        String novaDescricao = "Comprar leite e ovos";

        // Act
        tarefa.setDescricao(novaDescricao);

        // Assert
        assertEquals(novaDescricao, tarefa.getDescricao(), "A descrição deve ser atualizada.");
    }

    @Test
    void deveAlterarDataVencimentoCorretamente() {
        // Arrange (tarefa configurada no setUp)
        LocalDate novaDataVencimento = LocalDate.of(2025, 8, 1);

        // Act
        tarefa.setDataVencimento(novaDataVencimento);

        // Assert
        assertEquals(novaDataVencimento, tarefa.getDataVencimento(), "A data de vencimento deve ser atualizada.");
    }

    @Test
    void deveAlterarStatusCorretamente() {
        // Arrange (tarefa configurada no setUp)
        StatusTarefa novoStatus = StatusTarefa.CONCLUIDA;

        // Act
        tarefa.setStatus(novoStatus);

        // Assert
        assertEquals(novoStatus, tarefa.getStatus(), "O status deve ser atualizado.");
    }

    @Test
    void deveAlterarPrioridadeCorretamente() {
        // Arrange (tarefa configurada no setUp)
        Prioridade novaPrioridade = Prioridade.URGENTE;

        // Act
        tarefa.setPrioridade(novaPrioridade);

        // Assert
        assertEquals(novaPrioridade, tarefa.getPrioridade(), "A prioridade deve ser atualizada.");
    }

    @Test
    void deveMarcarTarefaComoConcluida() {
        // Arrange (tarefa com status PENDENTE no setUp)

        // Act
        tarefa.concluir();

        // Assert
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus(), "O status deve ser CONCLUIDA após chamar concluir().");
        assertTrue(tarefa.isConcluido(), "isConcluido() deve retornar true para tarefa concluída.");
        assertFalse(tarefa.isCancelada(), "isCancelada() deve retornar false para tarefa concluída.");
    }

    @Test
    void deveMarcarTarefaComoCancelada() {
        // Arrange (tarefa com status PENDENTE no setUp)

        // Act
        tarefa.cancelar();

        // Assert
        assertEquals(StatusTarefa.CANCELADA, tarefa.getStatus(), "O status deve ser CANCELADA após chamar cancelar().");
        assertTrue(tarefa.isCancelada(), "isCancelada() deve retornar true para tarefa cancelada.");
        assertFalse(tarefa.isConcluido(), "isConcluido() deve retornar false para tarefa cancelada.");
    }

    @Test
    void isConcluidoDeveRetornarFalsoParaNaoConcluida() {
        // Arrange (tarefa com status PENDENTE no setUp)

        // Act & Assert
        assertFalse(tarefa.isConcluido(), "isConcluido() deve retornar false para tarefa pendente.");

        // Mudar para cancelada e verificar novamente
        tarefa.cancelar();
        assertFalse(tarefa.isConcluido(), "isConcluido() deve retornar false para tarefa cancelada.");
    }

    @Test
    void isCanceladaDeveRetornarFalsoParaNaoCancelada() {
        // Arrange (tarefa com status PENDENTE no setUp)

        // Act & Assert
        assertFalse(tarefa.isCancelada(), "isCancelada() deve retornar false para tarefa pendente.");

        // Mudar para concluída e verificar novamente
        tarefa.concluir();
        assertFalse(tarefa.isCancelada(), "isCancelada() deve retornar false para tarefa concluída.");
    }

    @Test
    void deveGerarStringCorretaNoToString() {
        // Arrange (tarefa configurada no setUp)
        // Data de vencimento: 15-07-2025
        // Data de criação: 10-07-2025
        String expectedString = "ID: 1, Descrição: Comprar pão, Vencimento: 15-07-2025, Prioridade: MEDIA, Status: PENDENTE, Cadastrada em: 10-07-2025";

        // Act
        String actualString = tarefa.toString();

        // Assert
        assertNotNull(actualString, "A string gerada pelo toString() não deve ser nula.");
        assertEquals(expectedString, actualString, "O toString() deve formatar a tarefa corretamente.");
    }

    @Test
    void toStringDeveLidarComDataVencimentoNula() {
        // Arrange
        Tarefa tarefaComVencimentoNulo = new Tarefa(2, "Tarefa sem vencimento", null, Prioridade.BAIXA, StatusTarefa.PENDENTE, TEST_DATA_CRIACAO);
        String expectedString = "ID: 2, Descrição: Tarefa sem vencimento, Vencimento: N/A, Prioridade: BAIXA, Status: PENDENTE, Cadastrada em: 10-07-2025";

        // Act
        String actualString = tarefaComVencimentoNulo.toString();

        // Assert
        assertEquals(expectedString, actualString, "O toString() deve lidar com data de vencimento nula.");
    }

    @Test
    void toStringDeveLidarComDataCriacaoNula() {
        // Arrange
        Tarefa tarefaComCriacaoNula = new Tarefa(3, "Tarefa sem criação", TEST_DATA_VENCIMENTO, Prioridade.BAIXA, StatusTarefa.PENDENTE, null);
        String expectedString = "ID: 3, Descrição: Tarefa sem criação, Vencimento: 15-07-2025, Prioridade: BAIXA, Status: PENDENTE, Cadastrada em: N/A";

        // Act
        String actualString = tarefaComCriacaoNula.toString();

        // Assert
        assertEquals(expectedString, actualString, "O toString() deve lidar com data de criação nula.");
    }
}
