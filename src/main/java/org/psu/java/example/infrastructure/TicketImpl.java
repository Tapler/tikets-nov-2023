package org.psu.java.example.infrastructure;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.utils.NumberUtils;

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

        this.length = length;
        this.number = number;
    }
}
