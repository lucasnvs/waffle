package com.lucasnvs.waffle.ticket.dto;

import java.util.List;

public record SoldTicketsResponse(Long raffleId, int soldCount, List<Integer> numbers) {}

