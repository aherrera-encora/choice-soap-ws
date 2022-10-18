package mx.cacho.choice.soapws.repository;

import mx.cacho.choice.soapws.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    List<Amenity> getAmenitiesByHotels(Long hotelId);
}
