package org.psu.java.example.infrastructure;

public class EvenTicketImpl extends TicketImpl{
    public EvenTicketImpl(int length, long number) {
        super(length, number);
    }

    @Override
    public boolean isFortunate() {
        return getNumber() % 2 == 0 && super.isFortunate();
    }
}
