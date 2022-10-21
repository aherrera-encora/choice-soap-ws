package mx.cacho.choice.soapws.service;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.repository.AmenityRepository;
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
import java.util.List;
import java.util.Optional;

import static mx.cacho.choice.soapws.test.util.TestUtils.generateAmenity;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
class AmenityServiceImplTest {

    @InjectMocks
    private AmenityServiceImpl amenityService;

    @Mock
    private AmenityRepository amenityRepositoryMock;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class GetAmenity {
        @Test
        void returnsAmenity_whenUsingExistingId() {
            //given
            Long existingId = nextLong(1, Long.MAX_VALUE);
            Amenity existingAmenity = generateAmenity(existingId);
            when(amenityRepositoryMock.findById(existingId))
                    .thenReturn(Optional.of(existingAmenity));

            //when
            Optional<Amenity> fetchedAmenity = amenityService.getAmenity(existingId);

            //then
            assertTrue(fetchedAmenity.isPresent());
            assertEquals(existingAmenity, fetchedAmenity.get());
            verify(amenityRepositoryMock, times(1)).findById(existingId);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }

        @Test
        void returnsEmptyOptional_whenUsingNonExistingId() {
            //given
            Long nonExistingId = nextLong(1, Long.MAX_VALUE);
            when(amenityRepositoryMock.findById(nonExistingId))
                    .thenReturn(Optional.empty());

            //when
            Optional<Amenity> fetchedAmenity = amenityService.getAmenity(nonExistingId);

            //then
            assertFalse(fetchedAmenity.isPresent());
            verify(amenityRepositoryMock, times(1)).findById(nonExistingId);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }
    }

    @Nested
    class GetAmenities {
        @Test
        void returnsAmenityList_whenUsingExistingIds() {
            //given
            List<Long> existingIds = List.of(
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE));
            List<Amenity> existingAmenities = existingIds.stream()
                    .map(TestUtils::generateAmenity)
                    .toList();
            when(amenityRepositoryMock.findAllById(existingIds))
                    .thenReturn(existingAmenities);

            //when
            List<Amenity> fetchedAmenities = amenityService.getAmenities(existingIds);

            //then
            assertEquals(3, fetchedAmenities.size());
            assertTrue(fetchedAmenities.containsAll(existingAmenities));
            verify(amenityRepositoryMock, times(1)).findAllById(existingIds);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }

        @Test
        void returnsEmptyList_whenUsingNonExistingIds() {
            //given
            List<Long> nonExistingIds = List.of(
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE));
            when(amenityRepositoryMock.findAllById(nonExistingIds))
                    .thenReturn(Collections.emptyList());

            //when
            List<Amenity> fetchedAmenities = amenityService.getAmenities(nonExistingIds);

            //then
            assertTrue(fetchedAmenities.isEmpty());
            verify(amenityRepositoryMock, times(1)).findAllById(nonExistingIds);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }
    }

    @Nested
    class GetAllAmenities {
        @Test
        void returnsAllAmenitiesList_whenRepoIsPopulated() {
            //given
            List<Amenity> storedAmenities = List.of(
                    generateAmenity(nextLong(1, Long.MAX_VALUE)),
                    generateAmenity(nextLong(1, Long.MAX_VALUE)),
                    generateAmenity(nextLong(1, Long.MAX_VALUE)),
                    generateAmenity(nextLong(1, Long.MAX_VALUE)));
            when(amenityRepositoryMock.findAll())
                    .thenReturn(storedAmenities);

            //when
            List<Amenity> fetchedAmenities = amenityService.getAllAmenities();

            //then
            assertEquals(4, fetchedAmenities.size());
            assertTrue(fetchedAmenities.containsAll(storedAmenities));
            verify(amenityRepositoryMock, times(1)).findAll();
            verifyNoMoreInteractions(amenityRepositoryMock);
        }

        @Test
        void returnsEmptyList_whenRepoIsEmpty() {
            //given
            List<Amenity> storedAmenities = Collections.emptyList();
            when(amenityRepositoryMock.findAll())
                    .thenReturn(storedAmenities);

            //when
            List<Amenity> fetchedAmenities = amenityService.getAllAmenities();

            //then
            assertTrue(fetchedAmenities.isEmpty());
            verify(amenityRepositoryMock, times(1)).findAll();
            verifyNoMoreInteractions(amenityRepositoryMock);
        }
    }

    @Nested
    class CreateAmenity {
        @Test
        void createsAmenity_whenUsingValidAmenity() {
            //given
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            Amenity newAmenity = generateAmenity(0L, name, description);
            when(amenityRepositoryMock.save(newAmenity))
                    .thenReturn(generateAmenity(nextLong(1, Long.MAX_VALUE), name, description));

            //when
            Amenity createdAmenity = amenityService.createAmenity(newAmenity);

            //then
            assertTrue(createdAmenity.getAmenityId() > 0);
            assertEquals(name, createdAmenity.getName());
            assertEquals(description, createdAmenity.getDescription());
            verify(amenityRepositoryMock, times(1)).save(newAmenity);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }

        @Test
        void throws_whenUsingAmenityWithSetId() {
            //given
            Amenity newAmenity = generateAmenity(1L, randomAlphanumeric(100), randomAlphanumeric(100));

            //when
            Executable serviceCall = () -> amenityService.createAmenity(newAmenity);

            //then should throw a IllegalServiceOperationException
            assertThrows(IllegalServiceOperationException.class, serviceCall);
            verifyNoMoreInteractions(amenityRepositoryMock);
        }
    }
}
