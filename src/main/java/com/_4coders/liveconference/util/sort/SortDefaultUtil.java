package com._4coders.liveconference.util.sort;

import lombok.extern.flogger.Flogger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@Flogger
public class SortDefaultUtil {

    /**
     * Gets the default sort for {@code User} if the given {@code Sort} isn't sorted
     *
     * @param sort the {@code Sort} to check if sorted or not
     * @return default sort for {@code User} if {@code Sort} is unsorted otherwise the given sort will be returned
     * unchanged
     */
    public static Sort defaultUserSortIfNotSorted(Sort sort) {
        log.atFinest().log("Checking wither the given sort [%s] is sorted or not", sort);
        if (sort.isUnsorted()) {
            log.atFinest().log("It's unsorted, setting the default sort");
            sort = MappingSortPropertiesToSchemaProperties.userMappingSortPropertiesToSchemaProperties(Sort.by(Sort.Order.desc("id")));
            log.atFinest().log("Sort has been set to default sort [%s]", sort);
        } else {
            log.atFinest().log("Sort is sorted");
        }
        return sort;
    }

    /**
     * Gets the default sort for {@code AccountIpAddress} if the given {@code Sort} isn't sorted
     *
     * @param sort the {@code Sort} to check if sorted or not
     * @return default sort for {@code AccountIpAddress} if {@code Sort} is unsorted otherwise the given sort will be returned
     * unchanged
     */
    public static Sort defaultAccountIpAddressSortIfNotSorted(Sort sort) {
        log.atFinest().log("Checking wither the given sort [%s] is sorted or not", sort);
        if (sort.isUnsorted()) {
            log.atFinest().log("It's unsorted, setting the default sort");
            sort = MappingSortPropertiesToSchemaProperties.accountIpAddressMappingSortPropertiesToSchemaProperties(Sort.by(Sort.Order.desc("dateOfMatching")));
            log.atFinest().log("Sort has been set to default sort [%s]", sort);
        } else {
            log.atFinest().log("Sort is sorted");
        }
        return sort;
    }
}
