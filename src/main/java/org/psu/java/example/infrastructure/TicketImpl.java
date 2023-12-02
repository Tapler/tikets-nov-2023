package org.psu.java.example.infrastructure;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketImpl implements Ticket {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    int length;
    long number;

    public TicketImpl(int length, long number) {
        int maxTicketNumber = (int) Math.pow(10, length) - 1;
        if (number > maxTicketNumber) {
            throw new IllegalArgumentException(String.format("%d > %d", number, maxTicketNumber));
        }
        if (number < 0) {
            throw new IllegalArgumentException(String.format("Передан %d < 0", number));
        }
        this.length = length;
        this.number = number;
    }
}