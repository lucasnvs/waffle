package com.lucasnvs.waffle.integration;

import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.TicketRepository;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketPurchaseIntegrationTest
        extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    RaffleRepository raffleRepository;

    @Test
    void shouldPersistTicketAfterAsyncProcessing()
            throws Exception {

        RaffleEntity raffle = new RaffleEntity();
        raffle.setTitle("Test Raffle");
        raffle.setTotalTickets(100);
        raffle.setTicketPrice(10.0);
        raffle.setStatus(RaffleStatus.OPEN);
        raffle = raffleRepository.save(raffle);

        mockMvc.perform(post(
                        "/raffles/" + raffle.getId() + "/tickets"
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "number": 10,
              "userId": "user-test"
            }
        """))
                .andExpect(status().isAccepted());

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(
                            1,
                            ticketRepository.count()
                    );
                });
    }
}

