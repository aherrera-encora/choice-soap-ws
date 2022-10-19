package mx.cacho.choice.soapws.endpoint;

import lombok.extern.slf4j.Slf4j;
import mx.cacho.choice.soapws.endpoint.exception.AmenityNotFoundException;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.schema.CreateAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenitiesResponse;
import mx.cacho.choice.soapws.schema.GetAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenityResponse;
import mx.cacho.choice.soapws.service.AmenityService;
import mx.cacho.choice.soapws.util.AmenityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

import static mx.cacho.choice.soapws.util.AmenityMapper.toAmenityInfo;

@Slf4j
@Endpoint
public class AmenityEndpoint {
    private static final String NAMESPACE_URI = "http://choice.cacho.mx/soap-ws/hotels";
    private final AmenityService amenityService;

    @Autowired
    public AmenityEndpoint(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAmenityRequest")
    @ResponsePayload
    public GetAmenityResponse getAmenity(@RequestPayload GetAmenityRequest request) {
        Long amenityId = request.getId();
        Amenity amenity = amenityService.getAmenity(amenityId).orElseThrow(() -> new AmenityNotFoundException(String.format("Amenity not found: %s", amenityId)));

        GetAmenityResponse response = new GetAmenityResponse();
        response.setAmenity(toAmenityInfo(amenity));
        log.debug("Returning amenity: {}", amenity);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllAmenitiesRequest")
    @ResponsePayload
    public GetAmenitiesResponse getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();

        GetAmenitiesResponse response = new GetAmenitiesResponse();
        response.getAmenity().addAll(amenities.stream().map(AmenityMapper::toAmenityInfo).toList());
        log.debug("Returning amenities #: {}", amenities.size());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAmenityRequest")
    @ResponsePayload
    public GetAmenityResponse createAmenity(@RequestPayload CreateAmenityRequest request) {
        Amenity amenity = Amenity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        amenity = amenityService.createAmenity(amenity);

        GetAmenityResponse response = new GetAmenityResponse();
        response.setAmenity(toAmenityInfo(amenity));
        log.debug("Created amenity: {}", amenity);
        return response;
    }
}
