package mx.cacho.choice.soapws.util;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.schema.AmenityInfo;
import org.springframework.beans.BeanUtils;

public class AmenityMapper {

    private AmenityMapper() {
        throw new IllegalStateException("AmenityMapper is non-instantiable");
    }

    public static AmenityInfo toAmenityInfo(Amenity amenity) {
        AmenityInfo amenityInfo = new AmenityInfo();
        BeanUtils.copyProperties(amenity, amenityInfo);
        return amenityInfo;
    }

    public static Amenity toAmenity(AmenityInfo amenityInfo) {
        Amenity amenity = new Amenity();
        BeanUtils.copyProperties(amenityInfo, amenity);
        return amenity;
    }
}
