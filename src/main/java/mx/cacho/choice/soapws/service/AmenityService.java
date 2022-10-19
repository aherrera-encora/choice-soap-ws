package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AmenityService {

    public Optional<Amenity> getAmenity(Long amenityId);

    public List<Amenity> getAmenities(Collection<Long> amenityIds);

    public List<Amenity> getAllAmenities();

    Amenity createAmenity(Amenity amenity);
}
