package ru.practicum.ewmService.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.storage.PrivateParticipationRequestRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrivateParticipationRequestServiceImpl implements PrivateParticipationRequestService {

    private final PrivateParticipationRequestRepository privateParticipationRequestRepository;


    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId, ParticipationRequestDto participationRequestDto) {
        /*
        нельзя добавить повторный запрос (Ожидается код ошибки 409)
        инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного


        Получаем подробную информацию о событии по id и далее работает с его полями
         */
        return null;
    }
}
