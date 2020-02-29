package com._4coders.liveconference.util.sort;

import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import lombok.extern.flogger.Flogger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Flogger
final class SortMappingUtil {
    private SortMappingUtil() {
    }

    /**
     * Maps the given {@code Sort} for {@code User} to it's {@code Schema} properties
     *
     * @param sort the {@code User} {@code Sort} to be mapped
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code User} {@code Sort} properties
     *                                                          is given
     */
    public static Sort userMappingSortPropertiesToSchemaProperties(Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of User Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(userSortPropertiesToSchemaProperties(order.getProperty()));
            } else {
                return Sort.Order.desc(userSortPropertiesToSchemaProperties(order.getProperty()));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    /**
     * Maps the given {@code sortProperty} for {@code User} to it's {@code Schema} properties
     *
     * @param sortProperty the {@code String} representation of the property name
     * @return the name of the mapped property
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code sortProperty} is given
     */
    private static String userSortPropertiesToSchemaProperties(String sortProperty) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Mapping Sort property to User Schema property");
        String toReturn;
        switch (sortProperty) {
            case "id":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "id");
                toReturn = "id";
                break;
            case "uuid":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "uuid");
                toReturn = "uuid";
                break;
            case "username":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "username");
                toReturn = "username";
                break;
            case "lastLogin":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_login");
                toReturn = "last_login";
                break;
            case "status":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "status");
                toReturn = "status";
                break;
            case "isDeleted":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "is_deleted");
                toReturn = "is_deleted";
                break;
            case "creationDate":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "creation_date");
                toReturn = "creation_date";
                break;
            case "lastModifiedDate":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_modified_date");
                toReturn = "last_modified_date";
                break;
            case "accountId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_account_id");
                toReturn = "fk_account_id";
                break;
            default:
                log.atFine().log("No mapping for the Sort property [%s] to User Schema property", sortProperty);
                throw new MappingSortPropertiesToSchemaPropertiesException("Mapping failed from Sorted property " +
                        "to User Schema property", sortProperty);
        }
        return toReturn;
    }

    /**
     * Maps the given {@code Sort} for {@code AccountIpAddress} to it's {@code Schema} properties
     *
     * @param sort the {@code AccountIpAddress} {@code Sort} to be mapped
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code AccountIpAddress} {@code Sort} properties
     *                                                          is given
     */
    public static Sort accountIpAddressMappingSortPropertiesToSchemaProperties(Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of AccountIpAddress Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(accountIpAddressSortPropertiesToSchemaProperties(order.getProperty()));
            } else {
                return Sort.Order.desc(accountIpAddressSortPropertiesToSchemaProperties(order.getProperty()));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    /**
     * Maps the given {@code sortProperty} for {@code AccountIpAddress} to it's {@code Schema} properties
     *
     * @param sortProperty the {@code String} representation of the property name
     * @return the name of the mapped property
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code sortProperty} is given
     */
    private static String accountIpAddressSortPropertiesToSchemaProperties(String sortProperty) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Mapping Sort property to AccountIpAddress Schema property");
        String toReturn;
        switch (sortProperty) {
            case "accountId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_account_id");
                toReturn = "fk_account_id";
                break;
            case "ipAddressId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_ipAddress_id");
                toReturn = "fk_ipAddress_id";
                break;
            case "dateOfMatching":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "date_of_matching");
                toReturn = "date_of_matching";
                break;
            case "lastDateUsed":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_date_used");
                toReturn = "last_date_used";
                break;
            case "authorized":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "is_authroized");
                toReturn = "is_authroized";
                break;
            default:
                log.atFine().log("No mapping for the Sort property [%s] to AccountIpAddress Schema property", sortProperty);
                throw new MappingSortPropertiesToSchemaPropertiesException("Mapping failed from Sorted property " +
                        "to AccountIpAddress Schema property", sortProperty);
        }
        return toReturn;
    }
}
