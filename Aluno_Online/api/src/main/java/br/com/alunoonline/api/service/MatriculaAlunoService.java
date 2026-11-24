package br.com.alunoonline.api.service;

import br.com.alunoonline.api.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.dtos.DisciplinasAlunoResponseDTO;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponseDTO;
import br.com.alunoonline.api.dtos.atualizarNotaRequestDTO;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaAlunoService {

    private static final double MEDIA_PARA_APROVAR = 7.0;

    private final MatriculaAlunoRepository matriculaAlunoRepository;
    private final AlunoRepository alunoRepository;

    public HistoricoAlunoResponseDTO emitirHistoricoAluno(long alunoId, Usuario usuario) {
        Aluno aluno = alunoRepository.findByIdAndUsuarioId(alunoId, usuario.getId())
                .orElseThrow(this::alunoNaoEncontrado);

        List<MatriculaAluno> matriculas = matriculaAlunoRepository.findByAlunoId(aluno.getId());
        List<DisciplinasAlunoResponseDTO> disciplinas = matriculas.stream()
                .map(matricula -> new DisciplinasAlunoResponseDTO(
                        matricula.getDisciplina().getNome(),
                        matricula.getDisciplina().getProfessor() != null
                                ? matricula.getDisciplina().getProfessor().getNomeCompleto()
                                : null,
                        matricula.getNota1(),
                        matricula.getNota2(),
                        calcularMedia(matricula),
                        matricula.getStatus()))
                .toList();

        return new HistoricoAlunoResponseDTO(
                aluno.getNomeCompleto(), aluno.getCpf(), aluno.getEmail(), disciplinas);
    }

    public void atualizarNotas(Long id, atualizarNotaRequestDTO dto, Usuario usuario) {
        MatriculaAluno matricula = buscarMatriculaDoUsuario(id, usuario);

        if (dto.getNota1() != null) {
            matricula.setNota1(dto.getNota1());
        }
        if (dto.getNota2() != null) {
            matricula.setNota2(dto.getNota2());
        }

        if (matricula.getNota1() != null && matricula.getNota2() != null) {
            double media = (matricula.getNota1() + matricula.getNota2()) / 2;
            matricula.setStatus(media >= MEDIA_PARA_APROVAR
                    ? MatriculaAlunoStatusEnum.APROVADO
                    : MatriculaAlunoStatusEnum.REPROVADO);
        }
        matriculaAlunoRepository.save(matricula);
    }

    public void criarMatricula(MatriculaAluno matriculaAluno, Usuario usuario) {
        Aluno aluno = alunoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(this::alunoNaoEncontrado);
        matriculaAluno.setId(null);
        matriculaAluno.setAluno(aluno);
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        matriculaAlunoRepository.save(matriculaAluno);
    }

    public void trancarMatricula(Long id, Usuario usuario) {
        MatriculaAluno matricula = buscarMatriculaDoUsuario(id, usuario);
        if (matricula.getStatus() == MatriculaAlunoStatusEnum.MATRICULADO) {
            matricula.setStatus(MatriculaAlunoStatusEnum.TRANCADO);
            matriculaAlunoRepository.save(matricula);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Só é possível trancar uma matrícula com status MATRICULADO");
        }
    }

    private MatriculaAluno buscarMatriculaDoUsuario(Long id, Usuario usuario) {
        return matriculaAlunoRepository.findByIdAndAluno_Usuario_Id(id, usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Matrícula não encontrada para esta conta"));
    }

    private ResponseStatusException alunoNaoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Esta conta ainda não possui um cadastro de aluno vinculado");
    }

    private Double calcularMedia(MatriculaAluno matricula) {
        if (matricula.getNota1() != null && matricula.getNota2() != null) {
            return (matricula.getNota1() + matricula.getNota2()) / 2;
        }
        return null;
    }
}
