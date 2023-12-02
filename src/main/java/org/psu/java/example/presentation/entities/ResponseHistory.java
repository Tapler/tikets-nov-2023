package org.psu.java.example.presentation.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность для хранения истории запросов к REST API
 */
@Entity
@Data
@Table(name = "HISTORY")
//@IdClass(ResponseHistory.PrimaryKey.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseHistory implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    LocalDateTime endTime;

    @Column(name = "result")
    Integer result;

//    record PrimaryKey(LocalDateTime startTime, LocalDateTime endTime) implements Serializable { }

//    public ResponseHistory(LocalDateTime startTime, LocalDateTime endTime, Integer result) {
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.result = result;
//    }
}