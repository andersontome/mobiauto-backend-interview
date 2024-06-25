package com.api.revenda.mobiauto.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.revenda.mobiauto.model.Oportunidade;
import com.api.revenda.mobiauto.model.Oportunidade.StatusOportunidade;
import com.api.revenda.mobiauto.model.Usuario;
import com.api.revenda.mobiauto.repository.OportunidadeRepository;
import com.api.revenda.mobiauto.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Oportunidade> listarTodas() {
        return oportunidadeRepository.findAll();
    }

    public Optional<Oportunidade> buscarPorId(Long id) {
        return oportunidadeRepository.findById(id);
    }

    @Transactional
    public Oportunidade salvar(Oportunidade oportunidade) {
        Usuario currentUser = getCurrentUser();
        
        if (oportunidade.getId() == null) {
            oportunidade.setCodigo(UUID.randomUUID().toString());
            oportunidade.setStatus(StatusOportunidade.NOVO);
            oportunidade.setDataAtribuicao(LocalDateTime.now());
            oportunidade.setResponsavel(distribuirOportunidade(currentUser.getLoja().getId()));
        } else {
            if (!isAdminOrOwner(currentUser) && !isResponsibleUser(currentUser, oportunidade)) {
                throw new AccessDeniedException("User is not authorized to edit this opportunity.");
            }
            if (oportunidade.getStatus() == StatusOportunidade.CONCLUIDO) {
                if (oportunidade.getMotivoConclusao() == null || oportunidade.getMotivoConclusao().isBlank()) {
                    throw new IllegalArgumentException("Motivo de conclusão é obrigatório quando o status é CONCLUIDO");
                }
                oportunidade.setDataConclusao(LocalDateTime.now());
            }
        }

        return oportunidadeRepository.save(oportunidade);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario currentUser = getCurrentUser();
        Oportunidade oportunidade = oportunidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Oportunidade not found"));
        if (!isAdminOrOwner(currentUser) && !isResponsibleUser(currentUser, oportunidade)) {
            throw new AccessDeniedException("User is not authorized to delete this opportunity.");
        }
        oportunidadeRepository.deleteById(id);
    }

    public void transferirOportunidade(Long oportunidadeId, Long novoResponsavelId) {
        Usuario currentUser = getCurrentUser();
        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId).orElseThrow(() -> new RuntimeException("Oportunidade not found"));
        Usuario novoResponsavel = usuarioRepository.findById(novoResponsavelId).orElseThrow(() -> new RuntimeException("Usuario not found"));

        if (!isAdminOrOwner(currentUser) && !isOwnerOfLoja(currentUser, oportunidade)) {
            throw new AccessDeniedException("User is not authorized to transfer this opportunity.");
        }

        oportunidade.setResponsavel(novoResponsavel);
        oportunidadeRepository.save(oportunidade);
    }

    private Usuario distribuirOportunidade(Long lojaId) {
        List<Usuario> assistentes = new ArrayList<>();

        return assistentes.stream()
                .min(Comparator.comparingLong(this::getOportunidadesEmAndamento)
                        .thenComparing(Comparator.comparing(this::getUltimaOportunidadeRecebida)))
                .orElseThrow(() -> new RuntimeException("No available assistants found"));
    }

    private long getOportunidadesEmAndamento(Usuario usuario) {
        return oportunidadeRepository.countByResponsavelAndStatus(usuario, StatusOportunidade.EM_ATENDIMENTO);
    }

    private LocalDateTime getUltimaOportunidadeRecebida(Usuario usuario) {
        return oportunidadeRepository.findTopByResponsavelOrderByDataAtribuicaoDesc(usuario)
                .map(Oportunidade::getDataAtribuicao)
                .orElse(LocalDateTime.MIN);
    }

    private Usuario getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdminOrOwner(Usuario user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getNome().equals("ADMIN") || role.getNome().equals("PROPRIETARIO"));
    }

    private boolean isOwnerOfLoja(Usuario user, Oportunidade oportunidade) {
        return user.getId().equals(oportunidade.getResponsavel().getId()) &&
                user.getRoles().stream().anyMatch(role -> role.getNome().equals("PROPRIETARIO"));
    }

    private boolean isResponsibleUser(Usuario user, Oportunidade oportunidade) {
        return user.equals(oportunidade.getResponsavel());
    }
}