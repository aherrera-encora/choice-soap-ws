package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Validated
public interface AmenityService {

    public Optional<Amenity> getAmenity(Long amenityId);

    public List<Amenity> getAmenities(Collection<Long> amenityIds);

    public List<Amenity> getAllAmenities();

    Amenity createAmenity(@Valid Amenity amenity);
}
