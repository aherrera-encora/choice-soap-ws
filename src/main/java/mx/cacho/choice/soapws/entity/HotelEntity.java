package mx.cacho.choice.soapws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "hotels")
public class HotelEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hotel_id")
    private long hotelId;
    @Basic
    @Column(name = "country")
    private String country;
    @Basic
    @Column(name = "state")
    private String state;
    @Basic
    @Column(name = "city")
    private String city;
    @Basic
    @Column(name = "zipcode")
    private String zipcode;
    @Basic
    @Column(name = "address1")
    private String address1;
    @Basic
    @Column(name = "address2")
    private String address2;
    @Basic
    @Column(name = "address3")
    private String address3;
    @Basic
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Basic
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
