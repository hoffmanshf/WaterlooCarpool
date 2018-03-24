package com.hoffman.carpool.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class URIBuilderUtil {

    public URI getAccountURI(final String accountBaseURL, final String... sort) {
        if (sort.length > 0) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL)
                    .queryParam("sort", sort);
            URI uri = builder.build().encode().toUri();
            return uri;
        } else {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL);
            URI uri = builder.build().encode().toUri();
            return uri;
        }
    }

    public URI getAccountSearchURI(final String accountBaseURL, final String arrival,
                                   final String departure, final String date, final String... sort) {
        if (sort.length > 0) {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("sort", sort[0]);
            final URI uri = builder.build().encode().toUri();
            return uri;
        } else {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date);
            final URI uri = builder.build().encode().toUri();
            return uri;
        }
    }

    public URI getAccountSearchPassengerURI(final String accountBaseURL, final String arrival,
                                            final String departure, final String date, final String passengerNumber, final String... sort) {
        if (sort.length > 0) {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("passengerNumber", passengerNumber)
                    .queryParam("sort", sort[0]);
            final URI uri = builder.build().encode().toUri();
            return uri;
        } else {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountBaseURL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("passengerNumber", passengerNumber);
            final URI uri = builder.build().encode().toUri();
            return uri;
        }
    }
}
