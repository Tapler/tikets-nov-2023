package org.psu.java.example.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import org.psu.java.example.domain.Ticket;

/**
 * Добавляет проверку четности номера
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EvenDecorator implements Ticket {
    @Delegate(excludes = Exclude.class)
    Ticket wrapee;

    @Override
    public boolean isFortunate() {
        return wrapee.getNumber() % 2 == 0 && wrapee.isFortunate();
    }

    private interface Exclude {
        boolean isFortunate();
    }
}
