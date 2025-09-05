package br.com.fiap3esr.spring_boot_project.controller;

import br.com.fiap3esr.spring_boot_project.instrucao.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
public class InstrucaoController {

    @Autowired
    private InstrucaoService instrucaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> agendarInstrucao(@RequestBody @Valid DadosAgendamentoInstrucao dados) {
        try {
            var instrucao = instrucaoService.agendarInstrucao(dados);
            return ResponseEntity.ok("Instrução agendada com sucesso. ID: " + instrucao.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    @Transactional
    public ResponseEntity<String> cancelarInstrucao(@PathVariable Long id, 
                                                   @RequestBody @Valid DadosCancelamentoInstrucao dados) {
        try {
            instrucaoService.cancelarInstrucao(id, dados);
            return ResponseEntity.ok("Instrução cancelada com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
