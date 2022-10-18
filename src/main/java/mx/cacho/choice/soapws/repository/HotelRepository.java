package mx.cacho.choice.soapws.repository;

import mx.cacho.choice.soapws.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> getHotelsByNameContainingIgnoringCase(String namePattern);

    List<Hotel> getHotelsByAmenities(Long amenityId);
}
