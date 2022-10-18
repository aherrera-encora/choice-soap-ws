package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;

    @Autowired
    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    public Optional<Amenity> getAmenity(Long id) {
        return amenityRepository.findById(id);
    }

    @Override
    public List<Amenity> getAmenities(Collection<Long> amenityIds) {
        return amenityRepository.findAllById(amenityIds);
    }

    @Override
    public boolean createAmenity(Amenity amenity) {
        try {
            amenityRepository.save(amenity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
