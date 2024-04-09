package ru.practicum.ewmService.statClient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.statsDto.StatsToUserDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StatsDtoToUserList {
    private List<StatsToUserDto> statsToUserDtoList;

    public StatsDtoToUserList() {
        statsToUserDtoList = new ArrayList<>();
    }

    public StatsDtoToUserList(List<StatsToUserDto> statsToUserDtoList) {
        this.statsToUserDtoList = statsToUserDtoList;
    }
}
