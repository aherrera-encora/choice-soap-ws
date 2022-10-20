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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "amenities")
public class Amenity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "amenity_id")
    @Min(value = 0L, message = "Id cannot be negative.")
    private long amenityId;

    @Basic
    @Column(name = "name")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters.")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @Basic
    @Column(name = "description")
    @Size(max = 500, message = "Description must be between 0 and 500 characters.")
    private String description;
}
