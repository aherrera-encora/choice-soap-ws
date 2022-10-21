package mx.cacho.choice.soapws.endpoint;

import lombok.extern.slf4j.Slf4j;
import mx.cacho.choice.soapws.endpoint.exception.HotelNotFoundException;
import mx.cacho.choice.soapws.endpoint.exception.SenderException;
import mx.cacho.choice.soapws.entity.Amenity;
import mx.cacho.choice.soapws.entity.Hotel;
import mx.cacho.choice.soapws.schema.AddAmenitiesToHotelRequest;
import mx.cacho.choice.soapws.schema.CreateHotelRequest;
import mx.cacho.choice.soapws.schema.DeleteHotelRequest;
import mx.cacho.choice.soapws.schema.GetAllHotelsPaginatedRequest;
import mx.cacho.choice.soapws.schema.GetHotelRequest;
import mx.cacho.choice.soapws.schema.GetHotelResponse;
import mx.cacho.choice.soapws.schema.GetHotelsByNameRequest;
import mx.cacho.choice.soapws.schema.GetHotelsResponse;
import mx.cacho.choice.soapws.schema.HotelInfo;
import mx.cacho.choice.soapws.schema.PageInfo;
import mx.cacho.choice.soapws.schema.Pagination;
import mx.cacho.choice.soapws.schema.RemoveAmenitiesFromHotelRequest;
import mx.cacho.choice.soapws.schema.UpdateHotelRequest;
import mx.cacho.choice.soapws.service.AmenityService;
import mx.cacho.choice.soapws.service.HotelService;
import mx.cacho.choice.soapws.util.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.HashSet;
import java.util.List;

import static mx.cacho.choice.soapws.util.HotelMapper.toHotelInfo;

@Slf4j
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
        Long hotelId = request.getId();
        Hotel hotel = hotelService.getHotel(hotelId).orElseThrow(() -> new HotelNotFoundException(String.format("Hotel not found: %s", hotelId)));

        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toHotelInfo(hotel));
        log.debug("Returning hotel: {}", hotel);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllHotelsRequest")
    @ResponsePayload
    public GetHotelsResponse getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        List<HotelInfo> hotelInfoList = hotels.stream().map(HotelMapper::toHotelInfo).toList();

        GetHotelsResponse response = new GetHotelsResponse();
        response.getHotel().addAll(hotelInfoList);
        log.debug("Returning hotels #: {}", hotels.size());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllHotelsPaginatedRequest")
    @ResponsePayload
    public GetHotelsResponse getAllHotels(@RequestPayload GetAllHotelsPaginatedRequest request) {

        Pagination pagination = request.getPagination();
        final int pageNumber = pagination.getPageNumber();
        final int pageSize = pagination.getPageSize();

        if(pageNumber < 1 || pageSize < 1){
            throw new SenderException("Pagination parameters must be positive numbers.");
        }

        Page<Hotel> hotelPage = hotelService.getAllHotels(pageNumber, pageSize);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalPages(hotelPage.getTotalPages());
        pageInfo.setTotalElements(hotelPage.getTotalElements());
        List<HotelInfo> hotelInfoList = hotelPage.toList().stream().map(HotelMapper::toHotelInfo).toList();

        GetHotelsResponse response = new GetHotelsResponse();
        response.getHotel().addAll(hotelInfoList);
        response.setPageInfo(pageInfo);
        log.debug("Returning hotels #: {}, out of a total of #: {}", hotelInfoList.size(), pageInfo.getTotalElements());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHotelsByNameRequest")
    @ResponsePayload
    public GetHotelsResponse getHotelsByName(@RequestPayload GetHotelsByNameRequest request) {
        List<Hotel> hotels = hotelService.getHotelsByName(request.getName());
        List<HotelInfo> hotelInfoList = hotels.stream().map(HotelMapper::toHotelInfo).toList();

        GetHotelsResponse response = new GetHotelsResponse();
        response.getHotel().addAll(hotelInfoList);
        log.debug("Returning hotels #: {}", hotels.size());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createHotelRequest")
    @ResponsePayload
    @Transactional
    public GetHotelResponse createHotel(@RequestPayload CreateHotelRequest request) {
        List<Long> amenityIds = request.getAmenityIds();
        List<Amenity> amenities = validateAndGetAmenities(amenityIds);

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

        hotel = hotelService.createHotel(hotel);

        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toHotelInfo(hotel));
        log.debug("Created hotel: {}", hotel);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateHotelRequest")
    @ResponsePayload
    public GetHotelResponse updateHotel(@RequestPayload UpdateHotelRequest request) {
        List<Long> amenityIds = request.getAmenityIds();
        List<Amenity> amenities = validateAndGetAmenities(amenityIds);

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
                .amenities(new HashSet<>(amenities))
                .build();

        hotel = hotelService.updateHotel(hotel);

        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toHotelInfo(hotel));
        log.debug("Created hotel: {}", hotel);
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
        List<Long> amenityIds = request.getAmenityIds();
        List<Amenity> amenities = validateAndGetAmenities(amenityIds);

        Long hotelId = request.getHotelId();
        Hotel hotel = hotelService.addAmenitiesToHotel(hotelId, amenities);

        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toHotelInfo(hotel));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeAmenitiesFromHotelRequest")
    @ResponsePayload
    public GetHotelResponse removeAmenitiesToHotel(@RequestPayload RemoveAmenitiesFromHotelRequest request) {
        List<Long> amenityIds = request.getAmenityIds();
        validateAndGetAmenities(amenityIds);

        Long hotelId = request.getHotelId();
        Hotel hotel = hotelService.removeAmenitiesFromHotel(hotelId, amenityIds);

        GetHotelResponse response = new GetHotelResponse();
        response.setHotel(toHotelInfo(hotel));
        return response;
    }

    private List<Amenity> validateAndGetAmenities(List<Long> amenityIds) {
        List<Amenity> amenities = amenityService.getAmenities(amenityIds);

        // Number of passed in amenity ids must match number of fetched amenities.
        if (amenities.size() != amenityIds.size()) {
            throw new SenderException(String.format("Unable to add amenities to hotel due to non-existing amenities %s", amenityIds));
        }
        return amenities;
    }
}
