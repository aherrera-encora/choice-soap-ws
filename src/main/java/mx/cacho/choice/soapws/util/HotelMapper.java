package mx.cacho.choice.soapws.util;

import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.HotelInfo;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.function.Function;

import static mx.cacho.choice.soapws.util.AmenityMapper.toAmenity;
import static mx.cacho.choice.soapws.util.AmenityMapper.toAmenityInfo;

public class HotelMapper {

    private HotelMapper(){
        throw new IllegalStateException("HotelMapper is non-instantiable");
    }
    public static Function<Hotel, HotelInfo> toHotelInfo() {
        return hotel -> {
            HotelInfo hotelInfo = new HotelInfo();
            BeanUtils.copyProperties(hotel, hotelInfo);
            hotelInfo.setRating(hotel.getRating());
            if(hotel.getAmenities() != null && hotelInfo.getAmenities() != null) {
                hotelInfo.getAmenities().addAll(hotel.getAmenities().stream().map(a -> toAmenityInfo().apply(a)).toList());
            }
            return hotelInfo;
        };
    }

    public static Function<HotelInfo, Hotel> toHotel() {
        return hotelInfo -> {
            Hotel hotel = new Hotel();
            BeanUtils.copyProperties(hotelInfo, hotel);
            if(hotel.getAmenities() != null && hotelInfo.getAmenities() != null) {
                hotel.setAmenities(new HashSet<>(hotelInfo.getAmenities().stream().map(a -> toAmenity().apply(a)).toList()));
            }
            return hotel;
        };
    }
}
