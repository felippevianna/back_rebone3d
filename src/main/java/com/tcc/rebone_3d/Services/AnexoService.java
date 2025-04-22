package com.tcc.rebone_3d.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcc.rebone_3d.Models.Anexo;
import com.tcc.rebone_3d.Models.Solicitacao;
import com.tcc.rebone_3d.Repositories.AnexoRepository;

@Service
public class AnexoService {

    private final AnexoRepository anexoRepository;
    private final String uploadDir = "uploads/";

    public AnexoService(AnexoRepository anexoRepository) {
        this.anexoRepository = anexoRepository;
    }

    public Anexo salvarAnexo(MultipartFile file, Solicitacao solicitacao) throws IOException {
        if (!Files.exists(Paths.get(uploadDir))) {
            Files.createDirectories(Paths.get(uploadDir));
        }

        String filePath = uploadDir + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        Anexo anexo = new Anexo();
        anexo.setCaminhoArquivo(filePath);
        anexo.setSolicitacao(solicitacao);

        return anexoRepository.save(anexo);
    }

    public List<Anexo> listarAnexosPorSolicitacao(Solicitacao solicitacao) {
        return anexoRepository.findBySolicitacao(solicitacao);
    }

    public Optional<Anexo> buscarAnexoPorId(Long id) {
        return anexoRepository.findById(id);
    }
}
