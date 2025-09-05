package br.com.fiap3esr.spring_boot_project.aluno;

import br.com.fiap3esr.spring_boot_project.endereco.DadosEndereco;
import jakarta.validation.Valid;

public record DadosAtualizacaoAluno(
        String nome,
        String telefone,
        
        @Valid
        DadosEndereco endereco) {
}
