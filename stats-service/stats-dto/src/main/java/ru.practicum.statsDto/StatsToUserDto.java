package ru.practicum.statsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsToUserDto {
    String app;
    String uri;
    Long hits;
}
