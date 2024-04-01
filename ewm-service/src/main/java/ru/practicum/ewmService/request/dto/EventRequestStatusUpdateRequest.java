package ru.practicum.ewmService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.request.enums.RequestState;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    //Изменение статуса запроса на участие в событии текущего пользователя

    List<Long> requestIds; //Идентификаторы запросов на участие в событии текущего пользователя
    RequestState status; //Новый статус запроса на участие в событии текущего пользователя

}
