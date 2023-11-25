package org.psu.java.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * Выводит количество шестизначных счастливых билетов
 */
@Slf4j
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE)
@ComponentScan("org.psu.java.example.context")
public class Tickets implements CommandLineRunner {

    //    @Autowired
    final FortunateTicketService fortunateTicketService;

    //    @Autowired
//    ломбок не умеет автоматически генерировать конструктор с @Qualifier
//    @Qualifier("eightDigitsTicketGenerator")
    final TicketGenerator ticketGenerator;

    public Tickets(FortunateTicketService fortunateTicketService,
//                   @Qualifier("eightDigitsTicketGenerator")
                   TicketGenerator ticketGenerator) {
        this.fortunateTicketService = fortunateTicketService;
        this.ticketGenerator = ticketGenerator;
    }

    ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Tickets.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world Spring Boot style");
//        var fortunateTicketService = context.getBean(FortunateTicketService.class);
//        var ticketGenerator = context.getBean("recordTicketGenerator", TicketGenerator.class);
        var count = fortunateTicketService.count(ticketGenerator.getTickets());
        log.info(String.valueOf(count));
    }
}