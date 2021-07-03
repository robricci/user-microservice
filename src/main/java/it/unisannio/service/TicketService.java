package it.unisannio.service;

import it.unisannio.controller.dto.TicketDTO;
import it.unisannio.model.Ticket;
import it.unisannio.repository.TicketRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class TicketService {

    private final String LOCALHOST_IPV4 = "127.0.0.1";
    private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketDTO generateOneTimeTicket(HttpServletRequest request) {
        String sourceIpAddress = getClientIp(request);
        if (sourceIpAddress != null) {
            Optional<Ticket> oldTicket = this.ticketRepository.findByIpAddressAndValidTrue(sourceIpAddress);
            // current valid ticket invalidation
            if (oldTicket.isPresent()) {
                oldTicket.get().invalid();
                this.ticketRepository.save(oldTicket.get());
            }
        }

        // new ticket generation
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Ticket newTicket = new Ticket();
        newTicket.setIpAddress(sourceIpAddress);
        newTicket.setTimestamp(timestamp);
        newTicket.setValue(DigestUtils.sha256Hex(sourceIpAddress + timestamp.toString()));
        this.ticketRepository.save(newTicket);

        return new TicketDTO(newTicket.getValue());
    }

    public boolean validateOneTimeTicket(String ott, HttpServletRequest request) {
        Optional<Ticket> validTicket =
                this.ticketRepository.findByValueAndIpAddressAndValidTrue(ott, getClientIp(request));
        if (validTicket.isPresent()) {
            validTicket.get().invalid();
            this.ticketRepository.save(validTicket.get());
        }
        return validTicket.isPresent();
    }

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!StringUtils.isEmpty(ipAddress)
                && ipAddress.length() > 15
                && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }
}
