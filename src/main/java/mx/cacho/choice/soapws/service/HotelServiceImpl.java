package mx.cacho.choice.soapws.service;

import lombok.extern.slf4j.Slf4j;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.repository.AmenityRepository;
import mx.cacho.choice.soapws.repository.HotelRepository;
import mx.cacho.choice.soapws.service.exception.IllegalServiceOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, AmenityRepository amenityRepository) {
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    public Optional<Hotel> getHotel(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public List<Hotel> getHotels(Collection<Long> hotelIds) {
        return hotelRepository.findAllById(hotelIds);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Page<Hotel> getAllHotels(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return hotelRepository.findAll(pageable);
    }

    @Override
    public List<Hotel> getHotelsByName(String namePattern) {
        return hotelRepository.getHotelsByNameContainingIgnoringCase(namePattern);
    }

    @Override
    public Hotel createHotel(Hotel hotel) throws IllegalServiceOperationException {
        if (hotel.getHotelId() != 0) {
            log.warn("Attempted to create a hotel with id: {}.", hotel);
            throw new IllegalServiceOperationException("Unable to create hotel with pre-populated id.");
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Override
    public Hotel updateHotel(Hotel hotel) throws IllegalServiceOperationException {
        if (hotelRepository.existsById(hotel.getHotelId())) {
            return hotelRepository.save(hotel);
        } else {
            log.warn("Attempted to update hotel with non-existing id: {}.", hotel);
            throw new IllegalServiceOperationException("Unable to update hotel for non-existing id.");
        }
    }

    @Override
    public Hotel addAmenitiesToHotel(Long hotelId, Collection<Amenity> amenities) throws IllegalServiceOperationException {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if (hotelOptional.isEmpty()) {
            log.warn("Attempted to add amenities to non-existing hotel id: {}.", hotelId);
            throw new IllegalServiceOperationException("Unable to add amenities to non-existing hotel id.");
        }

        Hotel hotel = hotelOptional.get();
        hotel.addAmenities(amenities);
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel removeAmenitiesFromHotel(Long hotelId, Collection<Long> amenityIds) throws IllegalServiceOperationException {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);

        if (hotelOptional.isEmpty()) {
            log.warn("Attempted to remove amenities from non-existing hotel id: {}.", hotelId);
            throw new IllegalServiceOperationException("Unable to remove amenities from non-existing hotel id.");
        }
        Hotel hotel = hotelOptional.get();
        hotel.removeAmenities(amenityIds);
        return hotelRepository.save(hotel);
    }
}
