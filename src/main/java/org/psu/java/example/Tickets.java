package org.psu.java.example;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;


/**
 * Выводит количество шестизначных счастливых билетов
 */
@Slf4j
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE)
@ComponentScan("org.psu.java.example.context")
public class Tickets implements CommandLineRunner {
    //    @Autowired
    FortunateTicketService fortunateTicketService;

    //    @Autowired
    List<TicketGenerator> ticketGenerator;
    Map<String, TicketGenerator> ticketGeneratorAsMap;

    ApplicationContext context;

    public Tickets(FortunateTicketService fortunateTicketService,
                   List<TicketGenerator> ticketGenerator,
                   Map<String, TicketGenerator> ticketGeneratorAsMap,
                   ApplicationContext context) {
        this.fortunateTicketService = fortunateTicketService;
        this.ticketGenerator = ticketGenerator;
        this.ticketGeneratorAsMap = ticketGeneratorAsMap;
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Tickets.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world Spring Boot style!");
//        var fortunateTicketService = context.getBean(FortunateTicketService.class);
//        var ticketGenerator = context.getBean("recordTicketGenerator", TicketGenerator.class);
//        var count = fortunateTicketService.count(ticketGenerator.getTickets());
        ticketGenerator
                .stream()
                .map(TicketGenerator::getTickets)
                .mapToInt(fortunateTicketService::count)
                .mapToObj(String::valueOf)
                .forEach(log::info);
//        log.info(String.valueOf(count));
    }
}