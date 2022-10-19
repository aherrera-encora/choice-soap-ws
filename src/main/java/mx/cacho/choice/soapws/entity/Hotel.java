package mx.cacho.choice.soapws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "hotels")
public class Hotel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hotel_id")
    private long hotelId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
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
    @Column(name = "zip_code")
    private String zipCode;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "rating")
    private BigDecimal rating;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinTable(name = "hotel_amenities", joinColumns = {@JoinColumn(name = "hotel_id")}, inverseJoinColumns = {@JoinColumn(name = "amenity_id")})
    private Set<Amenity> amenities = new HashSet<>();

    public void addAmenities(Collection<Amenity> amenities) {
        if(this.amenities == null){
            this.amenities = new HashSet<>();
        }
        this.amenities.addAll(amenities);
    }

    public void removeAmenities(Collection<Long> amenityIds) {
        if(this.amenities == null || this.amenities.isEmpty()){
            return;
        }
        Set<Amenity> amenitiesToRemove = this.amenities.stream().filter(a -> amenityIds.contains(a.getAmenityId())).collect(Collectors.toSet());
        if (!amenities.isEmpty()) {
            this.amenities.removeAll(amenitiesToRemove);
        }
    }
}
