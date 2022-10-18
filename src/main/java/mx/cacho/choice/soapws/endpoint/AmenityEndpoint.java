package mx.cacho.choice.soapws.endpoint;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.schema.AmenityInfo;
import mx.cacho.choice.soapws.schema.CreateAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenitiesResponse;
import mx.cacho.choice.soapws.schema.GetAmenityRequest;
import mx.cacho.choice.soapws.schema.GetAmenityResponse;
import mx.cacho.choice.soapws.service.AmenityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

import static mx.cacho.choice.soapws.util.AmenityMapper.toAmenityInfo;

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
        return amenityService.getAmenity(request.getId())
                .map(h -> {
                    GetAmenityResponse response = new GetAmenityResponse();
                    response.setAmenity(toAmenityInfo().apply(h));
                    return response;
                }).orElse(null); //TODO
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllAmenitiesRequest")
    @ResponsePayload
    public GetAmenitiesResponse getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();

        List<AmenityInfo> amenityInfoList = amenities.stream().map(amenity -> {
            AmenityInfo amenityInfo = new AmenityInfo();
            BeanUtils.copyProperties(amenity, amenityInfo);
            return amenityInfo;
        }).toList();

        GetAmenitiesResponse response = new GetAmenitiesResponse();
        response.getAmenity().addAll(amenityInfoList);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAmenityRequest")
    @ResponsePayload
    public GetAmenityResponse createAmenity(@RequestPayload CreateAmenityRequest request) {
        Amenity amenity = Amenity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        AmenityInfo amenityInfo = new AmenityInfo();
        if (amenityService.createAmenity(amenity)) {
            BeanUtils.copyProperties(amenity, amenityInfo);
        }

        GetAmenityResponse response = new GetAmenityResponse();
        response.setAmenity(amenityInfo);
        return response;
    }
}
