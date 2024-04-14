package ru.practicum.statsServer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsServer.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT new ru.practicum.statsServer.storage.StatsViewDto(st.app, st.uri, COUNT(st.ip)) " + //as hits
            "FROM Statistic AS st " +
            "WHERE (COALESCE(:uris) IS NULL OR st.uri IN :uris) " +
            "AND st.timestamp BETWEEN CAST(:start AS timestamp) AND CAST(:end AS timestamp) " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC")
    List<StatsViewDto> findAllByUriInAndTimestampBetweenOrderByHitsDesc(List<String> uris,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end);


    @Query("SELECT new ru.practicum.statsServer.storage.StatsViewDto(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Statistic as st " +
            "WHERE (COALESCE(:uris) IS NULL OR st.uri IN :uris) " +
            "AND st.timestamp between CAST(:start AS timestamp) and CAST(:end AS timestamp) " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC")
    List<StatsViewDto> findAllDistinctByIpAndUriInAndTimestampBetween(List<String> uris,
                                                                      LocalDateTime start,
                                                                      LocalDateTime end);
}

