package com._4coders.liveconference.entities.account.accountIpAddress;

/**
 * Contains the Views interface's for the {@code AccountIpAddress} class
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 12/2/2019
 */
public class AccountIpAddressViews {

    /**
     * Indicate the View of the Owner of this {@code AccountIpAddress} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code AccountIpAddress} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code AccountIpAddress}
     */
    public interface SupportLittle {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code AccountIpAddress}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code AccountIpAddress}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code AccountIpAddress}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}
