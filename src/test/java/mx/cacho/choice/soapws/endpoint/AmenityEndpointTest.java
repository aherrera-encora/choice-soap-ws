package mx.cacho.choice.soapws.endpoint;

import mx.cacho.choice.soapws.endpoint.exception.AmenityNotFoundException;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.schema.AmenityInfo;
import mx.cacho.choice.soapws.schema.CreateAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenitiesResponse;
import mx.cacho.choice.soapws.schema.GetAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenityResponse;
import mx.cacho.choice.soapws.service.AmenityService;
import mx.cacho.choice.soapws.test.util.TestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static mx.cacho.choice.soapws.test.util.TestUtils.generateAmenity;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebServiceServerTest(endpoints = AmenityEndpoint.class)
class AmenityEndpointTest {

    @Autowired
    AmenityEndpoint amenityEndpoint;

    @MockBean
    AmenityService amenityServiceMock;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class GetAmenity {
        @Test
        void returnsResponseWithAmenity_whenUsingRequestWithExistingId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            Amenity existingAmenity = generateAmenity(id, name, description);
            when(amenityServiceMock.getAmenity(id))
                    .thenReturn(Optional.of(existingAmenity));

            //when
            GetAmenityRequest request = new GetAmenityRequest();
            request.setId(id);
            GetAmenityResponse response = amenityEndpoint.getAmenity(request);

            //then
            assertEquals(id, response.getAmenity().getAmenityId());
            assertEquals(name, response.getAmenity().getName());
            assertEquals(description, response.getAmenity().getDescription());
            verify(amenityServiceMock, times(1)).getAmenity(id);
            verifyNoMoreInteractions(amenityServiceMock);
        }

        @Test
        void throws_whenUsingRequestWithNonExistingId() {
            //given
            Long id = nextLong(1, Long.MAX_VALUE);
            when(amenityServiceMock.getAmenity(id))
                    .thenReturn(Optional.empty());

            //when
            GetAmenityRequest request = new GetAmenityRequest();
            request.setId(id);
            Executable endpointCall = () -> amenityEndpoint.getAmenity(request);

            //then
            assertThrows(AmenityNotFoundException.class, endpointCall);
            verify(amenityServiceMock, times(1)).getAmenity(id);
            verifyNoMoreInteractions(amenityServiceMock);
        }
    }

    @Nested
    class GetAllAmenities {
        @Test
        void returnsResponseWithAmenities_whenUsingRequestWithExistingIds() {
            List<Long> existingIds = List.of(
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE),
                    nextLong(1, Long.MAX_VALUE));
            List<Amenity> existingAmenities = existingIds.stream()
                    .map(TestUtils::generateAmenity)
                    .toList();
            when(amenityServiceMock.getAllAmenities())
                    .thenReturn(existingAmenities);

            //when
            GetAmenitiesResponse response = amenityEndpoint.getAllAmenities();

            //then
            List<AmenityInfo> amenityInfoList = existingAmenities.stream().map(a -> {
                AmenityInfo ai = new AmenityInfo();
                ai.setAmenityId(a.getAmenityId());
                ai.setDescription(a.getDescription());
                ai.setName(a.getName());
                return ai;
            }).toList();
            assertEquals(3, response.getAmenity().size());
            for (int i = 0; i < amenityInfoList.size(); i++) {
                assertTrue(EqualsBuilder.reflectionEquals(amenityInfoList.get(i), response.getAmenity().get(i)));
            }
            verify(amenityServiceMock, times(1)).getAllAmenities();
            verifyNoMoreInteractions(amenityServiceMock);
        }
    }

    @Nested
    class CreateAmenity {
        @Test
        void createsAmenity_whenUsingRequestWithValidAmenity() {
            //given
            String name = randomAlphanumeric(50);
            String description = randomAlphanumeric(50);
            Amenity newAmenity = generateAmenity(0L, name, description);
            when(amenityServiceMock.createAmenity(newAmenity))
                    .thenReturn(generateAmenity(nextLong(1, Long.MAX_VALUE), name, description));

            CreateAmenityRequest request = new CreateAmenityRequest();
            request.setName(name);
            request.setDescription(description);

            //when
            GetAmenityResponse response = amenityEndpoint.createAmenity(request);

            //then
            assertEquals(name, response.getAmenity().getName());
            assertEquals(description, response.getAmenity().getDescription());
        }
    }
}
