package com.lucas.appgestaopessoal.tarefas;
import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Testes seguindo AAA:
Arrange
Act
Assert
 */

public class GerenciadorTarefasTest {

    private GerenciadorTarefas gerenciadorTarefas;

    @BeforeEach
    void setUp() {
        gerenciadorTarefas = new GerenciadorTarefas();
        IdGenerator.resetId();
    }

    @Test
    void deveAdicionarTarefaSimplesCorretamente() {
        //Arrange
        String descricao = "Comprar leite";
        LocalDate vencimento = LocalDate.of(2025,7,5);
        Prioridade prioridade = Prioridade.ALTA;

        //Act
        Tarefa tarefaAdicionada = gerenciadorTarefas.adicionarTarefa(descricao, vencimento, prioridade);

        //Assert
        assertNotNull(tarefaAdicionada, "A tarefa adicionada não deve ser nula");
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertEquals(descricao, tarefaAdicionada.getDescricao(), "A descrição da tarefa deve ser 'Comprar leite'."); // Verifica o valor da descrição
        assertEquals(vencimento, tarefaAdicionada.getDataVencimento(), "A data de vencimento deve ser a definida.");
        assertEquals(prioridade, tarefaAdicionada.getPrioridade(), "A prioridade deve ser a definida.");
        assertEquals(StatusTarefa.PENDENTE, tarefaAdicionada.getStatus(), "O status inicial deve ser PENDENTE.");

    }

    @Test
    void deveConcluirTarefaExistente() {
        // Arrange
        Tarefa tarefaParaConcluir = gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        // Act
        gerenciadorTarefas.concluirTarefa(tarefaParaConcluir.getId());

        // Assert
        Tarefa tarefaVerificada = gerenciadorTarefas.buscarTarefaPorId(tarefaParaConcluir.getId());
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertNotNull(tarefaVerificada, "A tarefa deve ser encontrada após conclusão.");
        assertEquals(StatusTarefa.CONCLUIDA, tarefaVerificada.getStatus(), "O status da tarefa deve ser CONCLUIDA.");
    }

    @Test
    void naoDeveConcluirTarefaInexistente() {
        //Arrange
        //Nenhuma atividade adicionada inicialmente

        //Act
        boolean resultado = gerenciadorTarefas.concluirTarefa(999);

        //Assert
        assertEquals(0, gerenciadorTarefas.listarTodasTarefas().size(), "A lista de tarefas deve continuar vazia.");
        assertFalse(resultado, "Concluir tarefa inexistente deve retornar false.");
    }

    @Test
    void deveRemoverTarefaCorretamente() {
        //Arrange
        Tarefa tarefaParaRemover = gerenciadorTarefas
                .adicionarTarefa("Pagar aluguel", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        //Act
        gerenciadorTarefas.removerTarefa(tarefaParaRemover.getId());

        //Assert
        assertEquals(0,gerenciadorTarefas.listarTodasTarefas().size(), "A lista deve estar vazia." );
    }

    @Test
    void deveBuscarTarefaPorId(){
        // Arrange
        LocalDate vencimentoTarefa = LocalDate.now().plusDays(2);
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        // Act
        Tarefa tarefaEncontrada = gerenciadorTarefas.buscarTarefaPorId(1);

        // Assert
        assertNotNull(tarefaEncontrada, "A tarefa deve ser encontrada após busca por ID.");
        assertEquals("Terminar relatório", tarefaEncontrada.getDescricao(), "A descrição da tarefa é inválida.");
        assertEquals(vencimentoTarefa, tarefaEncontrada.getDataVencimento(), "A data de vencimento da tarefa é inválida.");
        assertEquals(Prioridade.URGENTE, tarefaEncontrada.getPrioridade(), "A prioridade da tarefa é inválida.");

    }

    @Test
    void deveBuscarTarefaPorTexto(){
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);

        List<Tarefa> tarefasEncontradas = gerenciadorTarefas.buscarTarefaPorTexto("relatorio");

        assertNotNull(tarefasEncontradas, "A tarefa deve ser encontrada após busca por texto.");
        assertEquals(1, tarefasEncontradas.size(), "Deve haver uma tarefa adicionada.");
        assertEquals("Terminar relatório", tarefasEncontradas.getFirst().getDescricao(), "A descrição da tarefa é inválida.");
        assertEquals(LocalDate.now().plusDays(2), tarefasEncontradas.getFirst().getDataVencimento(), "A data de vencimento da tarefa é inválida.");
        assertEquals(Prioridade.URGENTE, tarefasEncontradas.getFirst().getPrioridade(), "A prioridade da tarefa é inválida.");


    }

    @Test
    @Disabled
    void deveListarTarefasPendentes(){}

    @Test
    @Disabled
    void deveListarTarefasConcluidas(){}

    @Test
    @Disabled
    void deveListarTarefasCanceladas(){}

    @Test
    @Disabled
    void deveListarTarefasSimples(){}

    @Test
    @Disabled
    void deveListarTarefasRecorrentes(){}

    @Test
    @Disabled
    void deveListarTarefasOrdenadasPorVencimento(){}

    @Test
    @Disabled
    void deveListarTarefasOrdenadasPorPriodirdade(){}

    @Test
    @Disabled
    void deveAtualizarDescricaoDaTarefa(){}

    @Test
    @Disabled
    void deveAtualizarDataDeVencimentoDaTarefa(){}

    @Test
    @Disabled
    void deveAtualizarPrioridadeDaTarefa(){}

    @Test
    @Disabled
    void deveAtualizarStatusDaTarefa(){}


}
