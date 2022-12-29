package com.my1stle.customer.portal.serviceImpl.time.zone;

import com.dev1stle.scheduling.system.v1.service.geocoding.GeocodingService;
import com.dev1stle.scheduling.system.v1.service.geocoding.Geolocation;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.my1stle.customer.portal.service.time.zone.TimeZoneService;
import com.my1stle.customer.portal.service.util.LRUCache;
import com.my1stle.customer.portal.web.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

@Service
@Primary
public class GoogleTimeZoneService implements TimeZoneService {

    private GeocodingService geocodingService;

    private String googleMapsApiKey;

    private Map<LatLng, ZoneId> timeZoneCache = Collections.synchronizedMap(
            new LRUCache<LatLng, ZoneId>(100)
    );

    @Autowired
    public GoogleTimeZoneService(
            @Value("${google.maps.api.key}") String googleMapsApiKey, GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
        this.googleMapsApiKey = googleMapsApiKey;
    }

    /**
     * @param singleLineAddress ; address formatted as a single single
     * @return zone id based on the single line address;
     * @implNote uses Google Time Zone API get time zone based on the single line address.
     * uses LocationCache to get actual geolocation. Caches Geolocation to ZoneId in memory to
     * reduce number of API calls to Google
     */
    @Override
    public ZoneId getBySingleLineAddress(String singleLineAddress) {

        LatLng geolocation = getGeolocationFrom(singleLineAddress);

        return timeZoneCache.computeIfAbsent(
                geolocation,
                new GeoLocationToZoneId(
                        new GeoApiContext.Builder().apiKey(this.googleMapsApiKey).build()
                )
        );

    }

    private LatLng getGeolocationFrom(String singleLineAddress) {

        Geolocation bySingleLineAddress = geocodingService
                .getBySingleLineAddress(singleLineAddress);

        return new LatLng(bySingleLineAddress.getLat(), bySingleLineAddress.getLng());

    }

    private static class GeoLocationToZoneId implements Function<LatLng, ZoneId> {

        private GeoApiContext context;

        private GeoLocationToZoneId(GeoApiContext context) {
            this.context = context;
        }

        /**
         * Applies this function to the given argument.
         *
         * @param latLng the function argument
         * @return the function result
         */
        @Override
        public ZoneId apply(LatLng latLng) {
            try {
                return TimeZoneApi
                        .getTimeZone(context, latLng)
                        .await()
                        .toZoneId();
            } catch (ApiException | InterruptedException | IOException e) {
                throw new InternalServerErrorException(e.getMessage(), e);
            }
        }
    }


}
