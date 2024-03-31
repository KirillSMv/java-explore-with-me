package ru.practicum.statsServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String app;

    @Column
    String uri;

    @Column
    String ip;

    @Column
    LocalDateTime timestamp;

    @Column
    Long hits = 1L;
}
