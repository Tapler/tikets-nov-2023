package org.psu.java.example.presentation;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.psu.java.example.application.FortunateTicketService;
import org.psu.java.example.infrastructure.GeneratorType;
import org.psu.java.example.infrastructure.TicketGenerator;
import org.psu.java.example.presentation.entities.ResponseHistory;
import org.psu.java.example.presentation.entities.ResponseHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST-контроллер для работы с билетами
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/tickets")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketsController {

    TicketGenerator sixDigitsTicketGenerator;
    TicketGenerator fourDigitsTicketGenerator;
    TicketGenerator eightDigitsTicketGenerator;
    FortunateTicketService fortunateTicketService;
    FortunateTicketService evenFortunateTicketService;
    FortunateTicketService multipleOfFiveFortunateTicketService;
    ResponseHistoryRepository responseHistoryRepository;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    Map<Integer, FortunateTicketService> fortunateTicketServices = prepare();

    private Map<Integer, FortunateTicketService> prepare() {
        Map<Integer, FortunateTicketService> result = new HashMap<>();
        result.put(2, evenFortunateTicketService);
        result.put(5, multipleOfFiveFortunateTicketService);
        return result;
    }

    @GetMapping("/six")
    public ResponseEntity<Integer> getSixDigitsFortunateTicketsCount() {
        // Servlet(HttpRequest, HttpResponse) doGet, doPost, doPut Jackson
        int count = fortunateTicketService.count(sixDigitsTicketGenerator.getTickets());
        return ResponseEntity.ok(count);
    }


    @GetMapping("/four")
    public ResponseEntity<Integer> getFourDigitsFortunateTicketsCount() {
        int count = fortunateTicketService.count(fourDigitsTicketGenerator.getTickets());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/extra/{generatorType}")
    public ResponseEntity<Integer> getPathVariableFortunateTicketsCount(@PathVariable GeneratorType generatorType) {
        return ResponseEntity.ok(countWithGenerator(fortunateTicketService, generatorType));
    }

    @GetMapping("/extra")
    public ResponseEntity<Integer> getRequestParamFortunateTicketsCount(@RequestParam(name = "type", required = false) GeneratorType generatorType) {
        if (generatorType == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(countWithGenerator(fortunateTicketService, generatorType));
    }

    @GetMapping("/extra-multiplicity/{type}")
    public ResponseEntity<Integer> getFortunateTicketsMultiplicityCount(@PathVariable(name = "type") GeneratorType generatorType,
                                                                        @RequestParam int multiplicity) {
        return switch (multiplicity) {
            case 2 -> ResponseEntity.ok(countWithGenerator(evenFortunateTicketService, generatorType));
            case 5 -> ResponseEntity.ok(countWithGenerator(multipleOfFiveFortunateTicketService, generatorType));
            default -> ResponseEntity.badRequest().build();
        };
    }

    @PostMapping
    public ResponseEntity<Collection<FortunateTicketResponse>> getFortunateTicketCounts(@RequestBody Collection<FortunateTicketRequest> body) {
        var counts = body.stream().map(this::calculate).toList();
        return ResponseEntity.ok(counts);
    }

    private FortunateTicketResponse calculate(FortunateTicketRequest item) {
        if (item.multiplicity() == null) {
            var count = countWithGenerator(fortunateTicketService, item.type());
            return new FortunateTicketResponse(item.type(), item.multiplicity(), count);
        }

        var service = getFortunateTicketServices().getOrDefault(item.multiplicity(), fortunateTicketService);
        LocalDateTime startTime = LocalDateTime.now();
        var count = countWithGenerator(service, item.type());
        LocalDateTime endTime = LocalDateTime.now();
        ResponseHistory responseHistory = ResponseHistory.builder()
                .startTime(startTime)
                .endTime(endTime)
                .result(count)
                .build();
        responseHistoryRepository.save(responseHistory);
        log.info(responseHistory.toString());
        return new FortunateTicketResponse(item.type(), item.multiplicity(), count);
    }

    private int countWithGenerator(FortunateTicketService service, GeneratorType generatorType) {
        return switch (generatorType) {
            case FOUR -> service.count(fourDigitsTicketGenerator.getTickets());
            case SIX -> service.count(sixDigitsTicketGenerator.getTickets());
            case EIGHT -> service.count(eightDigitsTicketGenerator.getTickets());
        };
    }

    record FortunateTicketRequest(GeneratorType type, Integer multiplicity) { }
    record FortunateTicketResponse(GeneratorType type, Integer multiplicity, int count) { }

}