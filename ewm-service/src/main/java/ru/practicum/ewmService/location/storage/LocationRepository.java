package ru.practicum.ewmService.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.location.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByNameContaining(String text);

    @Query(value = "SELECT * FROM locations as loc " +
            "WHERE (" +
            "SELECT distance(loc.lat, loc.lon, :lat2, :lon2) <= loc.rad" +
            ") " +
            "LIMIT 1;", nativeQuery = true)
    List<Location> getLocationByCoordinates(@Param("lat2") Float lat, @Param("lon2") Float lon);

}
