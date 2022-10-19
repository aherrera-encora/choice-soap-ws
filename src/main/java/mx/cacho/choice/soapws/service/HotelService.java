package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HotelService {

    Optional<Hotel> getHotel(Long hotelId);

    List<Hotel> getHotels(Collection<Long> hotelIds);

    List<Hotel> getAllHotels();

    List<Hotel> getHotelsByName(String namePattern);

    Hotel createHotel(Hotel hotel);

    void deleteHotel(Long hotelId);

    Hotel updateHotel(Hotel hotel);

    Hotel addAmenitiesToHotel(Long hotelId, Collection<Amenity> amenities);

    Hotel removeAmenitiesFromHotel(Long hotelId, Collection<Long> amenityIds);
}
