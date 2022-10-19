package mx.cacho.choice.soapws.service;

import lombok.extern.slf4j.Slf4j;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.repository.AmenityRepository;
import mx.cacho.choice.soapws.service.exception.IllegalServiceOperationException;
import mx.cacho.choice.soapws.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository amenityRepository;

    @Autowired
    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public Optional<Amenity> getAmenity(final Long amenityId) {
        return amenityRepository.findById(amenityId);
    }

    @Override
    public List<Amenity> getAmenities(Collection<Long> amenityIds) {
        return amenityRepository.findAllById(amenityIds);
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    public Amenity createAmenity(Amenity amenity) {
        if (amenity.getAmenityId() != 0) {
            log.warn("Attempted to create an amenity with id: {}.", amenity);
            throw new IllegalServiceOperationException("Unable to create amenity with pre-populated id.");
        }
        return amenityRepository.save(amenity);
    }
}
