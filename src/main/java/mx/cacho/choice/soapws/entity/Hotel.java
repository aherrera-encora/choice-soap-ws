package mx.cacho.choice.soapws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "hotels")
public class Hotel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hotel_id")
    @Min(value = 0L, message = "Id cannot be negative.")
    private long hotelId;
    @Basic
    @Column(name = "name")
    @Size(max = 200, message = "Name must be between 1 and 200 characters.")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;
    @Basic
    @Column(name = "description")
    @Size(max = 500, message = "Description must be between 0 and 500 characters.")
    @NotEmpty(message = "Description cannot be empty.")
    private String description;
    @Basic
    @Column(name = "country")
    @Size(max = 50, message = "Country must be between 0 and 50 characters.")
    @NotEmpty(message = "Country cannot be empty.")
    private String country;
    @Basic
    @Column(name = "state")
    @Size(max = 50, message = "State must be between 0 and 50 characters.")
    @NotEmpty(message = "State cannot be empty.")
    private String state;
    @Basic
    @Column(name = "city")
    @Size(max = 50, message = "City must be between 0 and 50 characters.")
    @NotEmpty(message = "City cannot be empty.")
    private String city;
    @Basic
    @Column(name = "zip_code")
    @Size(max = 10, message = "Zip Code must be between 0 and 10 characters.")
    @NotEmpty(message = "Zip Code cannot be empty.")
    private String zipCode;
    @Basic
    @Column(name = "address")
    @Size(max = 300, message = "Address must be between 0 and 300 characters.")
    @NotEmpty(message = "Address cannot be empty.")
    private String address;
    @Basic
    @Column(name = "rating")
    @DecimalMax(value = "10.0", message = "Rating must be 10.0 or lower.")
    @DecimalMin(value = "0.0", message = "Rating must be 0.0 or higher.")
    private BigDecimal rating;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinTable(name = "hotel_amenities", joinColumns = {@JoinColumn(name = "hotel_id")}, inverseJoinColumns = {@JoinColumn(name = "amenity_id")})
    private Set<Amenity> amenities = new HashSet<>();

    public void addAmenities(Collection<Amenity> amenities) {
        if (this.amenities == null) {
            this.amenities = new HashSet<>();
        }
        this.amenities.addAll(amenities);
    }

    public void removeAmenities(Collection<Long> amenityIds) {
        if (this.amenities == null || this.amenities.isEmpty()) {
            return;
        }
        Set<Amenity> amenitiesToRemove = this.amenities.stream().filter(a -> amenityIds.contains(a.getAmenityId())).collect(Collectors.toSet());
        if (!amenities.isEmpty()) {
            this.amenities.removeAll(amenitiesToRemove);
        }
    }
}
