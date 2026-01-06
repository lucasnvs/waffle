package com.lucasnvs.waffle.raffle;

import com.lucasnvs.waffle.raffle.dto.CreateRaffleRequest;
import com.lucasnvs.waffle.raffle.dto.RaffleResponse;
import com.lucasnvs.waffle.raffle.dto.UpdateRaffleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/raffles")
@Tag(name = "Raffles", description = "API para gerenciamento de rifas")
public class RaffleController {

    private final RaffleService raffleService;

    public RaffleController(RaffleService raffleService) {
        this.raffleService = raffleService;
    }

    @PostMapping
    @Operation(summary = "Criar nova rifa", description = "Cria uma nova rifa com as informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rifa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RaffleResponse> create(@Valid @RequestBody CreateRaffleRequest request) {
        RaffleResponse createdRaffle = raffleService.createRaffle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRaffle);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rifa por ID", description = "Retorna os detalhes de uma rifa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rifa encontrada"),
            @ApiResponse(responseCode = "404", description = "Rifa não encontrada")
    })
    public ResponseEntity<RaffleResponse> getRaffleById(
            @Parameter(description = "ID da rifa") @PathVariable Long id) {
        RaffleResponse raffle = raffleService.getRaffleById(id);
        return ResponseEntity.ok(raffle);
    }

    @GetMapping
    @Operation(summary = "Listar todas as rifas", description = "Retorna uma lista com todas as rifas cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de rifas retornada com sucesso")
    public ResponseEntity<List<RaffleResponse>> getAllRaffles() {
        List<RaffleResponse> raffles = raffleService.getAllRaffles();
        return ResponseEntity.ok(raffles);
    }

    @GetMapping("/me")
    @Operation(summary = "Listar minhas rifas", description = "Retorna todas as rifas criadas pelo usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de rifas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<List<RaffleResponse>> getMyRaffles() {
        List<RaffleResponse> raffles = raffleService.getMyRaffles();
        return ResponseEntity.ok(raffles);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Listar rifas de um usuário", description = "Retorna todas as rifas criadas por um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de rifas retornada com sucesso")
    })
    public ResponseEntity<List<RaffleResponse>> getRafflesByUserId(
            @Parameter(description = "ID do usuário (Firebase UID)") @PathVariable String userId) {
        List<RaffleResponse> raffles = raffleService.getRafflesByUserId(userId);
        return ResponseEntity.ok(raffles);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar rifa", description = "Atualiza as informações de uma rifa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rifa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Rifa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RaffleResponse> updateRaffle(
            @Parameter(description = "ID da rifa") @PathVariable Long id,
            @Valid @RequestBody UpdateRaffleRequest request) {
        RaffleResponse updatedRaffle = raffleService.updateRaffle(id, request);
        return ResponseEntity.ok(updatedRaffle);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar rifa", description = "Remove uma rifa do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rifa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Rifa não encontrada")
    })
    public ResponseEntity<Void> deleteRaffle(
            @Parameter(description = "ID da rifa") @PathVariable Long id) {
        raffleService.deleteRaffle(id);
        return ResponseEntity.noContent().build();
    }
}
