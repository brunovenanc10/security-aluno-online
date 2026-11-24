package br.com.alunoonline.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponseDTO;
import br.com.alunoonline.api.dtos.atualizarNotaRequestDTO;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.service.MatriculaAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/matriculas")
public class MatriculaAlunoController {

    @Autowired
    MatriculaAlunoService matriculaAlunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarMatricula(@RequestBody MatriculaAluno matriculaAluno,
                               @AuthenticationPrincipal Usuario usuario){
        matriculaAlunoService.criarMatricula(matriculaAluno, usuario);
    }

    @PatchMapping("/trancar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void trancarMatricula(@PathVariable long id,
                                 @AuthenticationPrincipal Usuario usuario){
        matriculaAlunoService.trancarMatricula(id, usuario);
    }

    @PatchMapping("/atualizar-notas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarNotas(@PathVariable long id, @RequestBody atualizarNotaRequestDTO dto,
                               @AuthenticationPrincipal Usuario usuario){
        matriculaAlunoService.atualizarNotas(id, dto, usuario);
    }

    @GetMapping("/historico/{alunoId}")
    @ResponseStatus(HttpStatus.OK)
    public HistoricoAlunoResponseDTO emitirHistoricoAluno(@PathVariable long alunoId,
                                                           @AuthenticationPrincipal Usuario usuario){
        return matriculaAlunoService.emitirHistoricoAluno(alunoId, usuario);
    }
}
