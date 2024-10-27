package com.tcc.rebone_3d.DTO;

import java.util.Date;

import com.tcc.rebone_3d.Models.Paciente;

public record PacienteDTO (String nome, String email, String cpf, String telefone, Date dataNascimento,
                    String uf, String cidade, String cep, String rua, String numero, String bairro){
    
    public Paciente toPacienteModel(){
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
