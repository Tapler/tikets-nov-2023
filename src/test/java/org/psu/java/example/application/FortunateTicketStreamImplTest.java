package org.psu.java.example.application;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.psu.java.example.infrastructure.TicketImpl;
import org.psu.java.example.infrastructure.TicketRecordImpl;

import java.util.Iterator;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FortunateTicketStreamImplTest {

     TicketGenerator ticketGenerator;
     int maxNumber;
     FortunateTicketService service;
    private Ticket mockTicket;

    @Before
    public void setUp() {
        var length = 4;
        maxNumber = (int) Math.pow(10, 4);
        mockTicket = Mockito.spy(new TicketImpl(length, maxNumber - 1));
        when(mockTicket.isFortunate()).thenReturn(true);
        Iterator<Ticket> mockIterator = IntStream
                .range(0, maxNumber)
                .mapToObj(number -> mockTicket)
                .iterator();
        ticketGenerator = Mockito.mock(TicketGenerator.class);
        when(ticketGenerator.getTickets()).thenReturn(mockIterator);
        service = new FortunateTicketStreamImpl();
    }

    @After
    public void tearDown() {
        service = null;
        ticketGenerator = null;
        maxNumber = 0;
    }
    

    @Test
    public void testCount() {
        // given
        // when
        int actual = service.count(ticketGenerator.getTickets());
        // then
        assertEquals("Должно быть 10 000 счастливых билетов", maxNumber, actual);
        Mockito.verify(mockTicket, atLeast(maxNumber)).isFortunate();
    }
}