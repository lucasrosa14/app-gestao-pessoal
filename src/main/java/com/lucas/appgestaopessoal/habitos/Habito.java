package com.lucas.appgestaopessoal.habitos;

import com.lucas.appgestaopessoal.util.IdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habito {

    private final int id;
    private String nome;
    private String descricaoHabito;
    private List<LocalDate> diasFeitos;
    private boolean ativo;
    private LocalDate dataCriacao;

    public Habito(String nome, String descricaoHabito) {
        this.id = IdGenerator.generateNewId();
        this.nome = nome;
        this.descricaoHabito = descricaoHabito;
        this.diasFeitos = new ArrayList<>();
        this.ativo = true;
        this.dataCriacao = LocalDate.now();
    }

    // Getters e Setters...
    public int getId() { return id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricaoHabito() { return descricaoHabito; }

    public void setDescricaoHabito(String descricaoHabito) { this.descricaoHabito = descricaoHabito; }

    public List<LocalDate> getDiasFeitos() { return diasFeitos; }

    public void setDiasFeitos(List<LocalDate> diasFeitos) { this.diasFeitos = diasFeitos; }

    public boolean isAtivo() { return ativo; }

    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDate getDataCriacao() { return dataCriacao; }

    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
}
