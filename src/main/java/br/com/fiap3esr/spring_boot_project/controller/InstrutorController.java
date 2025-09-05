package br.com.fiap3esr.spring_boot_project.controller;

import br.com.fiap3esr.spring_boot_project.instrutor.DadosCadastroInstrutor;
import br.com.fiap3esr.spring_boot_project.instrutor.DadosListagemInstrutor;
import br.com.fiap3esr.spring_boot_project.instrutor.Instrutor;
import br.com.fiap3esr.spring_boot_project.instrutor.InstrutorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    @Autowired
    private InstrutorRepository repository;

    @PostMapping
    @Transactional
    public void cadastrarInstrutor(@RequestBody @Valid DadosCadastroInstrutor dados) {
        //System.out.println(dados);
        repository.save(new Instrutor(dados));
    }

    /*@GetMapping
    public List<Instrutor> listarInstrutores() {
        return repository.findAll();
    }*/

    @GetMapping
    public List<DadosListagemInstrutor> listarIntrutores() {
        return repository.findAll().stream().map(DadosListagemInstrutor::new).toList();
    }
}