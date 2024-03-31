package ru.practicum.statsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStatsParamsDto {
    LocalDateTime start;
    LocalDateTime end;
    List<String> uris;
    Boolean unique;
}
