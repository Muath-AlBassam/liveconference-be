package com._4coders.liveconference.util.sort;

import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddress;
import com._4coders.liveconference.entities.user.User;
import lombok.extern.flogger.Flogger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


@Component
@Flogger
public class SortUtil {

    /**
     * Maps the given {@link Sort} to the {@link User}s {@code DB} schema, if the given {@link Sort} is {@code
     * unsorted} a default sort will be returned
     *
     * @param sort the {@link Sort} to map
     * @return mapped {@link Sort}, the default one will be mapped to the {@code Users} {@code ID}
     */
    public static Sort userSortMapping(Sort sort) {
        log.atFinest().log("Checking wither the given sort [%s] is sorted or not", sort);
        if (sort.isUnsorted()) {
            log.atFinest().log("It's unsorted, setting the default sort");
            sort = Sort.by(Sort.Order.desc("id"));
            log.atFinest().log("Sort has been set to default sort [%s]", sort);
        }
        log.atFinest().log("Start User Sort Mapping");
        return SortMappingUtil.userMappingSortPropertiesToSchemaProperties(sort);
    }

    /**
     * Maps the given {@link Sort} to the {@link AccountIpAddress}s {@code DB} schema, if the given {@link Sort} is {@code
     * unsorted} a default sort will be returned
     *
     * @param sort the {@link Sort} to map
     * @return mapped {@link Sort}, the default one will be mapped to the {@code AccountIpAddress} {@code dateOfMatching}
     */
    public static Sort accountIpAddressSortMapping(Sort sort) {
        log.atFinest().log("Checking wither the given sort [%s] is sorted or not", sort);
        if (sort.isUnsorted()) {
            log.atFinest().log("It's unsorted, setting the default sort");
            sort = Sort.by(Sort.Order.desc("dateOfMatching"));
            log.atFinest().log("Sort has been set to default sort [%s]", sort);
        }
        log.atFinest().log("Start AccountIpAddress Sort Mapping");
        return SortMappingUtil.accountIpAddressMappingSortPropertiesToSchemaProperties(sort);
    }
}