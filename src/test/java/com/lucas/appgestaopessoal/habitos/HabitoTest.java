package com.lucas.appgestaopessoal.habitos;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HabitoTest {

    @Test
    void testConstrutorInicializaCorretamente() {
        Habito habito = new Habito("Beber água", "Beber 2L de água por dia");
        assertNotNull(habito.getNome());
        assertNotNull(habito.getDescricaoHabito());
        assertNotNull(habito.getDiasFeitos());
        assertTrue(habito.isAtivo());
        assertNotNull(habito.getDataCriacao());
        assertTrue(habito.getId() > 0);
    }

    @Test
    void testGetSetNome() {
        Habito habito = new Habito("A", "B");
        habito.setNome("Novo Nome");
        assertEquals("Novo Nome", habito.getNome());
    }

    @Test
    void testGetSetDescricaoHabito() {
        Habito habito = new Habito("A", "B");
        habito.setDescricaoHabito("Nova Descrição");
        assertEquals("Nova Descrição", habito.getDescricaoHabito());
    }

    @Test
    void testGetSetDiasFeitos() {
        Habito habito = new Habito("A", "B");
        List<LocalDate> dias = new ArrayList<>();
        dias.add(LocalDate.of(2024, 6, 1));
        habito.setDiasFeitos(dias);
        assertEquals(1, habito.getDiasFeitos().size());
        assertEquals(LocalDate.of(2024, 6, 1), habito.getDiasFeitos().get(0));
    }

    @Test
    void testGetSetAtivo() {
        Habito habito = new Habito("A", "B");
        habito.setAtivo(false);
        assertFalse(habito.isAtivo());
        habito.setAtivo(true);
        assertTrue(habito.isAtivo());
    }

    @Test
    void testGetSetDataCriacao() {
        Habito habito = new Habito("A", "B");
        LocalDate novaData = LocalDate.of(2020, 1, 1);
        habito.setDataCriacao(novaData);
        assertEquals(novaData, habito.getDataCriacao());
    }
}