package mx.cacho.choice.soapws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "hotel_amenities")
@IdClass(HotelAmenityPK.class)
public class HotelAmenity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hotel_id")
    private long hotelId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "amenity_id")
    private long amenityId;
}
