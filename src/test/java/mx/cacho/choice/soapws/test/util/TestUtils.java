package mx.cacho.choice.soapws.test.util;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;

import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

public class TestUtils {

    public static Amenity generateAmenity() {
        return generateAmenity(nextLong(1, Long.MAX_VALUE), randomAlphanumeric(10), randomAlphanumeric(10));
    }

    public static Amenity generateAmenity(Long id) {
        return generateAmenity(id, randomAlphanumeric(10), randomAlphanumeric(10));
    }

    public static Amenity generateAmenity(Long id, String name, String description) {
        return Amenity.builder().amenityId(id).name(name).description(description).build();
    }

    public static Hotel generateHotel(Long id, String name, String description, String country, String state, String city, String zipCode, String address, Set<Amenity> amenities) {
        return Hotel.builder().hotelId(id).name(name).description(description).country(country).state(state).city(city).zipCode(zipCode).address(address).amenities(amenities).build();
    }

    public static Hotel generateHotel(Long id) {
        return generateHotel(id, randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(5), randomAlphanumeric(20), null);
    }

    public static Hotel generateHotel(Long id, Set<Amenity> amenities) {
        return generateHotel(id, randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(5), randomAlphanumeric(20), amenities);
    }

    public static Hotel generateHotel(Set<Amenity> amenities) {
        return generateHotel(nextLong(1, Long.MAX_VALUE), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(5), randomAlphanumeric(20), amenities);
    }

    public static Hotel generateHotel(Long id, String name, String description) {
        return generateHotel(id, name, description, randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(5), randomAlphanumeric(20), null);
    }


    public static Hotel generateHotel(Long id, String name, String description, Set<Amenity> amenities) {
        return generateHotel(id, name, description, randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(20), randomAlphanumeric(5), randomAlphanumeric(20), amenities);
    }
}
