package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.repository.AmenityRepository;
import mx.cacho.choice.soapws.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public List<Hotel> getHotelsByName(String namePattern) {
        return hotelRepository.getHotelsByNameContainingIgnoringCase(namePattern);
    }

    @Override
    public boolean createHotel(Hotel hotel) {
        if (hotelRepository.existsById(hotel.getHotelId())) {
            return false;
        } else {
            hotelRepository.save(hotel);
            return true;
        }
    }

    @Override
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Override
    public void updateHotel(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getHotelId())){
            hotelRepository.save(hotel); //TODO: Fix logic. Error handling
        }
    }

    @Override
    public Optional<Hotel> addAmenitiesToHotel(Long hotelId, Collection<Amenity> amenities) {
        return hotelRepository.findById(hotelId).map(h -> {
            h.addAmenities(amenities);
            return hotelRepository.save(h);
        });
    }

    @Override
    public Optional<Hotel> removeAmenitiesFromHotel(Long hotelId, Collection<Long> amenityIds) {
        return hotelRepository.findById(hotelId).map(h -> {
            h.removeAmenities(amenityIds);
            return hotelRepository.save(h);
        });
    }
}
