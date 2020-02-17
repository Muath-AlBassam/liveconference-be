package com._4coders.liveconference.entities.account.systemBlocked;


/**
 * Contains the Views interface's for the {@code SystemBlockedAccount} class
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 12/2/2019
 */
public class SystemBlockedAccountViews {

    /**
     * Indicate the View of the Owner of this {@code SystemBlockedAccount} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code SystemBlockedAccount} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the other {@code SystemBlockedAccount} to this  {@code SystemBlockedAccount}
     */
    public interface Others {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code SystemBlockedAccount}
     */
    public interface SupportLittle extends Others {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code SystemBlockedAccount}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code SystemBlockedAccount}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code SystemBlockedAccount}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}

