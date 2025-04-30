package com.tcc.rebone_3d.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tcc.rebone_3d.DTO.Paciente.PacienteDTO;
import com.tcc.rebone_3d.DTO.Paciente.PacienteBuscaDTO;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.PacienteRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // Listar todos os pacientes
    public List<Paciente> listarTodos(Usuario usuario) {
        return pacienteRepository.findByUsuarioResponsavel(usuario);

    }

    // Buscar paciente por ID
    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    // Cadastrar um novo paciente
    public Paciente cadastrar(PacienteDTO pacienteDTO) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        Paciente paciente = pacienteDTO.toPacienteModel();
        paciente.setUsuarioResponsavel(usuarioLogado);

        return pacienteRepository.save(paciente);
    }

    // Editar dados do paciente existente
    public Paciente editar(Long id, Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNome(pacienteAtualizado.getNome());
            paciente.setEmail(pacienteAtualizado.getEmail());
            paciente.setCpf(pacienteAtualizado.getCpf());
            paciente.setTelefone(pacienteAtualizado.getTelefone());
            paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
            paciente.setUf(pacienteAtualizado.getUf());
            paciente.setCidade(pacienteAtualizado.getCidade());
            paciente.setCep(pacienteAtualizado.getCep());
            paciente.setRua(pacienteAtualizado.getRua());
            paciente.setNumero(pacienteAtualizado.getNumero());
            paciente.setBairro(pacienteAtualizado.getBairro());
            return pacienteRepository.save(paciente);
        }).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    // Deletar paciente
    public void deletar(Long id) {
        pacienteRepository.deleteById(id);
    }

    public boolean PacientePodeSerAlteradoPeloUsario (Long id, Usuario usuarioLogado){
        Paciente paciente = pacienteRepository.findByIdAndUsuarioResponsavel(id, usuarioLogado);
        if (paciente == null) {
            return false; // Não encontrado ou sem permissão
        } {
            return true;
        }
    }
    
    public List<PacienteBuscaDTO> buscarPorNomeEUsuario(String nome, Usuario usuario) {
        List<Paciente> listaPacientes = pacienteRepository.findByNomeContainingIgnoreCaseAndUsuarioResponsavel(nome, usuario);

        List<PacienteBuscaDTO> listaDTOPacientes = listaPacientes.stream()
        .map(p -> new PacienteBuscaDTO(p.getId(), p.getNome()))
        .toList();
        
        return listaDTOPacientes;
    }
    
}

