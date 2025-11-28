package ma.emsi.oussama.bookingservice.clients;

import ma.emsi.oussama.bookingservice.dtos.HotelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelRestClient {

    @GetMapping("/hotels/{id}")
    HotelDTO getHotelDetails(@PathVariable Long id);

    @GetMapping("/hotels/{id}/disponibilite/{type}")
    Boolean verifierDispo(@PathVariable Long id, @PathVariable String type);
}
