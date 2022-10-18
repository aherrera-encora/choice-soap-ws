package mx.cacho.choice.soapws.endpoint;

import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.AddAmenitiesToHotelRequest;
import mx.cacho.choice.soapws.schema.CreateHotelRequest;
import mx.cacho.choice.soapws.schema.DeleteHotelRequest;
import mx.cacho.choice.soapws.schema.GetHotelRequest;
import mx.cacho.choice.soapws.schema.GetHotelResponse;
import mx.cacho.choice.soapws.schema.GetHotelsByNameRequest;
import mx.cacho.choice.soapws.schema.GetHotelsResponse;
import mx.cacho.choice.soapws.schema.HotelInfo;
import mx.cacho.choice.soapws.schema.RemoveAmenitiesFromHotelRequest;
import mx.cacho.choice.soapws.schema.UpdateHotelRequest;
import mx.cacho.choice.soapws.service.AmenityService;
import mx.cacho.choice.soapws.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.HashSet;
import java.util.List;

import static mx.cacho.choice.soapws.util.HotelMapper.toHotelInfo;

@Endpoint
public class HotelEndpoint {

    private static final String NAMESPACE_URI = "http://choice.cacho.mx/soap-ws/hotels";
    private final HotelService hotelService;
    private final AmenityService amenityService;

    @Autowired
    public HotelEndpoint(HotelService hotelService, AmenityService amenityService) {
        this.hotelService = hotelService;
        this.amenityService = amenityService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHotelRequest")
    @ResponsePayload
    public GetHotelResponse getHotel(@RequestPayload GetHotelRequest request) {
        return hotelService.getHotel(request.getId())
                .map(h -> {
                    GetHotelResponse response = new GetHotelResponse();
                    response.setHotel(toHotelInfo().apply(h));
                    return response;
                }).orElse(null); //TODO
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllHotelsRequest")
    @ResponsePayload
    public GetHotelsResponse getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        List<HotelInfo> hotelInfoList = hotels.stream().map(toHotelInfo()).toList();

        GetHotelsResponse response = new GetHotelsResponse();
        response.getHotel().addAll(hotelInfoList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHotelsByNameRequest")
    @ResponsePayload
    public GetHotelsResponse getHotelsByName(@RequestPayload GetHotelsByNameRequest request) {
        List<Hotel> hotels = hotelService.getHotelsByName(request.getName());
        List<HotelInfo> hotelInfoList = hotels.stream().map(toHotelInfo()).toList();

        GetHotelsResponse response = new GetHotelsResponse();
        response.getHotel().addAll(hotelInfoList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createHotelRequest")
    @ResponsePayload
    @Transactional
    public GetHotelResponse createHotel(@RequestPayload CreateHotelRequest request) {
        List<Long> amenityIds = request.getAmenityIds();
        List<Amenity> amenities = amenityService.getAmenities(amenityIds);

        // Number of passed in amenity ids must match number of fetched amenities.
        if (amenities.size() != amenityIds.size()) {
            throw new RuntimeException(); //TODO: Error handling
        }

        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .address(request.getAddress())
                .rating(request.getRating())
                .amenities(new HashSet<>(amenities))
                .build();

        GetHotelResponse response = new GetHotelResponse();
        HotelInfo hotelInfo = null;
        if(hotelService.createHotel(hotel)) {
            hotelInfo = toHotelInfo().apply(hotel);
        }

        response.setHotel(hotelInfo);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateHotelRequest")
    @ResponsePayload
    public GetHotelResponse updateHotel(@RequestPayload UpdateHotelRequest request) {

        //TODO: FIX Add current amenities
        Hotel hotel = Hotel.builder()
                .hotelId(request.getHotelId())
                .name(request.getName())
                .description(request.getDescription())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .address(request.getAddress())
                .rating(request.getRating())
                .build();

        hotelService.updateHotel(hotel);

        HotelInfo hotelInfo = toHotelInfo().apply(hotel);
        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(hotelInfo);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteHotelRequest")
    @ResponsePayload
    public void deleteHotel(@RequestPayload DeleteHotelRequest request) {
        hotelService.deleteHotel(request.getHotelId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addAmenitiesToHotelRequest")
    @ResponsePayload
    public GetHotelResponse addAmenitiesToHotel(@RequestPayload AddAmenitiesToHotelRequest request) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeAmenitiesFromHotelRequest")
    @ResponsePayload
    public GetHotelResponse removeAmenitiesToHotel(@RequestPayload RemoveAmenitiesFromHotelRequest request) {
        throw new UnsupportedOperationException("NOT IMPLEMENTED");
    }
}