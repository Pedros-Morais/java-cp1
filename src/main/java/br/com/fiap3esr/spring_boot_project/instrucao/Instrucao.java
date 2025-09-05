package br.com.fiap3esr.spring_boot_project.instrucao;

import br.com.fiap3esr.spring_boot_project.aluno.Aluno;
import br.com.fiap3esr.spring_boot_project.instrutor.Instrutor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "instrucoes")
@Entity(name = "Instrucao")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Instrucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;

    public Instrucao(DadosAgendamentoInstrucao dados) {
        this.aluno = dados.aluno();
        this.instrutor = dados.instrutor();
        this.dataHora = dados.dataHora();
        this.ativo = true;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.ativo = false;
        this.motivoCancelamento = motivo;
    }
}
