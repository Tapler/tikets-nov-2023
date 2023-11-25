package org.psu.java.example.infrastructure;

import lombok.Getter;
import org.psu.java.example.domain.Ticket;

public record TicketRecordImpl(int length, long number) implements Ticket {
    public TicketRecordImpl {
        if (number < 0) {
            throw new IllegalArgumentException(String.format("Передан номер %d < 0", number));
        }

        if (length < 0) {
            throw new IllegalArgumentException(String.format("Передана длина билета %d < 0", length));
        }
        var digitNumber = (int) (Math.log10(number) + 1);

        if (digitNumber > length) {
            throw new IllegalArgumentException(String.format("Передан билет %d больше чем длина билета %d", length, number));
        }
    }

    @Override
    public int getLength() {
        return length();
    }

    @Override
    public long getNumber() {
        return number();
    }
}
