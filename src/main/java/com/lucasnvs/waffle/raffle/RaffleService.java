package com.lucasnvs.waffle.raffle;

import com.lucasnvs.waffle.common.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.raffle.dto.CreateRaffleRequest;
import com.lucasnvs.waffle.raffle.dto.RaffleResponse;
import com.lucasnvs.waffle.raffle.dto.UpdateRaffleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaffleService {

    private final RaffleRepository raffleRepository;

    public RaffleService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    @Transactional
    public RaffleResponse createRaffle(CreateRaffleRequest request) {
        RaffleEntity raffle = new RaffleEntity();
        raffle.setTitle(request.title());
        raffle.setTotalTickets(request.totalTickets());
        raffle.setTicketPrice(request.ticketPrice());
        raffle.setDescription(request.description());
        raffle.setHasDrawDate(request.hasDrawDate());
        raffle.setDrawDate(request.drawDate());
        raffle.setDrawTime(request.drawTime());
        raffle.setCoverImage(request.coverImage());
        raffle.setContactPhoneNumber(request.contactPhoneNumber());
        raffle.setPublic(request.isPublic());
        raffle.setShowWinnerPublicly(request.showWinnerPublicly());
        raffle.setPaymentMethods(request.paymentMethods());
        raffle.setDrawMethod(request.drawMethod());
        raffle.setStatus(RaffleStatus.OPEN);

        RaffleEntity saved = raffleRepository.save(raffle);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public RaffleResponse getRaffleById(Long id) {
        RaffleEntity raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new RaffleNotFoundException(id));
        return toResponse(raffle);
    }

    @Transactional(readOnly = true)
    public List<RaffleResponse> getAllRaffles() {
        return raffleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RaffleResponse updateRaffle(Long id, UpdateRaffleRequest request) {
        RaffleEntity raffle = raffleRepository.findById(id)
                .orElseThrow(() -> new RaffleNotFoundException(id));

        if (request.title() != null) {
            raffle.setTitle(request.title());
        }
        if (request.totalTickets() != null) {
            raffle.setTotalTickets(request.totalTickets());
        }
        if (request.ticketPrice() != null) {
            raffle.setTicketPrice(request.ticketPrice());
        }
        if (request.description() != null) {
            raffle.setDescription(request.description());
        }
        if (request.hasDrawDate() != null) {
            raffle.setHasDrawDate(request.hasDrawDate());
        }
        if (request.drawDate() != null) {
            raffle.setDrawDate(request.drawDate());
        }
        if (request.drawTime() != null) {
            raffle.setDrawTime(request.drawTime());
        }
        if (request.coverImage() != null) {
            raffle.setCoverImage(request.coverImage());
        }
        if (request.contactPhoneNumber() != null) {
            raffle.setContactPhoneNumber(request.contactPhoneNumber());
        }
        if (request.isPublic() != null) {
            raffle.setPublic(request.isPublic());
        }
        if (request.showWinnerPublicly() != null) {
            raffle.setShowWinnerPublicly(request.showWinnerPublicly());
        }
        if (request.paymentMethods() != null) {
            raffle.setPaymentMethods(request.paymentMethods());
        }
        if (request.drawMethod() != null) {
            raffle.setDrawMethod(request.drawMethod());
        }
        if (request.status() != null) {
            raffle.setStatus(request.status());
        }

        RaffleEntity updated = raffleRepository.save(raffle);
        return toResponse(updated);
    }

    @Transactional
    public void deleteRaffle(Long id) {
        if (!raffleRepository.existsById(id)) {
            throw new RaffleNotFoundException(id);
        }
        raffleRepository.deleteById(id);
    }

    public RaffleEntity getRaffleEntityById(Long id) {
        return raffleRepository.findById(id)
                .orElseThrow(() -> new RaffleNotFoundException(id));
    }

    private RaffleResponse toResponse(RaffleEntity raffle) {
        return new RaffleResponse(
                raffle.getId(),
                raffle.getTitle(),
                raffle.getTotalTickets(),
                raffle.getTicketPrice(),
                raffle.getDescription(),
                raffle.isHasDrawDate(),
                raffle.getDrawDate(),
                raffle.getDrawTime(),
                raffle.getCoverImage(),
                raffle.getContactPhoneNumber(),
                raffle.isPublic(),
                raffle.isShowWinnerPublicly(),
                raffle.getPaymentMethods(),
                raffle.getDrawMethod(),
                raffle.getStatus(),
                raffle.getCreatedAt(),
                raffle.getUpdatedAt()
        );
    }
}
