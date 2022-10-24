package mx.cacho.choice.soapws.endpoint;

import mx.cacho.choice.soapws.endpoint.exception.HotelNotFoundException;
import mx.cacho.choice.soapws.endpoint.exception.SenderException;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.AddAmenitiesToHotelRequest;
import mx.cacho.choice.soapws.schema.AmenityInfo;
import mx.cacho.choice.soapws.schema.CreateHotelRequest;
import mx.cacho.choice.soapws.schema.DeleteHotelRequest;
import mx.cacho.choice.soapws.schema.GetHotelRequest;
import mx.cacho.choice.soapws.schema.GetHotelResponse;
import mx.cacho.choice.soapws.schema.GetHotelsByNameRequest;
import mx.cacho.choice.soapws.schema.HotelInfo;
import mx.cacho.choice.soapws.schema.UpdateHotelRequest;
import mx.cacho.choice.soapws.service.AmenityServiceImpl;
import mx.cacho.choice.soapws.service.HotelServiceImpl;
import mx.cacho.choice.soapws.test.util.TestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit Tests; with no spring boot configuration.
 */
@ExtendWith(MockitoExtension.class)
class HotelEndpointTest {

    @InjectMocks
    HotelEndpoint hotelEndpoint;

    @Mock
    HotelServiceImpl hotelServiceMock;

    @Mock
    AmenityServiceImpl amenityServiceMock;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class GetHotel {
        @Test
        void returnsResponseWithHotelInfo_whenUsingRequestWithExistingHotelId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);

            Long amenityId = nextLong(1, Long.MAX_VALUE);
            String amenityName = randomAlphanumeric(50);
            String amenityDescription = randomAlphanumeric(50);
            Amenity hotelAmenity = generateAmenity(amenityId, amenityName, amenityDescription);
            Hotel existingHotel = generateHotel(id, name, description, country, state, city, zipCode, address, Set.of(hotelAmenity));
            when(hotelServiceMock.getHotel(id)).thenReturn(Optional.of(existingHotel));

            //when
            GetHotelRequest request = new GetHotelRequest();
            request.setId(id);
            GetHotelResponse response = hotelEndpoint.getHotel(request);

            //then
            HotelInfo expectedHotelInfo = new HotelInfo();
            expectedHotelInfo.setHotelId(id);
            expectedHotelInfo.setName(name);
            expectedHotelInfo.setDescription(description);
            expectedHotelInfo.setCountry(country);
            expectedHotelInfo.setState(state);
            expectedHotelInfo.setCity(city);
            expectedHotelInfo.setZipCode(zipCode);
            expectedHotelInfo.setAddress(address);
            assertTrue(EqualsBuilder.reflectionEquals(expectedHotelInfo, response.getHotel(), "amenities"));

            AmenityInfo expectedAmenityInfo = new AmenityInfo();
            expectedAmenityInfo.setAmenityId(amenityId);
            expectedAmenityInfo.setName(amenityName);
            expectedAmenityInfo.setDescription(amenityDescription);
            assertTrue(EqualsBuilder.reflectionEquals(expectedAmenityInfo, response.getHotel().getAmenities().get(0)));

            verify(hotelServiceMock, times(1)).getHotel(id);
            verifyNoMoreInteractions(hotelServiceMock);
        }

        @Test
        void throws_whenUsingRequestWithNonExistingHotelId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);
            when(hotelServiceMock.getHotel(id)).thenReturn(Optional.empty());

            //when
            GetHotelRequest request = new GetHotelRequest();
            request.setId(id);
            Executable endpointCall = () -> hotelEndpoint.getHotel(request);

            //then
            assertThrows(HotelNotFoundException.class, endpointCall);
            verify(hotelServiceMock, times(1)).getHotel(id);
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class GetAllHotels {
        @Test
        void returnsHotelsInResponse_whenServiceReturnsHotels() {
            //given
            List<Long> existingHotelIds = List.of(nextLong(1, Long.MAX_VALUE), nextLong(1, Long.MAX_VALUE), nextLong(1, Long.MAX_VALUE));
            List<Hotel> existingHotels = existingHotelIds.stream().map(TestUtils::generateHotel).toList();
            when(hotelServiceMock.getAllHotels()).thenReturn(existingHotels);

            //when
            var response = hotelEndpoint.getAllHotels();

            //then
            List<HotelInfo> hotelInfoList = existingHotels.stream().map(h -> {
                HotelInfo hotelInfo = new HotelInfo();
                hotelInfo.setHotelId(h.getHotelId());
                hotelInfo.setDescription(h.getDescription());
                hotelInfo.setName(h.getName());
                hotelInfo.setCountry(h.getCountry());
                hotelInfo.setState(h.getState());
                hotelInfo.setCity(h.getCity());
                hotelInfo.setZipCode(h.getZipCode());
                hotelInfo.setAddress(h.getAddress());
                return hotelInfo;
            }).toList();
            assertEquals(existingHotels.size(), response.getHotel().size());
            for (int i = 0; i < response.getHotel().size(); i++) {
                assertTrue(EqualsBuilder.reflectionEquals(hotelInfoList.get(i), response.getHotel().get(i), "amenities"));
            }

            verify(hotelServiceMock, times(1)).getAllHotels();
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class GetHotelsByName {
        @Test
        void returnsHotelsInResponse_whenServiceReturnsHotels() {
            //given
            String nameSuffix = "name";
            List<Long> existingHotelIds = List.of(nextLong(1, Long.MAX_VALUE), nextLong(1, Long.MAX_VALUE), nextLong(1, Long.MAX_VALUE));
            List<Hotel> existingHotels = existingHotelIds.stream().map(TestUtils::generateHotel).toList();
            when(hotelServiceMock.getHotelsByName(nameSuffix)).thenReturn(existingHotels);

            //when
            GetHotelsByNameRequest request = new GetHotelsByNameRequest();
            request.setName(nameSuffix);
            var response = hotelEndpoint.getHotelsByName(request);

            //then
            List<HotelInfo> hotelInfoList = existingHotels.stream().map(h -> {
                HotelInfo hotelInfo = new HotelInfo();
                hotelInfo.setHotelId(h.getHotelId());
                hotelInfo.setDescription(h.getDescription());
                hotelInfo.setName(h.getName());
                hotelInfo.setCountry(h.getCountry());
                hotelInfo.setState(h.getState());
                hotelInfo.setCity(h.getCity());
                hotelInfo.setZipCode(h.getZipCode());
                hotelInfo.setAddress(h.getAddress());
                return hotelInfo;
            }).toList();
            assertEquals(existingHotels.size(), response.getHotel().size());
            for (int i = 0; i < response.getHotel().size(); i++) {
                assertTrue(EqualsBuilder.reflectionEquals(hotelInfoList.get(i), response.getHotel().get(i), "amenities"));
            }

            verify(hotelServiceMock, times(1)).getHotelsByName(nameSuffix);
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class CreateHotel {
        @Test
        void returnsResponseWithHotelInfo_whenUsingRequestWithNewHotel() {
            //given
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(50);
            String state = randomAlphanumeric(50);
            String zipCode = randomAlphanumeric(10);
            String city = randomAlphanumeric(50);
            String address = randomAlphanumeric(50);
            Hotel newHotel = generateHotel(0L, name, description, country, state, city, zipCode, address, Collections.emptySet());

            Long createdHotelId = nextLong(1, Long.MAX_VALUE);
            Hotel createdHotel = generateHotel(createdHotelId, name, description, country, state, city, zipCode, address, Collections.emptySet());
            when(hotelServiceMock.createHotel(argThat(matcher ->
                    EqualsBuilder.reflectionEquals(matcher, newHotel, "amenities") &&
                            matcher.getAmenities().equals(newHotel.getAmenities()))))
                    .thenReturn(createdHotel);

            CreateHotelRequest request = new CreateHotelRequest();
            request.setName(name);
            request.setDescription(description);
            request.setCountry(country);
            request.setState(state);
            request.setCity(city);
            request.setZipCode(zipCode);
            request.setAddress(address);

            //when
            GetHotelResponse response = hotelEndpoint.createHotel(request);

            //then
            HotelInfo expectedHotelInfo = new HotelInfo();
            expectedHotelInfo.setHotelId(createdHotelId);
            expectedHotelInfo.setName(name);
            expectedHotelInfo.setDescription(description);
            expectedHotelInfo.setCountry(country);
            expectedHotelInfo.setState(state);
            expectedHotelInfo.setCity(city);
            expectedHotelInfo.setZipCode(zipCode);
            expectedHotelInfo.setAddress(address);
            assertTrue(EqualsBuilder.reflectionEquals(expectedHotelInfo, response.getHotel(), "amenities"));
            verify(hotelServiceMock, times(1)).createHotel(newHotel);
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class UpdateHotel {
        @Test
        void returnsResponseWithHotelInfo_whenUsingRequestWithExistingHotel() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(50);
            String state = randomAlphanumeric(50);
            String zipCode = randomAlphanumeric(10);
            String city = randomAlphanumeric(50);
            String address = randomAlphanumeric(50);
            Hotel updatedHotel = generateHotel(id, name, description, country, state, city, zipCode, address, Collections.emptySet());
            when(hotelServiceMock.updateHotel(argThat(matcher ->
                    EqualsBuilder.reflectionEquals(matcher, updatedHotel, "amenities") &&
                            matcher.getAmenities().equals(updatedHotel.getAmenities()))))
                    .thenReturn(updatedHotel);

            //when
            UpdateHotelRequest request = new UpdateHotelRequest();
            request.setHotelId(id);
            request.setName(name);
            request.setDescription(description);
            request.setCountry(country);
            request.setCity(city);
            request.setState(state);
            request.setZipCode(zipCode);
            request.setCity(city);
            request.setAddress(address);
            GetHotelResponse response = hotelEndpoint.updateHotel(request);

            //then
            HotelInfo expectedHotelInfo = new HotelInfo();
            expectedHotelInfo.setHotelId(id);
            expectedHotelInfo.setName(name);
            expectedHotelInfo.setDescription(description);
            expectedHotelInfo.setCountry(country);
            expectedHotelInfo.setState(state);
            expectedHotelInfo.setCity(city);
            expectedHotelInfo.setZipCode(zipCode);
            expectedHotelInfo.setAddress(address);
            assertTrue(EqualsBuilder.reflectionEquals(expectedHotelInfo, response.getHotel(), "amenities"));
            verify(hotelServiceMock, times(1)).updateHotel(updatedHotel);
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class DeleteHotel {
        @Test
        void makesDeleteCallInService_withEitherExistingOrNonExistingHotelId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);

            //when
            DeleteHotelRequest request = new DeleteHotelRequest();
            request.setHotelId(id);
            hotelEndpoint.deleteHotel(request);

            //then
            verify(hotelServiceMock, times(1)).deleteHotel(id);
            verifyNoMoreInteractions(hotelServiceMock);
        }
    }

    @Nested
    class AddAmenitiesToHotel {

        @Test
        void returnsResponseWithHotelAndAmenityInfo_whenUsingExistingHotelAndAmenities() {
            //given
            Long firstAmenityId = nextLong(1, Long.MAX_VALUE);
            Long secondAmenityId = nextLong(1, Long.MAX_VALUE);
            Long thirdAmenityId = nextLong(1, Long.MAX_VALUE);
            Amenity firstAmenity = generateAmenity(firstAmenityId);
            Amenity secondAmenity = generateAmenity(secondAmenityId);
            Amenity thirdAmenity = generateAmenity(thirdAmenityId);
            List<Long> amenityIdList = List.of(firstAmenityId, secondAmenityId, thirdAmenityId);
            List<Amenity> amenitiesList = List.of(firstAmenity, secondAmenity, thirdAmenity);
            when(amenityServiceMock.getAmenities(amenityIdList))
                    .thenReturn(amenitiesList);

            Long hotelId = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            String country = randomAlphanumeric(20);
            String state = randomAlphanumeric(20);
            String city = randomAlphanumeric(20);
            String zipCode = randomAlphanumeric(5);
            String address = randomAlphanumeric(20);

            Hotel updatedHotel = generateHotel(hotelId, name, description, country, state, city, zipCode, address, new HashSet<>(amenitiesList));
            when(hotelServiceMock.addAmenitiesToHotel(hotelId, amenitiesList))
                    .thenReturn(updatedHotel);

            //when
            AddAmenitiesToHotelRequest request = new AddAmenitiesToHotelRequest();
            request.setHotelId(hotelId);
            request.getAmenityIds().addAll(amenityIdList);
            GetHotelResponse response = hotelEndpoint.addAmenitiesToHotel(request);

            //then
            HotelInfo expectedHotelInfo = new HotelInfo();
            expectedHotelInfo.setHotelId(hotelId);
            expectedHotelInfo.setName(name);
            expectedHotelInfo.setDescription(description);
            expectedHotelInfo.setCountry(country);
            expectedHotelInfo.setState(state);
            expectedHotelInfo.setCity(city);
            expectedHotelInfo.setZipCode(zipCode);
            expectedHotelInfo.setAddress(address);
            assertTrue(EqualsBuilder.reflectionEquals(expectedHotelInfo, response.getHotel(), "amenities"));

            response.getHotel().getAmenities().forEach(amenityInfo -> {
                assertTrue(amenityIdList.contains(amenityInfo.getAmenityId()));
            });
        }

        @Test
        void throwsSenderException_whenUsingNonExistingAmenities() {
            //given
            Long firstAmenityId = nextLong(1, Long.MAX_VALUE);
            Long secondAmenityId = nextLong(1, Long.MAX_VALUE);
            Long thirdAmenityId = nextLong(1, Long.MAX_VALUE);
            Amenity firstAmenity = generateAmenity(firstAmenityId);
            Amenity secondAmenity = generateAmenity(secondAmenityId);
            List<Long> amenityIdListContainingNonExistingAmenity = List.of(firstAmenityId, secondAmenityId, thirdAmenityId);
            List<Amenity> amenitiesList = List.of(firstAmenity, secondAmenity);
            when(amenityServiceMock.getAmenities(amenityIdListContainingNonExistingAmenity))
                    .thenReturn(amenitiesList);

            //when
            Long hotelId = nextLong(1, Long.MAX_VALUE);
            AddAmenitiesToHotelRequest request = new AddAmenitiesToHotelRequest();
            request.setHotelId(hotelId);
            request.getAmenityIds().addAll(amenityIdListContainingNonExistingAmenity);
            Executable endpointCall = () -> hotelEndpoint.addAmenitiesToHotel(request);

            //then
            assertThrows(SenderException.class, endpointCall);
        }
    }

    @Nested
    class RemoveAmenitiesFromHotel {
    }

}