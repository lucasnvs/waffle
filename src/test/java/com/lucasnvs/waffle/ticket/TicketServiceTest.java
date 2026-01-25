package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.common.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.common.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseMessage;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    TicketPurchaseProducer producer;

    @Mock
    RaffleRepository raffleRepository;

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketService service;

    @Test
    void shouldSendMessageToQueueWhenRaffleIsOpen() {
        RaffleEntity raffle = new RaffleEntity();
        raffle.setId(1L);
        raffle.setStatus(RaffleStatus.OPEN);
        raffle.setTotalTickets(100);

        when(raffleRepository.findById(1L))
                .thenReturn(Optional.of(raffle));

        PurchaseTicketRequest request = new PurchaseTicketRequest(List.of(42));

        service.requestPurchase(1L, request, "user-1");

        verify(producer).send(any(TicketPurchaseMessage.class));
    }

    @Test
    void shouldFailWhenRaffleIsClosed() {
        RaffleEntity raffle = new RaffleEntity();
        raffle.setId(1L);
        raffle.setStatus(RaffleStatus.CLOSED);
        raffle.setTotalTickets(100);

        when(raffleRepository.findById(1L))
                .thenReturn(Optional.of(raffle));

        PurchaseTicketRequest request = new PurchaseTicketRequest(List.of(42));

        assertThrows(
                RaffleNotOpenException.class,
                () -> service.requestPurchase(1L, request, "user-1")
        );

        verify(producer, never()).send(any());
    }

    @Test
    void shouldFailWhenTicketAlreadySold() {
        RaffleEntity raffle = new RaffleEntity();
        raffle.setId(1L);
        raffle.setStatus(RaffleStatus.OPEN);
        raffle.setTotalTickets(100);

        when(raffleRepository.findById(1L))
                .thenReturn(Optional.of(raffle));
        when(ticketRepository.existsByRaffleIdAndNumber(1L, 42))
                .thenReturn(true);

        PurchaseTicketRequest request = new PurchaseTicketRequest(List.of(42));

        assertThrows(
                TicketAlreadySoldException.class,
                () -> service.requestPurchase(1L, request, "user-1")
        );

        verify(producer, never()).send(any());
    }
}
