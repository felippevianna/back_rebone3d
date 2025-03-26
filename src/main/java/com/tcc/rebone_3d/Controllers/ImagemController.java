package com.tcc.rebone_3d.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcc.rebone_3d.Models.Imagem;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.ImagemRepository;
import com.tcc.rebone_3d.Repositories.PacienteRepository;
import com.tcc.rebone_3d.Repositories.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

// Alterar o nome desse controller, para 'Arquivos'

@RestController
@RequestMapping("/api/imagens")
@Tag(name = "Gerenciamento de Arquivos", description = "Endpoints para upload e consulta de arquivos médicos (imagens, documentos)")
@SecurityRequirement(name = "bearerAuth")
public class ImagemController {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String UPLOAD_DIR = "uploads/";

    // Endpoint para upload de arquivo
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload de arquivo",
        description = "Realiza o upload de um arquivo vinculado a um paciente",
        requestBody = @RequestBody(
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                schema = @Schema(type = "object"),
                encoding = @Encoding(
                    name = "arquivo",
                    contentType = "application/octet-stream")
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Arquivo salvo com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<Imagem> uploadImagem(@Parameter(description = "Arquivo binário", required = true) @RequestPart("arquivo") MultipartFile arquivo,        
        @Parameter(description = "ID do paciente", required = true) @RequestParam("idPaciente") Long idPaciente) {

        // Verifica se o paciente existe
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(idPaciente);
        if (!pacienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtém o profissional logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario profissional = (Usuario) authentication.getPrincipal();

        // Gera o nome do arquivo: ID do paciente + timestamp
        String nomeArquivo = idPaciente + "_" + System.currentTimeMillis() + ".png";

        // Define o caminho da pasta: uploads/{idPaciente}/
        String caminhoPasta = UPLOAD_DIR + idPaciente + "/";
        String caminhoArquivo = caminhoPasta + nomeArquivo;

        // Salva o arquivo no diretório
        try {
            Path path = Paths.get(caminhoArquivo);
            Files.createDirectories(path.getParent()); // Cria a pasta se não existir
            Files.write(path, arquivo.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Cria a entidade Imagem
        Imagem imagem = new Imagem();
        imagem.setCaminhoArquivo(caminhoArquivo);
        imagem.setDataUpload(new Date());
        imagem.setProfissional(profissional);
        imagem.setPaciente(pacienteOptional.get());
        // Aqui você precisará buscar o agendamento pelo ID e vinculá-lo à imagem
        // imagem.setAgendamento(agendamento);

        // Salva a imagem no banco de dados
        Imagem novaImagem = imagemRepository.save(imagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaImagem);
    }

    // Endpoint para listar arquivos de um paciente ordenadas por data
    @GetMapping("/paciente/{idPaciente}")
    @Operation(
        summary = "Listar arquivos por paciente",
        description = "Retorna todos os arquivos de um paciente ordenados por data (mais recente primeiro)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de arquivos encontrada",
                     content = @Content(schema = @Schema(implementation = Imagem.class))),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<List<Imagem>> listarImagensPorPaciente( @Parameter(description = "ID do paciente", required = true, example = "1") @PathVariable Long idPaciente)  {
        List<Imagem> imagens = imagemRepository.findByPacienteIdOrderByDataUploadDesc(idPaciente);
        return ResponseEntity.ok(imagens);
    }
}