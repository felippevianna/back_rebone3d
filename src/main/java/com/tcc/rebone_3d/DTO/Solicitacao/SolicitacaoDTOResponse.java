package com.tcc.rebone_3d.DTO.Solicitacao;

import com.tcc.rebone_3d.Models.Solicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;


public record SolicitacaoDTOResponse(
    Long id,
    String titulo,
    String descricao,
    Date dataCriacao,
    String remetenteNome,
    Long idRemetente,
    String destinatarioNome,
    Long idDestinatario
) {
    public static SolicitacaoDTOResponse fromEntity(Solicitacao s) {
        return new SolicitacaoDTOResponse(
            s.getId(),
            s.getTitulo(),
            s.getDescricao(),
            s.getDataCriacao(),
            s.getRemetente() != null ? s.getRemetente().getUsername() : null,
            s.getRemetente() != null ? s.getRemetente().getId() :  null,
            s.getDestinatario() != null ? s.getDestinatario().getUsername() : null,
            s.getDestinatario() != null ? s.getDestinatario().getId() :  null
        );
    }
}
