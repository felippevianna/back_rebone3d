package com.tcc.rebone_3d.DTO;

import java.util.Date;

import com.tcc.rebone_3d.Models.Paciente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "PacienteDTO",
    description = "DTO para cadastro e atualização de pacientes"
)
public record PacienteDTO(
    @Schema(
        description = "Nome completo do paciente",
        example = "João da Silva",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 100
    )
    String nome,

    @Schema(
        description = "E-mail do paciente",
        example = "joao.silva@exemplo.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "email"
    )
    String email,

    @Schema(
        description = "CPF do paciente (apenas números)",
        example = "12345678901",
        requiredMode = Schema.RequiredMode.REQUIRED,
        pattern = "^\\d{11}$",
        minLength = 11,
        maxLength = 11
    )
    String cpf,

    @Schema(
        description = "Telefone para contato",
        example = "11987654321",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String telefone,

    @Schema(
        description = "Data de nascimento (formato: yyyy-MM-dd)",
        example = "1985-05-15",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",
        format = "date"
    )
    Date dataNascimento,

    @Schema(
        description = "Unidade Federativa (UF)",
        example = "SP",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 2,
        maxLength = 2
    )
    String uf,

    @Schema(
        description = "Cidade de residência",
        example = "São Paulo",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    String cidade,

    @Schema(
        description = "CEP (apenas números)",
        example = "12345678",
        requiredMode = Schema.RequiredMode.REQUIRED,
        pattern = "^\\d{8}$"
    )
    String cep,

    @Schema(
        description = "Logradouro",
        example = "Avenida Paulista",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 200
    )
    String rua,

    @Schema(
        description = "Número do endereço",
        example = "1000",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 10
    )
    String numero,

    @Schema(
        description = "Bairro",
        example = "Bela Vista",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    String bairro) {

    public Paciente toPacienteModel() {
        Paciente pacienteNovo = new Paciente();
        pacienteNovo.setNome(this.nome);
        pacienteNovo.setEmail(this.email);
        pacienteNovo.setCpf(this.cpf);
        pacienteNovo.setTelefone(this.telefone);
        pacienteNovo.setDataNascimento(this.dataNascimento); 
        pacienteNovo.setUf(this.uf);
        pacienteNovo.setCidade(this.cidade);
        pacienteNovo.setCep(this.cep);
        pacienteNovo.setRua(this.rua);
        pacienteNovo.setNumero(this.numero);
        pacienteNovo.setBairro(this.bairro);

        return pacienteNovo;
    }
}