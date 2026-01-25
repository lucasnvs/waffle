package com.lucasnvs.waffle.integration;

import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.TicketRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketIdempotencyIntegrationTest
        extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    RaffleRepository raffleRepository;

    @Test
    void shouldNotCreateDuplicateTicketWhenRetryingRequest()
            throws Exception {

        RaffleEntity raffle = new RaffleEntity();
        raffle.setTitle("Idempotent Raffle");
        raffle.setTotalTickets(100);
        raffle.setTicketPrice(10.0);
        raffle.setStatus(RaffleStatus.OPEN);
        raffle = raffleRepository.save(raffle);

        String idempotencyKey = UUID.randomUUID().toString();

        for (int i = 0; i < 2; i++) {
            mockMvc.perform(post(
                            "/raffles/" + raffle.getId() + "/tickets"
                    )
                            .header("Idempotency-Key", idempotencyKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                {
                  "number": 7,
                  "userId": "user-1"
                }
            """))
                    .andExpect(status().isAccepted());
        }

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1, ticketRepository.count());
                });
    }
}
