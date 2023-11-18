package org.psu.java.example.infrastructure;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Тесты для {@link TicketImpl}
 */
public class TicketImplTest {

    /**
     * Проверяет успешное создание объекта
     */
    @Test
    public void testSuccessfullyCreated() {
        // given
        var length = 6;
        var number = 100_500;

        // when
        var actual = new TicketImpl(length, number);
        // then
        assertNotNull("Object must be created", actual);
    }

    /**
     * Проверяет, что выбрасывается ошибка с нужным текстом если передано отрицательное число
     */
    @Test
    public void testNegativeNumber() {
        // given
        var length = 6;
        var number = -100_500;
        var expected = String.format("Передан номер %d < 0", number);

        // when
        try {
            new TicketImpl(length, number);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Должно быть брошено исключение с нужным текстом", expected, e.getMessage());
            return;
        }
        fail("Должно быть выброшено исключение");
    }

    /**
     * Проверяет, что выбрасывается ошибка с нужным текстом если передано число с разрядностью больше чем length
     */
    @Test
    public void testLengthAndNubmerLengthNotEquals() {
        // given
        var length = 6;
        var number = 100_000_500;
        var expected = String.format("Передан билет %d больше чем длина билета %d", length, number);

        // when
        try {
            new TicketImpl(length, number);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Должно быть брошено исключение с нужным текстом", expected, e.getMessage());
            return;
        }
        fail("Должно быть выброшено исключение");
    }

    /**
     * Проверяет, что билет счастливый
     */
    @Test
    public void testTicketIsFortunate() {
        // given
        var length = 6;
        var number = 0;
        // when
        var actual = new TicketImpl(length, number);
        // then
        assertTrue(actual.isFortunate());
    }

    /**
     * Проверяет, что билет не счастливый
     */
    @Test
    public void testTicketIsNotFortunate() {
        // given
        var length = 6;
        var number = 100_050;
        // when
        var actual = new TicketImpl(length, number);
        // then
        assertFalse(actual.isFortunate());
    }

    /**
     * Проверяет, что семизначный билет несчастливый даже если сумма цифр равна
     */
    @Test
    public void testSevenDigitTicketIsNotFortunate() {
        // given
        var length = 7;
        var number = 1000001;
        // when
        var actual = new TicketImpl(length, number);
        // then
        assertFalse(actual.isFortunate());
    }
}