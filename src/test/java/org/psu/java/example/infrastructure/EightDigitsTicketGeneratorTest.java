package org.psu.java.example.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.psu.java.example.domain.Ticket;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Тесты для {@link EightDigitsTicketGenerator}
 */
public class EightDigitsTicketGeneratorTest {

    /**
     * все билеты, создаваемые EightDigitsTicketGenerator, имеют правильную длину, равную 8
     * корректно ли проверять длину, если в EightDigitsTicketGenerator определен getLength()=8 ??
     */
    @Test
    public void testTicketLength() {
        TicketGenerator generator = new EightDigitsTicketGenerator();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertEquals(8, ticket.getLength());
        }
    }

    /**
     * все номера билетов находятся в пределах от 0 до 99999999 (включительно)
     */
    @Test
    public void testTicketNumberRange() {
        TicketGenerator generator = new EightDigitsTicketGenerator();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertTrue(ticket.getNumber() >= 0 && ticket.getNumber() < 100000000);
        }
    }

    /**
     * проверка, что все билеты уникальные (ошибка Java heap space)
     */
    @Test(timeout = 10)
    @Ignore("Сбой из-за нехватки памяти")
    public void testUniqueTickets() {
        TicketGenerator generator = new EightDigitsTicketGenerator();
        Set<Long> uniqueTicketNumbers = new HashSet<>();
        Iterator<Ticket> iterator = generator.getTickets();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            assertTrue(uniqueTicketNumbers.add(ticket.getNumber()));
        }
    }

    /**
     * проверка, что все билеты уникальные (без ошибки Java heap space)
     */
    @Test
    public void testUniqueTickets2() {
        TicketGenerator generator = new EightDigitsTicketGenerator();
        Set<Long> uniqueTicketNumbers = new HashSet<>();
        Iterator<Ticket> iterator = generator.getTickets();

        // Ограничим количество создаваемых билетов для теста
        int numberOfTicketsToGenerate = 65_000_000;

        for (int i = 0; i < numberOfTicketsToGenerate && iterator.hasNext(); i++) {
            Ticket ticket = iterator.next();
            assertTrue(uniqueTicketNumbers.add(ticket.getNumber()));
        }
    }
}