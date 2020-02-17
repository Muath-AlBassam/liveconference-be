package com._4coders.liveconference.entities.ipAddress;

import com._4coders.liveconference.exception.ipAddress.*;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
@Flogger
public class IpAddressService {

    @Autowired
    private IpAddressRepository ipAddressRepository;

    @Autowired
    private AsnRepository asnRepository;

    @Autowired
    private LocationInformationRepository locationInformationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IpAddressConnectionInfo ipAddressConnectionInfo;

    /**
     * Saves the given {@code IpAddress} to the DB unless it exists already
     *
     * @return saved {@code IpAddress}
     * @throws IpAddressFoundException if the given {@code IpAddress} exist already
     */
    @Transactional
    public IpAddress saveIpAddress(IpAddress ipAddress) throws IpAddressFoundException {
        if (ipAddressRepository.existsIpAddressByIp(ipAddress.getIp())) {
            log.atFinest().log("Trying to save an existent IpAddress [%s], thus results in throwing IpAddressFoundException", ipAddress);
            log.atFine().log("Throwing the exception [$s]", IpAddressFoundException.class);
            throw new IpAddressFoundException(String.format("the Address [%s] exist already", ipAddress));
        } else {

            log.atFinest().log("Saving IpAddress [%s]", ipAddress);
            return ipAddressRepository.save(ipAddress);
        }
    }


    /**
     * Returns an {@code IpAddress} from given ip if exists
     *
     * @return {@code IpAddress} if exists otherwise {@code null}
     */
    public IpAddress getIpAddress(String ip) {
        log.atFinest().log("Getting an IpAddress from DB by the ip [%s]", ip);
        final IpAddress toReturn = ipAddressRepository.getIpAddressByIp(ip);
        log.atFinest().log("Result of retrieving an IpAddress from DB by ip [%s] is [%s]", ip, toReturn);
        return toReturn;
    }

    /**
     * Returns wither the given {@code ipAddress} exist or not
     *
     * @param ip {@code String} representation of the {@code ip}
     * @return {@code true} if the {@code ipAddress} exists otherwise {@code false}
     */
    public boolean ipAddressExists(String ip) {
        log.atFinest().log("Checking wither the given ip [%s] exists or not", ip);
        final boolean toReturn = ipAddressRepository.existsIpAddressByIp(ip);
        log.atFinest().log("Result is [%b]", toReturn);
        return toReturn;
    }

    /**
     * Get a single {@code IpAddress} from a request
     *
     * @return IpAddress either from the {@code DB} if available or from the provider
     * @throws InvalidIpAddressException    when HTTP 400 has been received
     * @throws APIKeyNotProvidedException   when HTTP 401 has been received
     * @throws APIKeyQuotaExceededException when HTTP 403 has been received
     * @throws IpProviderUnknownException   when HTTP XXX has been received
     */
    public IpAddress getSingleIpAddress(HttpServletRequest request) throws InvalidIpAddressException,
            APIKeyNotProvidedException, APIKeyQuotaExceededException, IpProviderUnknownException {
        log.atFinest().log("Getting a single IpAddress from request");
        String ipAddress = obtainIpAddress(request);
        log.atFinest().log("Fetching IpAddress from DB if exist");
        IpAddress fetchedFromDB = ipAddressRepository.getIpAddressByIp(ipAddress);
        IpAddress toReturn;
        if (fetchedFromDB == null) {//we don't know about it, thus, get it from IP provider
            log.atFinest().log("IpAddress isn't in DB thus fetch it from an IpProvider");
            toReturn = fetchSingleIpAddressFromProvider(ipAddress);
        } else {//we have it, thus, return
            log.atFinest().log("IpAddress is in DB thus return it");
            toReturn = fetchedFromDB;
        }
        log.atFinest().log("Returning the IpAddress with data [%s]", toReturn);
        return toReturn;

    }

    /**
     * Fetches the given {@code ipAddress} from the {@code IpProvider}
     *
     * @return IpAddress
     * @throws InvalidIpAddressException    when HTTP 400 has been received
     * @throws APIKeyNotProvidedException   when HTTP 401 has been received
     * @throws APIKeyQuotaExceededException when HTTP 403 has been received
     * @throws IpProviderUnknownException   when HTTP XXX has been received
     */
    private IpAddress fetchSingleIpAddressFromProvider(String ipAddress) throws InvalidIpAddressException,
            APIKeyNotProvidedException, APIKeyQuotaExceededException, IpProviderUnknownException {
        log.atFinest().log("Fetching the IpAddress from the IpProvider");
        log.atFinest().log("Request is being initiated...");
        ResponseEntity<IpAddressDTO> fetchedFromProvider =
                restTemplate.getForEntity(ipAddressConnectionInfo.getGET_IP_INFO_FULL_URL(), IpAddressDTO.class, ipAddress);
        log.atFinest().log("Result of the request as IpAddressDTO [%s] with HttpStatus [%s]",
                fetchedFromProvider.getBody(), fetchedFromProvider.getStatusCode());
        final String errorMessage = fetchedFromProvider.getHeaders().getFirst("message");
        switch (fetchedFromProvider.getStatusCode()) {
            case OK:
                final IpAddress toReturn = fetchedFromProvider.getBody().toIpAddress();
                log.atFinest().log("As the HttpStatus was [%s] return the IpAddress object with data [%s]",
                        fetchedFromProvider.getStatusCode(), toReturn);
                return toReturn;
            case BAD_REQUEST:
                log.atFinest().log("As the HttpStatus was [%s] with message [%s] and given ipAddress [%s] " +
                        "throw InvalidIpAddressException", fetchedFromProvider.getStatusCode(), errorMessage, ipAddress);
                log.atFinest().log("Throwing the exception [$s]", InvalidIpAddressException.class);
                throw new InvalidIpAddressException(
                        String.format("The given IpAddress isn't valid [%s] with error message from the provider [%s]",
                                ipAddress, errorMessage), ipAddress);
            case UNAUTHORIZED:
                log.atFinest().log("As the HttpStatus was [%s] with message [%s] and given ipAddress [%s]" +
                        " throw APIKeyNotProvidedException", fetchedFromProvider.getStatusCode(), errorMessage, ipAddress);
                log.atFinest().log("Throwing the exception [$s]", APIKeyNotProvidedException.class);
                throw new APIKeyNotProvidedException(String.format("An API key wasn't provided or not valid [%s]",
                        errorMessage));
            case FORBIDDEN:
                log.atSevere().log("As the HttpStatus was [%s] throw APIKeyQuotaExceededException",
                        fetchedFromProvider.getStatusCode());
                log.atSevere().log("Throwing the exception [$s]", APIKeyQuotaExceededException.class);
                throw new APIKeyQuotaExceededException("The quota has been exceeded for the APIKey [%s]");
            default:
                log.atFinest().log("As the HttpStatus was [%s] with error message [%s] throw " +
                        "IpProviderUnknownException", fetchedFromProvider.getStatusCode(), errorMessage);
                log.atFine().log("Throwing the exception [$s]", IpProviderUnknownException.class);
                throw new IpProviderUnknownException(String.format("Unknown response was given from IPData provider with message " +
                        "[%s]", errorMessage), fetchedFromProvider.getStatusCode(), errorMessage);
        }
    }

    /**
     * Obtains the {@code ipAddress} from the received request
     *
     * @return {@code String} representation of the {@code ipAddress}
     */
    private String obtainIpAddress(HttpServletRequest request) {
        String ipAddress;
        log.atFinest().log("Obtaining the ipAddress from the request");
        if (request.getHeader("X-FORWARDED-FOR") == null) {//no proxy direct connection
            log.atFinest().log("Request is direct thus get ipAddress from getRemoteAddr()");
            ipAddress = request.getRemoteAddr();
            log.atFinest().log("The ipAddress is [%s]", request.getRemoteAddr());
        } else {
            log.atFinest().log("Request isn't direct thus get the ipAddress from the header X-FORWARDED-FOR");
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            log.atFinest().log("The ipAddress is [%s]", request.getHeader("X-FORWARDED-FOR"));
        }

        return ipAddress;
    }
}
