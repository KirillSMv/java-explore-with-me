package ru.practicum.ewmService.location.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Column(name = "rad")
    private Float rad = 0f;

    public Location(Long id, String name, Float lat, Float lon, Float rad) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.rad = rad;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Location location = (Location) other;
        return this.id != null && id.equals(location.getId());
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", rad=" + rad +
                '}';
    }

    @Override
    public int hashCode() {
        return 18;
    }
}
