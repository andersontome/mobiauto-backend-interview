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

import com.api.revenda.mobiauto.model.Oportunidade;
import com.api.revenda.mobiauto.service.OportunidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/oportunidades")
@Validated
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Operation(summary = "Lista todas as oportunidades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de oportunidades retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public List<Oportunidade> listarTodas() {
        return oportunidadeService.listarTodas();
    }

    @Operation(summary = "Busca uma oportunidade pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oportunidade encontrada",
                content = @Content(schema = @Schema(implementation = Oportunidade.class))),
        @ApiResponse(responseCode = "404", description = "Oportunidade não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> buscarPorId(@PathVariable Long id) {
        Optional<Oportunidade> oportunidade = oportunidadeService.buscarPorId(id);
        if (oportunidade.isPresent()) {
            return ResponseEntity.ok(oportunidade.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cria uma nova oportunidade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oportunidade criada com sucesso",
                content = @Content(schema = @Schema(implementation = Oportunidade.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Oportunidade> criarOportunidade(@Valid @RequestBody Oportunidade oportunidade) {
        Oportunidade novaOportunidade = oportunidadeService.salvar(oportunidade);
        return ResponseEntity.ok(novaOportunidade);
    }

    @Operation(summary = "Exclui uma oportunidade pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Oportunidade excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Oportunidade não encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirOportunidade(@PathVariable Long id) {
        Optional<Oportunidade> oportunidade = oportunidadeService.buscarPorId(id);
        if (oportunidade.isPresent()) {
            oportunidadeService.excluir(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
