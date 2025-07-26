package com.lucas.appgestaopessoal.controller;

import com.lucas.appgestaopessoal.tarefas.GerenciadorTarefas;
import com.lucas.appgestaopessoal.tarefas.Tarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "http://localhost:4200") // Permite acesso do seu Angular local
public class TarefasController {

    @Autowired
    private GerenciadorTarefas gerenciadorTarefas;

    @GetMapping
    public List<Tarefa> listarTodas() {
        return gerenciadorTarefas.listarTodasTarefas();
    }

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable int id) {
        return gerenciadorTarefas.buscarTarefaPorId(id).orElse(null);
    }

    @PostMapping
    public Tarefa adicionar(@RequestBody Tarefa tarefa) {
        return gerenciadorTarefas.adicionarTarefa(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable int id, @RequestBody Tarefa tarefa) {
        // Não altere o id, apenas atualize os outros campos
        return gerenciadorTarefas.atualizarTarefa(tarefa);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable int id) {
        gerenciadorTarefas.removerTarefa(id);
    }
}