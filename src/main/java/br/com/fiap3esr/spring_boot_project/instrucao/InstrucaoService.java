package br.com.fiap3esr.spring_boot_project.instrucao;

import br.com.fiap3esr.spring_boot_project.aluno.AlunoRepository;
import br.com.fiap3esr.spring_boot_project.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class InstrucaoService {

    @Autowired
    private InstrucaoRepository instrucaoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private InstrutorRepository instrutorRepository;

    public Instrucao agendarInstrucao(DadosAgendamentoInstrucao dados) {
        validarHorarioFuncionamento(dados.dataHora());
        validarAntecedenciaMinima(dados.dataHora());
        validarAlunoAtivo(dados.aluno().getId());
        validarLimiteInstrucoesPorDia(dados.aluno().getId(), dados.dataHora());
        
        var instrutor = dados.instrutor();
        if (instrutor == null) {
            instrutor = escolherInstrutorAleatorio(dados.dataHora());
        } else {
            validarInstrutorDisponivel(instrutor.getId(), dados.dataHora());
            validarInstrutorAtivo(instrutor.getId());
        }
        
        var instrucao = new Instrucao(new DadosAgendamentoInstrucao(dados.aluno(), instrutor, dados.dataHora()));
        return instrucaoRepository.save(instrucao);
    }

    public void cancelarInstrucao(Long instrucaoId, DadosCancelamentoInstrucao dados) {
        var instrucao = instrucaoRepository.getReferenceById(instrucaoId);
        validarAntecedenciaCancelamento(instrucao.getDataHora());
        instrucao.cancelar(dados.motivo());
    }

    private void validarHorarioFuncionamento(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime horario = dataHora.toLocalTime();
        
        if (diaSemana == DayOfWeek.SUNDAY || 
            horario.isBefore(LocalTime.of(6, 0)) || 
            horario.isAfter(LocalTime.of(21, 0))) {
            throw new IllegalArgumentException("Horário fora do funcionamento da auto-escola");
        }
    }

    private void validarAntecedenciaMinima(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Instrução deve ser agendada com antecedência mínima de 30 minutos");
        }
    }

    private void validarAlunoAtivo(Long alunoId) {
        var aluno = alunoRepository.getReferenceById(alunoId);
        if (!aluno.getAtivo()) {
            throw new IllegalArgumentException("Não é possível agendar instrução com aluno inativo");
        }
    }

    private void validarInstrutorAtivo(Long instrutorId) {
        var instrutor = instrutorRepository.getReferenceById(instrutorId);
        if (!instrutor.getAtivo()) {
            throw new IllegalArgumentException("Não é possível agendar instrução com instrutor inativo");
        }
    }

    private void validarLimiteInstrucoesPorDia(Long alunoId, LocalDateTime dataHora) {
        Long count = instrucaoRepository.countInstrucoesPorAlunoNoDia(alunoId, dataHora);
        if (count >= 2) {
            throw new IllegalArgumentException("Aluno já possui 2 instruções agendadas para este dia");
        }
    }

    private void validarInstrutorDisponivel(Long instrutorId, LocalDateTime dataHora) {
        List<Instrucao> instrucoes = instrucaoRepository.findByInstrutorAndDataHora(instrutorId, dataHora);
        if (!instrucoes.isEmpty()) {
            throw new IllegalArgumentException("Instrutor já possui instrução agendada neste horário");
        }
    }

    private br.com.fiap3esr.spring_boot_project.instrutor.Instrutor escolherInstrutorAleatorio(LocalDateTime dataHora) {
        var instrutoresAtivos = instrutorRepository.findAll().stream()
                .filter(i -> i.getAtivo())
                .filter(i -> instrucaoRepository.findByInstrutorAndDataHora(i.getId(), dataHora).isEmpty())
                .toList();
        
        if (instrutoresAtivos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum instrutor disponível neste horário");
        }
        
        Random random = new Random();
        return instrutoresAtivos.get(random.nextInt(instrutoresAtivos.size()));
    }

    private void validarAntecedenciaCancelamento(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusHours(24))) {
            throw new IllegalArgumentException("Instrução só pode ser cancelada com antecedência mínima de 24 horas");
        }
    }
}
