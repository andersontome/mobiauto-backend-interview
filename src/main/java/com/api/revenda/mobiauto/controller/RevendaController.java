package com.api.revenda.mobiauto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.revenda.mobiauto.model.Revenda;
import com.api.revenda.mobiauto.service.RevendaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/revendas")
public class RevendaController {

    @Autowired
    private RevendaService revendaService;

    @Operation(summary = "Lista todas as revendas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de revendas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public List<Revenda> listarTodas() {
        return revendaService.listarTodas();
    }

    @Operation(summary = "Busca uma revenda pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenda encontrada",
                content = @Content(schema = @Schema(implementation = Revenda.class))),
        @ApiResponse(responseCode = "404", description = "Revenda não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Revenda> buscarPorId(@PathVariable Long id) {
        Optional<Revenda> revenda = revendaService.buscarPorId(id);
        if (revenda.isPresent()) {
            return ResponseEntity.ok(revenda.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cria uma nova revenda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenda criada com sucesso",
                content = @Content(schema = @Schema(implementation = Revenda.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Revenda> criarRevenda(@Valid @RequestBody Revenda revenda) {
        Revenda novaRevenda = revendaService.salvar(revenda);
        return ResponseEntity.ok(novaRevenda);
    }

    @Operation(summary = "Exclui uma revenda pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Revenda excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Revenda não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRevenda(@PathVariable Long id) {
        Optional<Revenda> revenda = revendaService.buscarPorId(id);
        if (revenda.isPresent()) {
            revendaService.excluir(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}