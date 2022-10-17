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
@Table(name = "hotel_ratings")
public class HotelRatingEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "rating_id")
    private long ratingId;
    @Basic
    @Column(name = "hotel_id")
    private Long hotelId;
    @Basic
    @Column(name = "score")
    private Integer score;
    @Basic
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Basic
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
