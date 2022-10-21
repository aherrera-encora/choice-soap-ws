package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.repository.AmenityRepository;
import mx.cacho.choice.soapws.repository.HotelRepository;
import mx.cacho.choice.soapws.service.exception.IllegalServiceOperationException;
import mx.cacho.choice.soapws.test.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static mx.cacho.choice.soapws.test.util.TestUtils.generateAmenity;
import static mx.cacho.choice.soapws.test.util.TestUtils.generateHotel;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit Tests; with no spring boot configuration.
 * TODO: Use integration tests to verify Validation configuration.
 */
@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelRepository hotelRepositoryMock;

    @Mock
    private AmenityRepository amenityRepositoryMock;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class GetHotel {
        @Test
        void returnsHotel_whenUsingExistingId() {
            //given
            Long existingId = nextLong(1, Long.MAX_VALUE);
            Hotel existingHotel = generateHotel(existingId);
            when(hotelRepositoryMock.findById(existingId))
                    .thenReturn(Optional.of(existingHotel));

            //when
            Optional<Hotel> fetchedHotel = hotelService.getHotel(existingId);

            //then
            assertTrue(fetchedHotel.isPresent());
            assertEquals(existingHotel, fetchedHotel.get());
            verify(hotelRepositoryMock, times(1)).findById(existingId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void returnsEmptyOptional_whenUsingNonExistingId() {
            //given
            Long nonExistingId = nextLong(1, Long.MAX_VALUE);
            when(hotelRepositoryMock.findById(nonExistingId))
                    .thenReturn(Optional.empty());

            //when
            Optional<Hotel> fetchedHotel = hotelService.getHotel(nonExistingId);

            //then
            assertTrue(fetchedHotel.isEmpty());
            verify(hotelRepositoryMock, times(1)).findById(nonExistingId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class GetHotels {
        @Test
        void returnsHotelList_whenUsingExistingIds() {
            //given
            List<Long> existingIds = List.of(
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE));
            List<Hotel> existingHotels = existingIds.stream()
                    .map(TestUtils::generateHotel)
                    .toList();
            when(hotelRepositoryMock.findAllById(existingIds))
                    .thenReturn(existingHotels);

            //when
            List<Hotel> fetchedHotels = hotelService.getHotels(existingIds);

            //then
            assertEquals(3, fetchedHotels.size());
            assertTrue(fetchedHotels.containsAll(existingHotels));
            verify(hotelRepositoryMock, times(1)).findAllById(existingIds);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void returnsEmptyList_whenUsingNonExistingIds() {
            //given
            List<Long> nonExistingIds = List.of(
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE));
            when(hotelRepositoryMock.findAllById(nonExistingIds))
                    .thenReturn(Collections.emptyList());

            //when
            List<Hotel> fetchedHotels = hotelService.getHotels(nonExistingIds);

            //then
            assertTrue(fetchedHotels.isEmpty());
            verify(hotelRepositoryMock, times(1)).findAllById(nonExistingIds);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class GetAllHotels {
        @Test
        void returnsAllHotels_whenRepoIsPopulated() {
            //given
            List<Hotel> storedHotels = List.of(
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)));
            when(hotelRepositoryMock.findAll())
                    .thenReturn(storedHotels);

            //when
            List<Hotel> fetchedHotels = hotelService.getAllHotels();

            //then
            assertEquals(4, fetchedHotels.size());
            assertTrue(fetchedHotels.containsAll(storedHotels));
            verify(hotelRepositoryMock, times(1)).findAll();
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void returnsEmptyList_whenRepoIsEmpty() {
            //given
            when(hotelRepositoryMock.findAll())
                    .thenReturn(Collections.emptyList());

            //when
            List<Hotel> fetchedHotels = hotelService.getAllHotels();

            //then
            assertTrue(fetchedHotels.isEmpty());
            verify(hotelRepositoryMock, times(1)).findAll();
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class GetHotelsByName {
        @Test
        void returnsHotels_whenRepoIsPopulated() {
            //given
            String name = randomAlphanumeric(50);
            List<Hotel> storedHotels = List.of(
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)),
                    generateHotel(nextLong(1, Long.MAX_VALUE)));
            when(hotelRepositoryMock.getHotelsByNameContainingIgnoringCase(name))
                    .thenReturn(storedHotels);

            //when
            List<Hotel> fetchedHotels = hotelService.getHotelsByName(name);

            //then
            assertEquals(4, fetchedHotels.size());
            assertTrue(fetchedHotels.containsAll(storedHotels));
        }

        @Test
        void returnsEmptyList_whenRepoIsEmpty() {
            //given
            String name = randomAlphanumeric(50);
            when(hotelRepositoryMock.getHotelsByNameContainingIgnoringCase(name))
                    .thenReturn(Collections.emptyList());

            //when
            List<Hotel> fetchedHotels = hotelService.getHotelsByName(name);

            //then
            assertTrue(fetchedHotels.isEmpty());
            verify(hotelRepositoryMock, times(1)).getHotelsByNameContainingIgnoringCase(name);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class CreateHotel {
        @Test
        void createsHotel_whenUsingValidHotel() {
            //given
            Long validCreateId = 0L;
            Long persistedHotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);
            Set<Amenity> amenities = Collections.emptySet();
            Hotel newHotel = generateHotel(validCreateId, name, description, country, state, city, zipCode, address, amenities);
            Hotel expectedHotel = generateHotel(persistedHotelId, name, description, country, state, city, zipCode, address, amenities);
            when(hotelRepositoryMock.save(newHotel))
                    .thenReturn(expectedHotel);

            //when
            Hotel createdHotel = hotelService.createHotel(newHotel);

            //then
            assertEquals(expectedHotel, createdHotel);
            verify(hotelRepositoryMock, times(1)).save(newHotel);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void throws_whenUsingHotelWithSetId() {
            //given
            Long invalidHotelCreateId = nextLong(1, Long.MAX_VALUE);
            Hotel newHotel = generateHotel(invalidHotelCreateId);

            //when
            Executable serviceCall = () -> hotelService.createHotel(newHotel);

            //then should throw a IllegalServiceOperationException
            assertThrows(IllegalServiceOperationException.class, serviceCall);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class UpdateHotel {
        @Test
        void updatesHotel_whenUsingValidHotel() {
            //given
            Long hotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);
            Set<Amenity> amenities = Collections.emptySet();
            Hotel newHotel = generateHotel(hotelId, name, description, country, state, city, zipCode, address, amenities);
            Hotel expectedHotel = generateHotel(hotelId, name, description, country, state, city, zipCode, address, amenities);
            when(hotelRepositoryMock.existsById(hotelId))
                    .thenReturn(true);
            when(hotelRepositoryMock.save(newHotel))
                    .thenReturn(expectedHotel);

            //when
            Hotel updatedHotel = hotelService.updateHotel(newHotel);

            //then
            assertEquals(expectedHotel, updatedHotel);
            verify(hotelRepositoryMock, times(1)).save(newHotel);
            verify(hotelRepositoryMock, times(1)).existsById(hotelId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void throws_whenUsingNonExistingHotel() {
            //given
            Long nonExistingHotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            Hotel newHotel = generateHotel(nonExistingHotelId, name, description);
            when(hotelRepositoryMock.existsById(nonExistingHotelId))
                    .thenReturn(false);

            //when
            Executable serviceCall = () -> hotelService.updateHotel(newHotel);

            //then should throw a IllegalServiceOperationException
            assertThrows(IllegalServiceOperationException.class, serviceCall);
            verify(hotelRepositoryMock, times(1)).existsById(nonExistingHotelId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class DeleteHotel {

        @Test
        void makesDeleteCallInRepo_withEitherExistingOrNonExistingHotelId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);

            //when
            hotelService.deleteHotel(id);

            //then
            verify(hotelRepositoryMock, times(1)).deleteById(id);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class AddAmenitiesToHotel {

        @Test
        void addsAmenities_whenUsingExistingHotel() {
            //given
            Long hotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);

            Amenity firstAmenity = generateAmenity();
            Amenity secondAmenity = generateAmenity();
            Amenity thirdAmenity = generateAmenity();
            Set<Amenity> hotelAmenities = new HashSet<>(Set.of(firstAmenity, secondAmenity));
            Hotel existingHotelWithAmenities = generateHotel(hotelId, name, description, country, state, city, zipCode, address, hotelAmenities);

            when(hotelRepositoryMock.findById(hotelId))
                    .thenReturn(Optional.of(existingHotelWithAmenities));
            when(hotelRepositoryMock.save(existingHotelWithAmenities))
                    .thenReturn(existingHotelWithAmenities);

            //when
            Set<Amenity> amenitiesToAdd = Set.of(thirdAmenity);
            Hotel updatedHotel = hotelService.addAmenitiesToHotel(hotelId, amenitiesToAdd);

            //then
            Set<Amenity> mergedAmenities = Set.of(firstAmenity, secondAmenity, thirdAmenity);
            Hotel hotelWithAddedAmenities = generateHotel(hotelId, name, description, country, state, city, zipCode, address, mergedAmenities);
            assertEquals(hotelWithAddedAmenities, updatedHotel);
            verify(hotelRepositoryMock, times(1)).findById(hotelId);
            verify(hotelRepositoryMock, times(1)).save(existingHotelWithAmenities);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void throws_whenUsingNonExistingHotel() {
            //given
            Long nonExistingHotelId = nextLong(1, Long.MAX_VALUE);
            when(hotelRepositoryMock.findById(nonExistingHotelId))
                    .thenReturn(Optional.empty());

            //when
            Executable serviceCall = () -> hotelService.addAmenitiesToHotel(nonExistingHotelId, Collections.emptyList());

            //then should throw a IllegalServiceOperationException
            assertThrows(IllegalServiceOperationException.class, serviceCall);
            verify(hotelRepositoryMock, times(1)).findById(nonExistingHotelId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }

    @Nested
    class RemoveAmenitiesFromHotel {

        @Test
        void removesAmenities_whenUsingExistingHotel() {
            //given
            Long hotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);

            Amenity firstAmenity = generateAmenity();
            Amenity secondAmenity = generateAmenity();
            Amenity thirdAmenity = generateAmenity();
            Set<Amenity> hotelAmenities = new HashSet<>(Set.of(firstAmenity, secondAmenity, thirdAmenity));
            Hotel existingHotel = generateHotel(hotelId, name, description, country, state, city, zipCode, address, hotelAmenities);

            when(hotelRepositoryMock.findById(hotelId))
                    .thenReturn(Optional.of(existingHotel));
            when(hotelRepositoryMock.save(existingHotel))
                    .thenReturn(existingHotel);

            //when
            Set<Amenity> amenitiesToRemove = Set.of(firstAmenity, secondAmenity);
            Hotel updatedHotel = hotelService.removeAmenitiesFromHotel(hotelId, amenitiesToRemove.stream().map(Amenity::getAmenityId).toList());

            //then
            Hotel hotelWithRemovedAmenities = generateHotel(hotelId, name, description, country, state, city, zipCode, address, Set.of(thirdAmenity));
            assertEquals(hotelWithRemovedAmenities, updatedHotel);
            verify(hotelRepositoryMock, times(1)).save(existingHotel);
            verify(hotelRepositoryMock, times(1)).findById(hotelId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }

        @Test
        void throws_whenUsingNonExistingHotel() {
            //given
            Long nonExistingHotelId = nextLong(1, Long.MAX_VALUE);
            when(hotelRepositoryMock.findById(nonExistingHotelId))
                    .thenReturn(Optional.empty());

            //when
            Executable serviceCall = () -> hotelService.removeAmenitiesFromHotel(nonExistingHotelId, Collections.emptyList());

            //then should throw a IllegalServiceOperationException
            assertThrows(IllegalServiceOperationException.class, serviceCall);
            verify(hotelRepositoryMock, times(1)).findById(nonExistingHotelId);
            verifyNoMoreInteractions(hotelRepositoryMock);
        }
    }
}
