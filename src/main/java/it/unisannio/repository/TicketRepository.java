package it.unisannio.repository;

import it.unisannio.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

   Optional<Ticket> findByIpAddressAndValidTrue(String ipAddress);

   Optional<Ticket> findByValueAndIpAddressAndValidTrue(String ott, String ipAddress);
}
