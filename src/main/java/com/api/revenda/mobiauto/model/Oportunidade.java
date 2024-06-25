package com.api.revenda.mobiauto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "oportunidades")
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Code is mandatory")
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "Customer name is mandatory")
    private String nomeCliente;

    @Column(nullable = false)
    @NotBlank(message = "Customer email is mandatory")
    private String emailCliente;

    @Column(nullable = false)
    @NotBlank(message = "Customer phone is mandatory")
    private String telefoneCliente;

    @Column(nullable = false)
    @NotBlank(message = "Vehicle brand is mandatory")
    private String marcaVeiculo;

    @Column(nullable = false)
    @NotBlank(message = "Vehicle model is mandatory")
    private String modeloVeiculo;

    @Column(nullable = false)
    @NotBlank(message = "Vehicle version is mandatory")
    private String versaoVeiculo;

    @Column(nullable = false)
    @NotBlank(message = "Vehicle model year is mandatory")
    private String anoModeloVeiculo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is mandatory")
    private StatusOportunidade status;

    @Column(nullable = true)
    private String motivoConclusao;

    @Column(nullable = true)
    private LocalDateTime dataAtribuicao;

    @Column(nullable = true)
    private LocalDateTime dataConclusao;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    public enum StatusOportunidade {
        NOVO, EM_ATENDIMENTO, CONCLUIDO
    }
}