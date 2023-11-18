package org.psu.java.example.domain;

import org.psu.java.example.utils.NumberUtils;

public interface Ticket {
    private int getDiscriminant() {
        return (int) Math.pow(10, getLength() / 2);
    }
    default boolean isFortunate() {
        if (getLength() % 2 != 0) {
            return false;
        }
        var first = NumberUtils.digitsSum(getNumber() / getDiscriminant());
        var second = NumberUtils.digitsSum(getNumber() % getDiscriminant());
        return first == second;
    }

    int getLength();

    long getNumber();
}
