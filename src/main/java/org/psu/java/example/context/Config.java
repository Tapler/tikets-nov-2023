package org.psu.java.example.context;

import org.psu.java.example.application.EvenDecorator;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.application.FortunateTicketStreamImpl;
import org.psu.java.example.application.MultipleOfFiveDecorator;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.TicketImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Конфигурация
 */
@Configuration
@ComponentScan(basePackageClasses = TicketImpl.class)
public class Config {
    @Bean
//    @Primary
    FortunateTicketService getEvenFortunateTicketService(@Qualifier("getEvenDecorator") UnaryOperator<Ticket> decorator) {
        return new FortunateTicketStreamImpl(decorator);
    }

    @Bean
    FortunateTicketService getMultipleFiveFortunateTicketService(@Qualifier("getNumberMultipleOfFiveDecorator") UnaryOperator<Ticket> decorator) {
        return new FortunateTicketStreamImpl(decorator);
    }

    @Bean
    FortunateTicketService getBothDecorators(
            @Qualifier("getEvenDecorator") UnaryOperator<Ticket> evenDecorator,
            @Qualifier("getNumberMultipleOfFiveDecorator") UnaryOperator<Ticket> divFiveDecorator
    ) {
        return new FortunateTicketStreamImpl(t -> evenDecorator.apply(divFiveDecorator.apply(t)));
    }

    @Bean
    FortunateTicketService getDecoratorsFromList(List<Function<Ticket, Ticket>> list) {
        return new FortunateTicketStreamImpl(t -> (Ticket) list.stream().reduce(Function.identity(), Function::andThen));
    }

    @Bean
    UnaryOperator<Ticket> getEvenDecorator() {
        return EvenDecorator::new;
    }

    @Bean
    UnaryOperator<Ticket> getNumberMultipleOfFiveDecorator() {
        return MultipleOfFiveDecorator::new;
    }

}
