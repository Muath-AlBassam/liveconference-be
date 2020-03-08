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
     * @param sort     the {@code User} {@code Sort} to be mapped
     * @param isNative indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code User} {@code Sort} properties
     *                                                          is given
     */
    public static Sort userMappingSortPropertiesToSchemaProperties(Sort sort, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of User Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(userSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            } else {
                return Sort.Order.desc(userSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    /**
     * Maps the given {@code sortProperty} for {@code User} to it's {@code Schema} properties
     *
     * @param sortProperty the {@code String} representation of the property name
     * @param isNative     indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return the name of the mapped property
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code sortProperty} is given
     */
    private static String userSortPropertiesToSchemaProperties(String sortProperty, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
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
                if (isNative) {
                    toReturn = "username";
                } else {
                    toReturn = "userName";
                }
                break;
            case "lastLogin":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_login");
                if (isNative) {
                    toReturn = "last_login";
                } else {
                    toReturn = "lastLogin";
                }
                break;
            case "status":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "status");
                toReturn = "status";
                break;
            case "isDeleted":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "is_deleted");
                if (isNative) {
                    toReturn = "is_deleted";
                } else {
                    toReturn = "isDeleted";
                }
                break;
            case "creationDate":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "creation_date");
                if (isNative) {
                    toReturn = "creation_date";
                } else {
                    toReturn = "creationDate";
                }
                break;
            case "lastModifiedDate":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_modified_date");
                if (isNative) {
                    toReturn = "last_modified_date";
                } else {
                    toReturn = "lastModifiedDate";
                }
                break;
            case "accountId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_account_id");
                if (isNative) {
                    toReturn = "fk_account_id";
                } else {
                    toReturn = "account";
                }
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
     * @param sort     the {@code AccountIpAddress} {@code Sort} to be mapped
     * @param isNative indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code AccountIpAddress} {@code Sort} properties
     *                                                          is given
     */
    public static Sort accountIpAddressMappingSortPropertiesToSchemaProperties(Sort sort, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of AccountIpAddress Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(accountIpAddressSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            } else {
                return Sort.Order.desc(accountIpAddressSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    /**
     * Maps the given {@code sortProperty} for {@code AccountIpAddress} to it's {@code Schema} properties
     *
     * @param sortProperty the {@code String} representation of the property name
     * @param isNative     indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return the name of the mapped property
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code sortProperty} is given
     */
    private static String accountIpAddressSortPropertiesToSchemaProperties(String sortProperty, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Mapping Sort property to AccountIpAddress Schema property");
        String toReturn;
        switch (sortProperty) {
            case "accountId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_account_id");
                if (isNative) {
                    toReturn = "fk_account_id";
                } else {
                    toReturn = "account";
                }
                break;
            case "ipAddressId":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_ipAddress_id");
                if (isNative) {
                    toReturn = "fk_ipAddress_id";
                } else {
                    toReturn = "ipAddress";
                }
                break;
            case "dateOfMatching":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "date_of_matching");
                if (isNative) {
                    toReturn = "date_of_matching";
                } else {
                    toReturn = "dateOfMatching";
                }
                break;
            case "lastDateUsed":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "last_date_used");
                if (isNative) {
                    toReturn = "last_date_used";
                } else {
                    toReturn = "lastDateUsed";
                }
                break;
            case "authorized":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "is_authroized");
                if (isNative) {
                    toReturn = "is_authroized";
                } else {
                    toReturn = "isAuthorized";
                }
                break;
            default:
                log.atFine().log("No mapping for the Sort property [%s] to AccountIpAddress Schema property", sortProperty);
                throw new MappingSortPropertiesToSchemaPropertiesException("Mapping failed from Sorted property " +
                        "to AccountIpAddress Schema property", sortProperty);
        }
        return toReturn;
    }

    /**
     * Maps the given {@code Sort} for {@code Friend} to it's {@code Schema} properties
     *
     * @param sort     the {@code Friend} {@code Sort} to be mapped
     * @param isNative indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code Friend} {@code Sort} properties
     *                                                          is given
     */
    public static Sort friendMappingSortPropertiesToSchemaProperties(Sort sort, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of Friend Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(friendSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            } else {
                return Sort.Order.desc(friendSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    private static String friendSortPropertiesToSchemaProperties(String sortProperty, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Mapping Sort property to Friend Schema property");
        String toReturn;
        switch (sortProperty) {
            case "id":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_user_added_id");
                if (isNative) {
                    toReturn = "fk_user_added_id";
                } else {
                    toReturn = "added";
                }
                break;
            case "date":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "relation_start_date");
                if (isNative) {
                    toReturn = "relation_start_date";
                } else {
                    toReturn = "relationStartDate";
                }
                break;
            default:
                log.atFine().log("No mapping for the Sort property [%s] to Friend Schema property", sortProperty);
                throw new MappingSortPropertiesToSchemaPropertiesException("Mapping failed from Sorted property " +
                        "to Friend Schema property", sortProperty);
        }
        return toReturn;
    }

    /**
     * Maps the given {@code Sort} for {@code FriendRequest} to it's {@code Schema} properties
     *
     * @param sort     the {@code FriendRequest} {@code Sort} to be mapped
     * @param isNative indicate wither the give {@link Sort} is used for <b>{@code Native query}</b> or not
     * @return mapped {@code Schema} {@code Sort}
     * @throws MappingSortPropertiesToSchemaPropertiesException when unknown {@code FriendRequest} {@code Sort} properties
     *                                                          is given
     */
    public static Sort friendRequestMappingSortPropertiesToSchemaProperties(Sort sort, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Starting the mapping of FriendRequest Properties to Schema properties");
        List<Sort.Order> orders = sort.get().map(order -> {
            if (order.isAscending()) {
                return Sort.Order.asc(friendRequestSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            } else {
                return Sort.Order.desc(friendRequestSortPropertiesToSchemaProperties(order.getProperty(), isNative));
            }
        }).collect(Collectors.toList());
        return Sort.by(orders);
    }

    private static String friendRequestSortPropertiesToSchemaProperties(String sortProperty, boolean isNative) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Mapping Sort property to FriendRequest Schema property");
        String toReturn;
        switch (sortProperty) {
            case "id":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "fk_user_added_id");
                if (isNative) {
                    toReturn = "fk_user_added_id";
                } else {
                    toReturn = "added";
                }
                break;
            case "date":
                log.atFinest().log("Sort property [%s] mapped to schema property [%s]", sortProperty, "creation_date");
                if (isNative) {
                    toReturn = "creation_date";
                } else {
                    toReturn = "creationDate";
                }
                break;
            default:
                log.atFine().log("No mapping for the Sort property [%s] to FriendRequest Schema property", sortProperty);
                throw new MappingSortPropertiesToSchemaPropertiesException("Mapping failed from Sorted property " +
                        "to Friend Schema property", sortProperty);
        }
        return toReturn;
    }

}
