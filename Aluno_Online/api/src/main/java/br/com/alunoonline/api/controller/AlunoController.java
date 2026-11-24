package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.service.AlunoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@RequestBody Aluno aluno,
                           @AuthenticationPrincipal Usuario usuario) {
        alunoService.criarAluno(aluno, usuario);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Aluno> listarAlunos(@AuthenticationPrincipal Usuario usuario) {
        return alunoService.listarAlunosDoUsuario(usuario);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Aluno buscarAluno(@PathVariable Long id,
                             @AuthenticationPrincipal Usuario usuario) {
        return alunoService.buscarAlunoDoUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarAluno(@PathVariable Long id,
                             @AuthenticationPrincipal Usuario usuario) {
        alunoService.deletarAlunoDoUsuario(id, usuario);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarAluno(@PathVariable Long id, @RequestBody Aluno dados,
                               @AuthenticationPrincipal Usuario usuario) {
        alunoService.atualizarAlunoDoUsuario(id, dados, usuario);
    }
}
