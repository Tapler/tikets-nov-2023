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
        ResponseHistory responseHistory = createResponseHistory();
        int count = fortunateTicketService.count(sixDigitsTicketGenerator.getTickets());
        saveResponseHistory(responseHistory, count);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/four")
    public ResponseEntity<Integer> getFourDigitsFortunateTicketsCount() {
        ResponseHistory responseHistory = createResponseHistory();
        int count = fortunateTicketService.count(fourDigitsTicketGenerator.getTickets());
        saveResponseHistory(responseHistory, count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/extra/{generatorType}")
    public ResponseEntity<Integer> getPathVariableFortunateTicketsCount(@PathVariable GeneratorType generatorType) {
        ResponseHistory responseHistory = createResponseHistory();
        int count = countWithGenerator(fortunateTicketService, generatorType);
        saveResponseHistory(responseHistory, count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/extra")
    public ResponseEntity<Integer> getRequestParamFortunateTicketsCount(@RequestParam(name = "type", required = false) GeneratorType generatorType) {
        if (generatorType == null) {
            return ResponseEntity.badRequest().build();
        }
        ResponseHistory responseHistory = createResponseHistory();
        int count = countWithGenerator(fortunateTicketService, generatorType);
        saveResponseHistory(responseHistory, count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/extra-multiplicity/{type}")
    public ResponseEntity<Integer> getFortunateTicketsMultiplicityCount(@PathVariable(name = "type") GeneratorType generatorType,
                                                                        @RequestParam int multiplicity) {
        ResponseHistory responseHistory = createResponseHistory();
        int count;
        switch (multiplicity) {
            case 2:
                count = countWithGenerator(evenFortunateTicketService, generatorType);
                break;
            case 5:
                count = countWithGenerator(multipleOfFiveFortunateTicketService, generatorType);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        saveResponseHistory(responseHistory, count);
        return ResponseEntity.ok(count);
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
        ResponseHistory responseHistory = createResponseHistory();
        var count = countWithGenerator(service, item.type());
        saveResponseHistory(responseHistory, count);
        return new FortunateTicketResponse(item.type(), item.multiplicity(), count);
    }

    private int countWithGenerator(FortunateTicketService service, GeneratorType generatorType) {
        return switch (generatorType) {
            case FOUR -> service.count(fourDigitsTicketGenerator.getTickets());
            case SIX -> service.count(sixDigitsTicketGenerator.getTickets());
            case EIGHT -> service.count(eightDigitsTicketGenerator.getTickets());
        };
    }

    private ResponseHistory createResponseHistory() {
        LocalDateTime startTime = LocalDateTime.now();
        return ResponseHistory.builder().startTime(startTime).build();
    }

    private void saveResponseHistory(ResponseHistory responseHistory, int count) {
        responseHistory.setEndTime(LocalDateTime.now());
        responseHistory.setResult(count);
        responseHistoryRepository.save(responseHistory);
        log.info(responseHistory.toString());
        log.info(String.format("Все объекты: %s",responseHistoryRepository.findAll()));
    }


    record FortunateTicketRequest(GeneratorType type, Integer multiplicity) { }
    record FortunateTicketResponse(GeneratorType type, Integer multiplicity, int count) { }

}
