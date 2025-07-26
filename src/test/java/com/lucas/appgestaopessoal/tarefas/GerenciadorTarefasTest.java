package com.lucas.appgestaopessoal.tarefas;

import com.lucas.appgestaopessoal.util.FrequenciaRecorrencia;
import com.lucas.appgestaopessoal.util.IdGenerator;
import com.lucas.appgestaopessoal.util.Prioridade;
import com.lucas.appgestaopessoal.util.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        LocalDate vencimento = LocalDate.of(2025, 7, 5);
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
        assertEquals(0, gerenciadorTarefas.listarTodasTarefas().size(), "A lista deve estar vazia.");
    }

    @Test
    void deveBuscarTarefaPorId() {
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
    void naoDeveBuscarTarefaPorIdInexistente() {
        // Arrange
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        // Act
        Tarefa tarefaNaoEncontrada = gerenciadorTarefas.buscarTarefaPorId(99);

        // Assert
        assertNull(tarefaNaoEncontrada, "A tarefa não deve ser encontrada após busca por ID.");

    }

    @Test
    void deveBuscarTarefaPorTexto() {
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);

        List<Tarefa> tarefasEncontradas = gerenciadorTarefas.buscarTarefaPorTexto("relatorio");

        assertNotNull(tarefasEncontradas, "A tarefa deve ser encontrada após busca por texto.");
        assertEquals(1, tarefasEncontradas.size(), "Deve haver uma tarefa adicionada.");
        assertEquals("Terminar relatório", tarefasEncontradas.get(0).getDescricao(), "A descrição da tarefa é inválida.");
        assertEquals(LocalDate.now().plusDays(2), tarefasEncontradas.get(0).getDataVencimento(), "A data de vencimento da tarefa é inválida.");
        assertEquals(Prioridade.URGENTE, tarefasEncontradas.get(0).getPrioridade(), "A prioridade da tarefa é inválida.");


    }

    @Test
    void naoDeveBuscarTarefaPorTextoInexistente() {
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);

        List<Tarefa> tarefasEncontradas = gerenciadorTarefas.buscarTarefaPorTexto("estudar");

        assertNotNull(tarefasEncontradas, "A lista de tarefas encontradas não deve ser nula.");
        assertTrue(tarefasEncontradas.isEmpty(), "A lista de tarefas encontradas deve estar vazia quando o texto não existe.");
        assertEquals(0, tarefasEncontradas.size(), "A lista de tarefas encontradas deve ter 0 elementos.");


    }

    @Test
    void deveListarTarefasPendentes() {
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Comprar ração", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.concluirTarefa(3);

        List<Tarefa> tarefasPendentes = gerenciadorTarefas.listarTarefasPendentes();

        assertNotNull(tarefasPendentes, "A lista de tarefas pendentes não deve ser vazia.");
        assertEquals(2, tarefasPendentes.size(), "Deve haver duas tarefas pendentes.");
        assertTrue(tarefasPendentes.stream().anyMatch(t -> t.getDescricao().equals("Terminar relatório")),
                "A tarefa 'Terminar relatório' deve estar pendente.");
        assertTrue(tarefasPendentes.stream().anyMatch(t -> t.getDescricao().equals("Comprar comida")),
                "A tarefa 'Comprar comida' deve estar pendente.");
        assertFalse(tarefasPendentes.stream().anyMatch(t -> t.getDescricao().equals("Comprar ração")),
                "A tarefa 'Comprar ração' não deve estar na lista de tarefas pendentes.");

    }

    @Test
    void deveListarTarefasConcluidas() {
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Comprar ração", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.concluirTarefa(3);
        gerenciadorTarefas.cancelarTarefa(1);

        List<Tarefa> tarefasConcluidas = gerenciadorTarefas.listarTarefasConcluidas();

        assertNotNull(tarefasConcluidas, "A lista de tarefas pendentes não deve ser vazia.");
        assertEquals(1, tarefasConcluidas.size(), "Deve haver uma tarefa concluída.");
        assertFalse(tarefasConcluidas.stream().anyMatch(t -> t.getDescricao().equals("Terminar relatório")),
                "A tarefa 'Terminar relatório' não deve estar na lista de tarefas concluídas.");
        assertFalse(tarefasConcluidas.stream().anyMatch(t -> t.getDescricao().equals("Comprar comida")),
                "A tarefa 'Comprar comida' não deve estar na lista de tarefas concluídas.");
        assertTrue(tarefasConcluidas.stream().anyMatch(t -> t.getDescricao().equals("Comprar ração")),
                "A tarefa 'Comprar ração' deve estar na lista de tarefas concluídas.");
    }

    @Test
    void deveListarTarefasCanceladas() {
        gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Comprar comida", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Comprar ração", LocalDate.now().plusDays(1), Prioridade.BAIXA);
        gerenciadorTarefas.concluirTarefa(3);
        gerenciadorTarefas.cancelarTarefa(1);

        List<Tarefa> tarefasCanceladas = gerenciadorTarefas.listarTarefasCanceladas();

        assertNotNull(tarefasCanceladas, "A lista de tarefas pendentes não deve ser vazia.");
        assertEquals(1, tarefasCanceladas.size(), "Deve haver uma tarefa cancelada.");
        assertTrue(tarefasCanceladas.stream().anyMatch(t -> t.getDescricao().equals("Terminar relatório")),
                "A tarefa 'Terminar relatório' deve estar na lista de tarefas canceladas.");
        assertFalse(tarefasCanceladas.stream().anyMatch(t -> t.getDescricao().equals("Comprar comida")),
                "A tarefa 'Comprar comida' não deve estar na lista de tarefas canceladas.");
        assertFalse(tarefasCanceladas.stream().anyMatch(t -> t.getDescricao().equals("Comprar ração")),
                "A tarefa 'Comprar ração' não deve estar na lista de tarefas canceladas.");

    }

    @Test
    void deveListarTarefasSimples() {
        Tarefa tarefaSimples1 = new Tarefa(IdGenerator.generateNewId(), "Comprar pão", LocalDate.now().plusDays(1), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now()); // ID 1
        Tarefa tarefaSimples2 = new Tarefa(IdGenerator.generateNewId(), "Pagar contas", LocalDate.now().plusDays(5), Prioridade.URGENTE, StatusTarefa.PENDENTE, LocalDate.now()); // ID 2
        TarefaRecorrente tarefaRecorrente1 = new TarefaRecorrente(IdGenerator.generateNewId(), "Reunião semanal", LocalDate.now().plusDays(7), Prioridade.MEDIA, FrequenciaRecorrencia.SEMANAL, LocalDate.now(), null); // ID 3
        TarefaRecorrente tarefaRecorrente2 = new TarefaRecorrente(IdGenerator.generateNewId(), "Fazer backup", LocalDate.now().plusDays(30), Prioridade.BAIXA, FrequenciaRecorrencia.MENSAL, LocalDate.now(), LocalDate.now().plusYears(1)); // ID 4

        gerenciadorTarefas.adicionarTarefa(tarefaSimples1);
        gerenciadorTarefas.adicionarTarefa(tarefaSimples2);
        gerenciadorTarefas.adicionarTarefa(tarefaRecorrente1);
        gerenciadorTarefas.adicionarTarefa(tarefaRecorrente2);

        List<Tarefa> tarefasSimples = gerenciadorTarefas.listarTarefasSimples();

        assertNotNull(tarefasSimples, "A lista de tarefas simples não deve ser nula.");
        assertEquals(2, tarefasSimples.size(), "Deve haver duas tarefas simples.");

        assertTrue(tarefasSimples.contains(tarefaSimples1),
                "A tarefa 'Comprar pão' deve estar na lista de tarefas simples.");
        assertTrue(tarefasSimples.contains(tarefaSimples2),
                "A tarefa 'Pagar contas' deve estar na lista de tarefas simples.");
        assertFalse(tarefasSimples.contains(tarefaRecorrente1),
                "A tarefa 'Reunião semanal' não deve estar na lista de tarefas simples.");
        assertFalse(tarefasSimples.contains(tarefaRecorrente2),
                "A tarefa 'Fazer backup' não deve estar na lista de tarefas simples.");
        for (Tarefa tarefa : tarefasSimples) {
            assertFalse(tarefa instanceof TarefaRecorrente, "Nenhuma tarefa na lista deve ser uma TarefaRecorrente.");
        }
    }

    @Test
    void deveListarTarefasRecorrentes() {
        Tarefa tarefaSimples1 = new Tarefa(IdGenerator.generateNewId(), "Comprar pão", LocalDate.now().plusDays(1), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now()); // ID 1
        Tarefa tarefaSimples2 = new Tarefa(IdGenerator.generateNewId(), "Pagar contas", LocalDate.now().plusDays(5), Prioridade.URGENTE, StatusTarefa.PENDENTE, LocalDate.now()); // ID 2
        TarefaRecorrente tarefaRecorrente1 = new TarefaRecorrente(IdGenerator.generateNewId(), "Reunião semanal", LocalDate.now().plusDays(7), Prioridade.MEDIA, FrequenciaRecorrencia.SEMANAL, LocalDate.now(), null); // ID 3
        TarefaRecorrente tarefaRecorrente2 = new TarefaRecorrente(IdGenerator.generateNewId(), "Fazer backup", LocalDate.now().plusDays(30), Prioridade.BAIXA, FrequenciaRecorrencia.MENSAL, LocalDate.now(), LocalDate.now().plusYears(1)); // ID 4

        gerenciadorTarefas.adicionarTarefa(tarefaSimples1);
        gerenciadorTarefas.adicionarTarefa(tarefaSimples2);
        gerenciadorTarefas.adicionarTarefa(tarefaRecorrente1);
        gerenciadorTarefas.adicionarTarefa(tarefaRecorrente2);

        List<Tarefa> tarefasRecorrentes = gerenciadorTarefas.listarTarefasRecorrentes();

        assertNotNull(tarefasRecorrentes, "A lista de tarefas simples não deve ser nula.");
        assertEquals(2, tarefasRecorrentes.size(), "Deve haver duas tarefas recorrentes.");
        assertFalse(tarefasRecorrentes.contains(tarefaSimples1),
                "A tarefa 'Comprar pão' não deve estar na lista de tarefas recorrentes.");
        assertFalse(tarefasRecorrentes.contains(tarefaSimples2),
                "A tarefa 'Pagar contas' não deve estar na lista de tarefas recorrentes.");
        assertTrue(tarefasRecorrentes.contains(tarefaRecorrente1),
                "A tarefa 'Reunião semanal' deve estar na lista de tarefas recorrentes.");
        assertTrue(tarefasRecorrentes.contains(tarefaRecorrente2),
                "A tarefa 'Fazer backup' deve estar na lista de tarefas recorrentes.");
        for (Tarefa tarefa : tarefasRecorrentes) {
            assertInstanceOf(TarefaRecorrente.class, tarefa, "Cada tarefa na lista deve ser uma TarefaRecorrente.");
            assertNotEquals(Tarefa.class, tarefa.getClass(), "Nenhuma tarefa na lista deve ser uma Tarefa simples (não recorrente).");

        }
    }

    @Test
    void deveListarTarefasOrdenadasPorVencimento() {
        gerenciadorTarefas.adicionarTarefa("Tarefa Vence em 30 dias", LocalDate.now().plusDays(30), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Tarefa Vence Amanhã", LocalDate.now().plusDays(1), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Tarefa Vence Hoje", LocalDate.now(), Prioridade.ALTA);
        gerenciadorTarefas.adicionarTarefa("Tarefa Vence em 10 dias", LocalDate.now().plusDays(10), Prioridade.MEDIA);

        List<Tarefa> tarefasPorVencimento = gerenciadorTarefas.listarTarefasOrdenadasPorVencimento();

        assertNotNull(tarefasPorVencimento, "A lista de tarefas simples não deve ser nula.");
        assertEquals(4, tarefasPorVencimento.size(), "Deve haver quatro tarefas na lista.");

        for (int i = 0; i < tarefasPorVencimento.size() - 1; i++) {
            LocalDate dataAtual = tarefasPorVencimento.get(i).getDataVencimento();
            LocalDate dataProxima = tarefasPorVencimento.get(i + 1).getDataVencimento();

            assertTrue(dataAtual.isBefore(dataProxima) || dataAtual.isEqual(dataProxima),
                    "As tarefas não estão em ordem crescente de vencimento. Problema entre '" +
                            tarefasPorVencimento.get(i).getDescricao() + "' (" + dataAtual + ") e '" +
                            tarefasPorVencimento.get(i + 1).getDescricao() + "' (" + dataProxima + ")");


        }
    }

    @Test
    void deveListarTarefasOrdenadasPorPriodirdade() {
        gerenciadorTarefas.adicionarTarefa("Tarefa com prioridade BAIXA.", LocalDate.now().plusDays(30), Prioridade.BAIXA);
        gerenciadorTarefas.adicionarTarefa("Tarefa com prioridade URGENTE", LocalDate.now().plusDays(1), Prioridade.URGENTE);
        gerenciadorTarefas.adicionarTarefa("Tarefa com prioridade ALTA", LocalDate.now(), Prioridade.ALTA);
        gerenciadorTarefas.adicionarTarefa("Tarefa com prioridade MEDIA", LocalDate.now().plusDays(10), Prioridade.MEDIA);

        List<Tarefa> tarefasPorPrioridade = gerenciadorTarefas.listarTarefasOrdenadasPorPrioridade();

        assertNotNull(tarefasPorPrioridade, "A lista de tarefas simples não deve ser nula.");
        assertEquals(4, tarefasPorPrioridade.size(), "Deve haver quatro tarefas na lista.");

        for (int i = 0; i < tarefasPorPrioridade.size() - 1; i++) {
            Prioridade prioridadeAtual = tarefasPorPrioridade.get(i).getPrioridade();
            Prioridade prioridadeProxima = tarefasPorPrioridade.get(i + 1).getPrioridade();

            assertTrue(prioridadeAtual.compareTo(prioridadeProxima) <= 0,
                    "As tarefas não estão em ordem crescente de prioridade. Problema entre '" +
                            tarefasPorPrioridade.get(i).getDescricao() + "' (" + prioridadeAtual + ") e '" +
                            tarefasPorPrioridade.get(i+1).getDescricao() + "' (" + prioridadeProxima + ")");


        }
        assertEquals(Prioridade.URGENTE, tarefasPorPrioridade.get(0).getPrioridade(), "A última tarefa deve ter prioridade URGENTE.");
        assertEquals(Prioridade.BAIXA, tarefasPorPrioridade.get(tarefasPorPrioridade.size() - 1).getPrioridade(), "A primeira tarefa deve ter prioridade BAIXA.");


    }

    @Test
    void deveAtualizarDescricaoDaTarefa() {
        Tarefa tarefaParaAtualizar = gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        tarefaParaAtualizar.setDescricao("Terminar relatório atualizado.");
        gerenciadorTarefas.atualizarTarefa(tarefaParaAtualizar);

        Tarefa tarefaAtualizada = gerenciadorTarefas.buscarTarefaPorId(tarefaParaAtualizar.getId());
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertNotNull(tarefaAtualizada, "A tarefa deve ser encontrada após atualizar a descrição.");
        assertEquals("Terminar relatório atualizado.", tarefaAtualizada.getDescricao(), "A descrição da tarefa deve ser 'Terminar relatório atualizado.'.");
        assertEquals(LocalDate.now().plusDays(2), tarefaAtualizada.getDataVencimento(), "A data de vencimento não deve mudar.");
        assertEquals(Prioridade.URGENTE, tarefaAtualizada.getPrioridade(), "A prioridade não deve mudar.");
        assertEquals(StatusTarefa.PENDENTE, tarefaAtualizada.getStatus(), "O status não deve mudar.");
    }

    @Test
    void deveAtualizarDataDeVencimentoDaTarefa() {
        Tarefa tarefaParaAtualizar = gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        tarefaParaAtualizar.setDataVencimento(LocalDate.now().plusDays(4));
        gerenciadorTarefas.atualizarTarefa(tarefaParaAtualizar);

        Tarefa tarefaAtualizada = gerenciadorTarefas.buscarTarefaPorId(tarefaParaAtualizar.getId());
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertNotNull(tarefaAtualizada, "A tarefa deve ser encontrada após atualizar a data de vencimento.");
        assertEquals("Terminar relatório", tarefaAtualizada.getDescricao(), "A descrição da tarefa não deve ser atualizada.");
        assertEquals(LocalDate.now().plusDays(4), tarefaAtualizada.getDataVencimento(), "A data de vencimento deve ser '" + LocalDate.now().plusDays(4) +"'.");
        assertEquals(Prioridade.URGENTE, tarefaAtualizada.getPrioridade(), "A prioridade não deve mudar.");
        assertEquals(StatusTarefa.PENDENTE, tarefaAtualizada.getStatus(), "O status não deve mudar.");
    }

    @Test
    void deveAtualizarPrioridadeDaTarefa() {
        Tarefa tarefaParaAtualizar = gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        tarefaParaAtualizar.setPrioridade(Prioridade.BAIXA);
        gerenciadorTarefas.atualizarTarefa(tarefaParaAtualizar);

        Tarefa tarefaAtualizada = gerenciadorTarefas.buscarTarefaPorId(tarefaParaAtualizar.getId());
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertNotNull(tarefaAtualizada, "A tarefa deve ser encontrada após atualizar a prioridade.");
        assertEquals("Terminar relatório", tarefaAtualizada.getDescricao(), "A descrição da tarefa não deve ser atualizada.");
        assertEquals(LocalDate.now().plusDays(2), tarefaAtualizada.getDataVencimento(), "A data de vencimento deve ser '" + LocalDate.now().plusDays(2) +"'.");
        assertEquals(Prioridade.BAIXA, tarefaAtualizada.getPrioridade(), "A prioridade deve ser 'BAIXA' após atualização.");
        assertEquals(StatusTarefa.PENDENTE, tarefaAtualizada.getStatus(), "O status não deve mudar.");
    }

    @Test
    void deveAtualizarStatusDaTarefa() {
        Tarefa tarefaParaAtualizar = gerenciadorTarefas.adicionarTarefa("Terminar relatório", LocalDate.now().plusDays(2), Prioridade.URGENTE);

        tarefaParaAtualizar.setStatus(StatusTarefa.CANCELADA);
        gerenciadorTarefas.atualizarTarefa(tarefaParaAtualizar);

        Tarefa tarefaAtualizada = gerenciadorTarefas.buscarTarefaPorId(tarefaParaAtualizar.getId());
        assertEquals(1, gerenciadorTarefas.listarTodasTarefas().size(), "Deve haver uma tarefa adicionada.");
        assertNotNull(tarefaAtualizada, "A tarefa deve ser encontrada após atualizar o status.");
        assertEquals("Terminar relatório", tarefaAtualizada.getDescricao(), "A descrição da tarefa não deve ser atualizada.");
        assertEquals(LocalDate.now().plusDays(2), tarefaAtualizada.getDataVencimento(), "A data de vencimento deve ser '" + LocalDate.now().plusDays(2) +"'.");
        assertEquals(Prioridade.URGENTE, tarefaAtualizada.getPrioridade(), "A prioridade não deve mudar.");
        assertEquals(StatusTarefa.CANCELADA, tarefaAtualizada.getStatus(), "O status deve ser 'CANCELADA' após atualização.");
    }

    @Test
    void deveSugerirTarefaPrioritariaCorretamenteComPrioridadesDiferentes() {
        Tarefa tarefaAlta = new Tarefa(IdGenerator.generateNewId(), "Preparar apresentação", LocalDate.now().plusDays(5), Prioridade.ALTA, StatusTarefa.PENDENTE, LocalDate.now());
        Tarefa tarefaMedia = new Tarefa(IdGenerator.generateNewId(), "Responder e-mails", LocalDate.now().plusDays(1), Prioridade.MEDIA, StatusTarefa.PENDENTE, LocalDate.now());
        Tarefa tarefaBaixa = new Tarefa(IdGenerator.generateNewId(), "Organizar arquivos", LocalDate.now().plusDays(10), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now());
        Tarefa tarefaUrgente = new Tarefa(IdGenerator.generateNewId(), "Revisar relatório final", LocalDate.now().plusDays(7), Prioridade.URGENTE, StatusTarefa.PENDENTE, LocalDate.now());

        gerenciadorTarefas.adicionarTarefa(tarefaAlta);
        gerenciadorTarefas.adicionarTarefa(tarefaMedia);
        gerenciadorTarefas.adicionarTarefa(tarefaBaixa);
        gerenciadorTarefas.adicionarTarefa(tarefaUrgente);

        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        assertTrue(tarefaSugerida.isPresent(), "Deve haver uma tarefa sugerida.");
        assertEquals(tarefaUrgente.getId(), tarefaSugerida.get().getId(), "A tarefa sugerida deve ser a URGENTE.");
        assertEquals("Revisar relatório final", tarefaSugerida.get().getDescricao(), "A descrição da tarefa sugerida é incorreta.");
    }

    @Test
    void deveSugerirTarefaPrioritariaCorretamenteComMesmaPrioridadeEDiferentesDatas() {
        Tarefa tarefaBaixaAmanha = new Tarefa(IdGenerator.generateNewId(), "Verificar estoque", LocalDate.now().plusDays(1), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now());
        Tarefa tarefaBaixa3Dias = new Tarefa(IdGenerator.generateNewId(), "Atualizar software", LocalDate.now().plusDays(3), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now());
        Tarefa tarefaBaixaHoje = new Tarefa(IdGenerator.generateNewId(), "Limpar mesa", LocalDate.now(), Prioridade.BAIXA, StatusTarefa.PENDENTE, LocalDate.now());

        gerenciadorTarefas.adicionarTarefa(tarefaBaixa3Dias);
        gerenciadorTarefas.adicionarTarefa(tarefaBaixaAmanha);
        gerenciadorTarefas.adicionarTarefa(tarefaBaixaHoje);

        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        assertTrue(tarefaSugerida.isPresent(), "Deve haver uma tarefa sugerida.");
        assertEquals(tarefaBaixaHoje.getId(), tarefaSugerida.get().getId(), "A tarefa sugerida deve ser a BAIXA que vence hoje.");
        assertEquals("Limpar mesa", tarefaSugerida.get().getDescricao(), "A descrição da tarefa sugerida é incorreta.");
    }

    @Test
    void deveRetornarOptionalVazioSeNaoHouverTarefas() {

        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        assertFalse(tarefaSugerida.isPresent(), "Não deve haver tarefa sugerida se a lista estiver vazia.");
    }

    @Test
    void deveRetornarOptionalVazioSeTodasAsTarefasEstiveremConcluidas() {

        Tarefa tarefaConcluida = gerenciadorTarefas.adicionarTarefa("Comprar café", LocalDate.now(), Prioridade.BAIXA);
        gerenciadorTarefas.concluirTarefa(tarefaConcluida.getId());

        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        assertFalse(tarefaSugerida.isPresent(), "Não deve haver tarefa sugerida se todas estiverem concluídas.");
    }

    @Test
    void deveRetornarOptionalVazioSeTodasAsTarefasEstiveremCanceladas() {
        Tarefa tarefaCancelada = gerenciadorTarefas.adicionarTarefa("Comprar café", LocalDate.now(), Prioridade.BAIXA);
        gerenciadorTarefas.cancelarTarefa(tarefaCancelada.getId()); // Marca como cancelada

        Optional<Tarefa> tarefaSugerida = gerenciadorTarefas.sugerirTarefaPrioritaria();

        assertFalse(tarefaSugerida.isPresent(), "Não deve haver tarefa sugerida se todas estiverem canceladas.");
    }

}
