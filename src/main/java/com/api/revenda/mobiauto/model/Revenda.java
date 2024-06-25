package com.api.revenda.mobiauto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "revendas", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cnpj"),
        @UniqueConstraint(columnNames = "codigo")
})
public class Revenda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Code is mandatory")
	private String codigo;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "CNPJ is mandatory")
	@Pattern(regexp = "\\d{14}", message = "CNPJ must contain 14 numeric digits")
	private String cnpj;

	@Column(nullable = false)
	@NotBlank(message = "Corporate name is mandatory")
	private String nomeSocial;


}
