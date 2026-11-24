package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
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
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void newStudentIsLinkedToAuthenticatedAccount() {
        Usuario authenticatedUser = new Usuario(10L, "ana", "hash");
        Aluno request = new Aluno();
        request.setId(999L);
        request.setNomeCompleto("Ana");
        when(alunoRepository.findByUsuarioId(10L)).thenReturn(Optional.empty());

        alunoService.criarAluno(request, authenticatedUser);

        ArgumentCaptor<Aluno> saved = ArgumentCaptor.forClass(Aluno.class);
        verify(alunoRepository).save(saved.capture());
        assertNull(saved.getValue().getId());
        assertSame(authenticatedUser, saved.getValue().getUsuario());
    }

    @Test
    void cannotFetchStudentOwnedByAnotherAccount() {
        when(alunoRepository.findByIdAndUsuarioId(20L, 10L)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> alunoService.buscarAlunoDoUsuario(20L, new Usuario(10L, "ana", "hash")));

        assertEquals(404, error.getStatusCode().value());
        verify(alunoRepository).findByIdAndUsuarioId(20L, 10L);
    }
}
