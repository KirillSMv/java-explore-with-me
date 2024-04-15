package ru.practicum.statsDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.statsDto.validation.ValidIPv4;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewStatsDto {

    @NotBlank(message = "app value cannot be empty")
    String app;

    @NotBlank(message = "uri value cannot be empty")
    String uri;

    @ValidIPv4
    String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
