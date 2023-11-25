package org.psu.java.example.context;

import org.psu.java.example.application.EvenDecorator;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.application.FortunateTicketStreamImpl;
import org.psu.java.example.domain.Ticket;
import org.psu.java.example.infrastructure.TicketImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.function.UnaryOperator;

/**
 * Конфигурация
 */
@Configuration
@ComponentScan(basePackageClasses = TicketImpl.class)
public class Config {
    @Bean
    FortunateTicketService getFortunateTicketService(@Qualifier("evenDecorator") UnaryOperator<Ticket> decorator) {
        return new FortunateTicketStreamImpl(decorator);
    }

    @Bean("evenDecorator")
    UnaryOperator<Ticket> getDecorator() {
        return EvenDecorator::new;
    }

}
