package br.com.alunoonline.api.service;

import br.com.alunoonline.api.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaAlunoServiceTest {

    @Mock
    private MatriculaAlunoRepository matriculaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private MatriculaAlunoService service;

    @Test
    void enrollmentUsesAuthenticatedUsersStudentInsteadOfRequestStudent() {
        Usuario user = new Usuario(10L, "ana", "hash");
        Aluno ownStudent = new Aluno();
        ownStudent.setId(100L);
        Aluno forgedStudent = new Aluno();
        forgedStudent.setId(200L);
        MatriculaAluno request = new MatriculaAluno();
        request.setId(777L);
        request.setAluno(forgedStudent);
        when(alunoRepository.findByUsuarioId(10L)).thenReturn(Optional.of(ownStudent));

        service.criarMatricula(request, user);

        ArgumentCaptor<MatriculaAluno> saved = ArgumentCaptor.forClass(MatriculaAluno.class);
        verify(matriculaRepository).save(saved.capture());
        assertNull(saved.getValue().getId());
        assertEquals(100L, saved.getValue().getAluno().getId());
        assertEquals(MatriculaAlunoStatusEnum.MATRICULADO, saved.getValue().getStatus());
    }

    @Test
    void cannotChangeEnrollmentOwnedByAnotherAccount() {
        when(matriculaRepository.findByIdAndAluno_Usuario_Id(5L, 10L))
                .thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.trancarMatricula(5L, new Usuario(10L, "ana", "hash")));

        assertEquals(404, error.getStatusCode().value());
        verify(matriculaRepository).findByIdAndAluno_Usuario_Id(5L, 10L);
        verify(matriculaRepository, never()).save(any());
    }
}
