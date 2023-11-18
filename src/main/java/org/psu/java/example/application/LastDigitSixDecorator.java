package org.psu.java.example.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;

/**
 * Добавляет проверку последней цифры номера на равенство шести
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LastDigitSixDecorator implements Ticket {
    Ticket wrapee;

    @Override
    public boolean isFortunate() {
        return wrapee.getNumber() % 10 == 6 && wrapee.isFortunate() ;
    }

    @Override
    public int getLength() {
        return wrapee.getLength();
    }

    @Override
    public long getNumber() {
        return wrapee.getNumber();
    }
}
