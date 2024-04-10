package ru.practicum.statsServer.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsServer.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Statistic, Long> {

    @Query(value = "SELECT st.app, st.uri, COUNT(st.ip) as hits " +
            "FROM stats as st " +
            "WHERE st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
    List<StatsDtoToUser> findAllByTimestampBetweenOrderByHitsDesc(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT st.app, st.uri, COUNT(DISTINCT st.ip) as hits " +
            "FROM stats as st " +
            "WHERE st.uri IN (?1) " +
            "AND st.timestamp between ?2 and ?3 " +
            "group by st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
    List<StatsDtoToUser> findAllDistinctByIpAndUriInAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT st.app, st.uri, COUNT(DISTINCT st.ip) as hits " +
            "FROM stats as st " +
            "WHERE st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
    List<StatsDtoToUser> findAllDistinctByIpAndTimestampBetween(LocalDateTime start, LocalDateTime end);

/*    @Query(value = "SELECT st.app, st.uri, COUNT(DISTINCT st.ip) as hits " +
            "FROM stats as st " +
            //"WHERE (:uris IS NULL OR st.uri IN (:uris))" +
            "WHERE st.uri IN (COALESCE(:uris, st.uri))" +
            "AND st.timestamp between :start and :end " +
            "group by st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
    List<StatsDtoToUser> findAllDistinctByIpAndTimestampBetweenWithNullableUris(@Param("uris") List<String> uris,
                                                                                @Param("start") LocalDateTime start,
                                                                                @Param("end") LocalDateTime end);*/

    /*    @Query(value = "SELECT st.app, st.uri, COUNT(st.ip) as hits " +
                "FROM stats as st " +
                //"WHERE st.uri IN (:uris) " +
                //"WHERE (:uris IS NULL OR st.uri IN (:uris))" +
                "WHERE st.uri IN (COALESCE(:uris, st.uri))" +
                "AND st.timestamp between :start and :end " +
                "group by st.app, st.uri " +
                "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
        List<StatsDtoToUser> findAllByIpAndTimestampBetweenWithNullableUris(@Param("uris") List<String> uris,
                                                                            @Param("start") LocalDateTime start,
                                                                            @Param("end") LocalDateTime end);*/
    @Query(value = "SELECT st.app, st.uri, COUNT(st.ip) as hits " +
            "FROM stats as st " +
            "WHERE st.uri IN (?1) " +
            "AND st.timestamp between ?2 and ?3 " +
            "group by st.app, st.uri " +
            "ORDER BY COUNT(st.ip) DESC", nativeQuery = true)
    List<StatsDtoToUser> findAllByUriInAndTimestampBetweenOrderByHitsDesc(List<String> uris, LocalDateTime start, LocalDateTime end);
}
