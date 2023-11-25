package org.psu.java.example.infrastructure;

import org.junit.Test;
import org.psu.java.example.domain.Ticket;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Тесты для {@link RecordTicketGenerator}
 */
public class RecordTicketGeneratorTest {
    /**
     * Проверка, что билеты, созданные RecordTicketGenerator, имеют правильную длину.
     */
    @Test
    public void testTicketLength() {
        TicketGenerator generator = new RecordTicketGenerator();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertEquals(6, ticket.getLength());
        }
    }

    /**
     * Проверка уникальности билетов, созданных RecordTicketGenerator.
     */
    @Test
    public void testUniqueTickets() {
        TicketGenerator generator = new RecordTicketGenerator();
        Set<Long> uniqueTicketNumbers = new HashSet<>();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertTrue(uniqueTicketNumbers.add(ticket.getNumber()));
        }
    }

    /**
     * Проверка, что номера билетов, созданных RecordTicketGenerator, находятся в правильном диапазоне.
     */
    @Test
    public void testTicketNumberRange() {
        TicketGenerator generator = new RecordTicketGenerator();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertTrue(ticket.getNumber() >= 0 && ticket.getNumber() < 1000000);
        }
    }
}