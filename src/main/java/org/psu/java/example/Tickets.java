package org.psu.java.example;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
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


/**
 * Выводит количество шестизначных счастливых билетов
 */
@Slf4j
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE)
@ComponentScan("org.psu.java.example.context")
@AllArgsConstructor
public class Tickets implements CommandLineRunner {
    //    @Autowired
//    FortunateTicketService fortunateTicketService;

    //    @Autowired
    List<TicketGenerator> ticketGenerator;
//    Map<String, TicketGenerator> ticketGeneratorAsMap;

    ApplicationContext context;

    @Getter(lazy = true)
    final Tickets self = prepare();

    public Tickets prepare() {
        return context.getBean(Tickets.class);
    }

//    Tickets self2;
//
//    @PostConstruct
//    private void intSelf2() {
//        self2 = prepare();
//    }

//    public Tickets(FortunateTicketService fortunateTicketService,
//                   List<TicketGenerator> ticketGenerator,
//                   Map<String, TicketGenerator> ticketGeneratorAsMap,
//                   ApplicationContext context) {
//        this.fortunateTicketService = fortunateTicketService;
//        this.ticketGenerator = ticketGenerator;
//        this.ticketGeneratorAsMap = ticketGeneratorAsMap;
//        this.context = context;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Tickets.class);
    }

    @Override
    public void run(String... args) {
        log.info("Hello world Spring Boot style!");
        prepare().calculate("recordTicketGenerator");
        prepare().calculate("eightDigitsTicketGenerator");
        prepare().calculate("fourDigitsTicketGenerator");
//        recordTicketGenerator
//                .stream()
//                .map(TicketGenerator::getTickets)
//                .mapToInt(serviceForRecordTicketGenerator::recordTicketCount)
//                .mapToObj(String::valueOf)
//                .forEach(log::info);
    }

    private void calculate(String generatorName) {
        var serviceTicketGenerator = context.getBean("getMultipleFiveFortunateTicketService", FortunateTicketService.class);
        var serviceEvenTicketGenerator = context.getBean("getEvenFortunateTicketService", FortunateTicketService.class);
        var serviceBothGenerator = context.getBean("getBothDecorators", FortunateTicketService.class);
        var recordTicketGenerator = context.getBean(generatorName, TicketGenerator.class);
        var recordTicketCount = serviceTicketGenerator.count(recordTicketGenerator.getTickets());
        log.info(generatorName + " подсчитал количество кратных пяти: " + recordTicketCount);
        var evenTicketsGenerator = context.getBean(generatorName, TicketGenerator.class);
        var evenTicketsGeneratorCount = serviceEvenTicketGenerator.count(evenTicketsGenerator.getTickets());
        log.info(generatorName + " подсчитал количество четных: " + evenTicketsGeneratorCount);
    }
}