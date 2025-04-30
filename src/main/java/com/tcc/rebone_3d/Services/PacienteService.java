package com.tcc.rebone_3d.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Paciente editar(Long id, PacienteDTO pacienteAtualizado) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(id);
        String teste = pacienteAtualizado.nome();
        
        if (pacienteExistente.isPresent()){
            Paciente paciente = pacienteExistente.get();
            paciente.setNome(pacienteAtualizado.nome());
            paciente.setEmail(pacienteAtualizado.email());
            paciente.setCpf(pacienteAtualizado.cpf());
            paciente.setTelefone(pacienteAtualizado.telefone());
            paciente.setDataNascimento(pacienteAtualizado.dataNascimento());
            paciente.setUf(pacienteAtualizado.uf());
            paciente.setCidade(pacienteAtualizado.cidade());
            paciente.setCep(pacienteAtualizado.cep());
            paciente.setRua(pacienteAtualizado.rua());
            paciente.setNumero(pacienteAtualizado.numero());
            paciente.setBairro(pacienteAtualizado.bairro());

            pacienteRepository.save(paciente);

            return paciente;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        
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
        .map(p -> new PacienteBuscaDTO(p.getId(), p.getNome(), p.getEmail(), p.getTelefone()))
        .toList();
        
        return listaDTOPacientes;
    }
    
}

