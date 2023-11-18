package org.psu.java.example.context;

import org.psu.java.example.application.EvenDecorator;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.application.LastDigitSixDecorator;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.GeneratorType;
import org.psu.java.example.infrastructure.TicketGenerator;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * Выводит количество шестизначных счастливых билетов
 */
public class Tickets {
    private static TicketGenerator generator = TicketGenerator.getInstance(GeneratorType.SIX);
    private static FortunateTicketService service = FortunateTicketService.getStreamInstance();

    public static void main(String[] args) {
        var result = service.count(generator.getTickets());
        System.out.println(result);

        var d = 5;
        var n = (Integer) null;
        var var = new ArrayList<Integer>();
        BiFunction<Integer, Integer, Integer> biFunction =
                (a, b) -> {
                    int res = a + b + d;
                    System.out.println(a + "+" + "b" + "+" + "d" + "=" + res);
                    return res;
                };
        Predicate<Integer> greaterThanSeven = x -> x > 7;
        Predicate<Integer> lessThanTen = x -> x < 10;

        System.out.println(greaterThanSeven.and(lessThanTen).test(9));

        Supplier<Integer> getSeven = () -> 7;
        Function<Integer, String> asString = Object::toString;
        Function<Integer, String> toChars =
                asString
                        .andThen(String::toCharArray)
                        .andThen(arr -> {
                            StringJoiner joiner = new StringJoiner(", ");
                            for (char item : arr) {
                                joiner.add(String.valueOf(item));
                            }
                            return joiner.toString();
                        });
        System.out.println(toChars.apply(100500));

        StringJoiner joiner = new StringJoiner(", ");

        Function<Ticket, String> ticketToString =
                ((Function<Ticket, Long>) Ticket::getNumber).andThen(Objects::toString);

        for (var i = -100; i < 10000; i++) {
            Optional<Ticket> ticket = generator.getTicket(i);
            ticket
                    .map(LastDigitSixDecorator::new)
                    .map(EvenDecorator::new)
                    .filter(Ticket::isFortunate)
                    .map(ticketToString)
                    .ifPresent(joiner::add);
        }
        System.out.println("Счастливые билеты: " + joiner);
    }
}