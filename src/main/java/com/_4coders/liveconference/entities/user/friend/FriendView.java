package com._4coders.liveconference.entities.user.friend;


/**
 * Contains the Views interface's for the {@code Friend} class
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 03/03/2020
 */
public class FriendView {
    /**
     * Indicate the View of the Owner of this {@code Friend} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code Friend} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the other {@code Friend} to this  {@code Friend}
     */
    public interface Others {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code Friend}
     */
    public interface SupportLittle extends Others {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code Friend}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code Friend}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code Friend}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}
