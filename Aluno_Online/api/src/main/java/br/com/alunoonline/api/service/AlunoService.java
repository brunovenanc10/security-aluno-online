package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public void criarAluno(Aluno aluno, Usuario usuarioAutenticado) {
        if (alunoRepository.findByUsuarioId(usuarioAutenticado.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta conta já possui um cadastro de aluno");
        }
        aluno.setId(null);
        aluno.setUsuario(usuarioAutenticado);
        alunoRepository.save(aluno);
    }

    public List<Aluno> listarAlunosDoUsuario(Usuario usuarioAutenticado) {
        return alunoRepository.findByUsuarioId(usuarioAutenticado.getId())
                .map(List::of)
                .orElseGet(List::of);
    }

    public Aluno buscarAlunoDoUsuario(Long id, Usuario usuarioAutenticado) {
        return alunoRepository.findByIdAndUsuarioId(id, usuarioAutenticado.getId())
                .orElseThrow(() -> alunoNaoEncontrado());
    }

    public void deletarAlunoDoUsuario(Long id, Usuario usuarioAutenticado) {
        Aluno aluno = buscarAlunoDoUsuario(id, usuarioAutenticado);
        alunoRepository.delete(aluno);
    }

    public void atualizarAlunoDoUsuario(Long id, Aluno dados, Usuario usuarioAutenticado) {
        Aluno aluno = buscarAlunoDoUsuario(id, usuarioAutenticado);
        aluno.setNomeCompleto(dados.getNomeCompleto());
        aluno.setCpf(dados.getCpf());
        aluno.setEmail(dados.getEmail());
        alunoRepository.save(aluno);
    }

    private ResponseStatusException alunoNaoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Aluno não encontrado para esta conta");
    }
}
