package com._4coders.liveconference.entities.setting;


/**
 * Contains the Views interface's for the {@code Setting} classes
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 12/2/2019
 */
public class SettingViews {

    /**
     * Indicate the View of the Owner of this {@code Setting} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code Setting} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the other {@code Setting} to this  {@code Setting}
     */
    public interface Others {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code Setting}
     */
    public interface SupportLittle extends Others {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code Setting}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code Setting}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code Setting}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}


