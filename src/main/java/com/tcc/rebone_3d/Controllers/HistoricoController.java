package com.tcc.rebone_3d.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcc.rebone_3d.DTO.HistoricoDTO;
import com.tcc.rebone_3d.Models.Historico;
import com.tcc.rebone_3d.Models.Arquivo;
import com.tcc.rebone_3d.Models.Paciente;
import com.tcc.rebone_3d.Models.Usuario;
import com.tcc.rebone_3d.Repositories.HistoricoRepository;
import com.tcc.rebone_3d.Repositories.PacienteRepository;
import com.tcc.rebone_3d.Services.HistoricoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/historicos")
@Tag(name = "Históricos Médicos", description = "Endpoints para gerenciamento de históricos médicos e imagens associadas")
@SecurityRequirement(name = "bearerAuth")
public class HistoricoController {
    @Value("${app.upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoricoService historicoService;

    // Endpoint para obter todos os históricos
    @GetMapping
    @Operation(
        summary = "Listar históricos",
        description = "Retorna todos os históricos médicos de um paciente específico ou do usuário logado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Históricos encontrados"),
        @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado", content = @Content)
    })
    public ResponseEntity<List<Historico>> getAllHistoricos(
        @Parameter(description = "ID do paciente (opcional)") Long idPaciente
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<Historico> historicos = historicoService.listarTodosHistoricosDoPaciente(usuarioLogado, idPaciente);

        return new ResponseEntity<>(historicos, HttpStatus.OK);
    }

    // Endpoint para obter um histórico por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar histórico por ID",
        description = "Retorna um histórico médico específico pelo seu ID, se o usuário logado tiver permissão."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public ResponseEntity<Historico> getHistoricoById(@Parameter(description = "ID do histórico", required = true) @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        
        if(!historicoService.HistoricoPodeSerAlteradoPeloUsario(id, usuarioLogado)) {
            Optional<Historico> historico = historicoRepository.findById(id);
            if (historico.isPresent()) {
                return ResponseEntity.ok(historico.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    // Endpoint para criar um novo histórico
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(
        summary = "Criar histórico",
        description = "Cria um novo histórico médico com imagens anexadas. Requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Histórico criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro ao processar imagens")
    })
    public ResponseEntity<Historico> createHistorico(@Parameter(description = "Dados do histórico (JSON)", required = true, 
                content = @Content(schema = @Schema(implementation = HistoricoDTO.class)))
    @RequestPart("historico") HistoricoDTO historicoDto,

    @Parameter(description = "Arquivos de imagem (PNG/JPEG)", required = true,
                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @RequestPart("imagens") List<MultipartFile> arquivos) {

        // Validação dos campos obrigatórios
        if (historicoDto.idPaciente() == null || historicoDto.data() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Verifica se o paciente existe
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(historicoDto.idPaciente());
        if (!pacienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtém o profissional logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario profissional = (Usuario) authentication.getPrincipal();

        // Cria o histórico
        Historico historico = new Historico();
        historico.setPaciente(pacienteOptional.get());
        historico.setData(historicoDto.data());
        historico.setDescricao(historicoDto.descricao());

        // Salva o histórico para gerar o ID
        Historico novoHistorico = historicoRepository.save(historico);

        // Lista para armazenar as imagens
        List<Arquivo> arquivosSalvos = new ArrayList<>();

        // Processa cada imagem
        for (MultipartFile arquivo : arquivos) {
            if (!arquivo.isEmpty()) {
                // Gera o nome do arquivo: ID do histórico + timestamp
                String nomeArquivo = novoHistorico.getId() + "_" + System.currentTimeMillis() + ".png";

                // Define o caminho da pasta: uploads/{idPaciente}/
                String caminhoPasta = UPLOAD_DIR + historicoDto.idPaciente() + "/";
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

                // Cria a entidade Arquivo
                Arquivo novoArquivo = new Arquivo();
                novoArquivo.setCaminhoArquivo(caminhoArquivo);
                novoArquivo.setDataUpload(new Date());
                novoArquivo.setHistorico(novoHistorico);
                novoArquivo.setProfissional(profissional);
                novoArquivo.setPaciente(pacienteOptional.get());

                // Adiciona à lista de arquivos
                arquivosSalvos.add(novoArquivo);
            }
        }

        // Vincula as imagens ao histórico
        novoHistorico.setArquivos(arquivosSalvos);

        // Salva o histórico atualizado
        historicoRepository.save(novoHistorico);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoHistorico);
    }


    // Endpoint para atualizar um histórico existente
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar histórico",
        description = "Atualiza os dados de um histórico médico existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico atualizado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Histórico ou paciente não encontrado")
    })
    public ResponseEntity<Historico> updateHistorico(@Parameter(description = "ID do histórico", required = true) @PathVariable Long id,
    @Parameter(description = "Dados atualizados do histórico", required = true,
                content = @Content(schema = @Schema(implementation = Historico.class)))
    @RequestBody Historico historicoAtualizado) {

        Optional<Historico> historicoOptional = historicoRepository.findById(id);

        if (!historicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Historico historicoExistente = historicoOptional.get();
        historicoExistente.setDescricao(historicoAtualizado.getDescricao());
        historicoExistente.setData(historicoAtualizado.getData());

        // Se o paciente for alterado, verifique se o novo paciente existe
        if (historicoAtualizado.getPaciente() != null && historicoAtualizado.getPaciente().getId() != null) {
            Optional<Paciente> pacienteOptional = pacienteRepository.findById(historicoAtualizado.getPaciente().getId());
            if (pacienteOptional.isPresent()) {
                historicoExistente.setPaciente(pacienteOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

        historicoRepository.save(historicoExistente);
        return ResponseEntity.ok(historicoExistente);
    }

    // Endpoint para deletar um histórico
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir histórico",
        description = "Remove um histórico médico do sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Histórico excluído"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public ResponseEntity<Void> deleteHistorico(@Parameter(description = "ID do histórico", required = true) @PathVariable Long id)  {
        if (historicoRepository.existsById(id)) {
            historicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
