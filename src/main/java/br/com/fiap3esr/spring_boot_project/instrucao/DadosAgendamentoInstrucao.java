package br.com.fiap3esr.spring_boot_project.instrucao;

import br.com.fiap3esr.spring_boot_project.aluno.Aluno;
import br.com.fiap3esr.spring_boot_project.instrutor.Instrutor;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoInstrucao(
        @NotNull
        Aluno aluno,
        
        Instrutor instrutor, // Opcional
        
        @NotNull
        @Future
        LocalDateTime dataHora) {
}
