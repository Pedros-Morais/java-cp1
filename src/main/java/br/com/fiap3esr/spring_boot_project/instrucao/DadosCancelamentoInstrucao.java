package br.com.fiap3esr.spring_boot_project.instrucao;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(
        @NotNull
        MotivoCancelamento motivo) {
}
