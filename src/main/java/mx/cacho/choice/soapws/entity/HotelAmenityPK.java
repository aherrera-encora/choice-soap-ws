package mx.cacho.choice.soapws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HotelAmenityPK implements Serializable {
    @Column(name = "hotel_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hotelId;
    @Column(name = "amenity_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long amenityId;
}
