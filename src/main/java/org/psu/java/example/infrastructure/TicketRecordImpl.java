package org.psu.java.example.infrastructure;

import lombok.Getter;
import org.psu.java.example.domain.Ticket;

public record TicketRecordImpl(int length, long number) implements Ticket {

    @Override
    public int getLength() {
        return length();
    }

    @Override
    public long getNumber() {
        return number();
    }
}