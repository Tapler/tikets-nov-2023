package org.psu.java.example.context;

import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.TicketImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация
 */
@Configuration
@ComponentScan(basePackageClasses = TicketImpl.class)
public class Config {

    @Bean
    FortunateTicketService getFortunateTicketService() {
        return FortunateTicketService.getStreamInstance();
    }
}
