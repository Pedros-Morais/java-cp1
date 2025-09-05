package br.com.fiap3esr.spring_boot_project.instrucao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    
    @Query("SELECT COUNT(i) FROM Instrucao i WHERE i.aluno.id = :alunoId AND DATE(i.dataHora) = DATE(:dataHora) AND i.ativo = true")
    Long countInstrucoesPorAlunoNoDia(@Param("alunoId") Long alunoId, @Param("dataHora") LocalDateTime dataHora);
    
    @Query("SELECT i FROM Instrucao i WHERE i.instrutor.id = :instrutorId AND i.dataHora = :dataHora AND i.ativo = true")
    List<Instrucao> findByInstrutorAndDataHora(@Param("instrutorId") Long instrutorId, @Param("dataHora") LocalDateTime dataHora);
    
}
