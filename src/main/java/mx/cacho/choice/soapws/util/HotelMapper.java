package mx.cacho.choice.soapws.util;

import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.HotelInfo;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.function.Function;

public class HotelMapper {

    private HotelMapper() {
        throw new IllegalStateException("HotelMapper is non-instantiable");
    }

    public static HotelInfo toHotelInfo(Hotel hotel) {
        HotelInfo hotelInfo = new HotelInfo();
        BeanUtils.copyProperties(hotel, hotelInfo);
        if (hotel.getAmenities() != null) {
            hotelInfo.getAmenities().addAll(hotel.getAmenities().stream().map(AmenityMapper::toAmenityInfo).toList());
        }
        return hotelInfo;
    }

    public static Hotel toHotel(HotelInfo hotelInfo) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelInfo, hotel);
        if (hotel.getAmenities() != null) {
            hotel.setAmenities(new HashSet<>(hotelInfo.getAmenities().stream().map(AmenityMapper::toAmenity).toList()));
        }
        return hotel;
    }
}
