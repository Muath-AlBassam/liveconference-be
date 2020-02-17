package com._4coders.liveconference.entities.chat;


/**
 * Contains the Views interface's for the {@code Chat} class
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 12/2/2019
 */
public class ChatViews {

    /**
     * Indicate the View of the Owner of this {@code Chat} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code Chat} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the other {@code Chat} to this  {@code Chat}
     */
    public interface Others {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code Chat}
     */
    public interface SupportLittle extends Others {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code Chat}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code Chat}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code Chat}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}


