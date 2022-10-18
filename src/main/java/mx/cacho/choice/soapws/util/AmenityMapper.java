package mx.cacho.choice.soapws.util;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.AmenityInfo;
import mx.cacho.choice.soapws.schema.HotelInfo;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.function.Function;

public class AmenityMapper {

    private AmenityMapper(){
        throw new IllegalStateException("AmenityMapper is non-instantiable");
    }

    public static Function<Amenity, AmenityInfo> toAmenityInfo() {
        return amenity -> {
            AmenityInfo amenityInfo = new AmenityInfo();
            BeanUtils.copyProperties(amenity, amenityInfo);
            return amenityInfo;
        };
    }

    public static Function<AmenityInfo, Amenity> toAmenity() {
        return amenityInfo -> {
            Amenity amenity = new Amenity();
            BeanUtils.copyProperties(amenityInfo, amenity);
            return amenity;
        };
    }
}
