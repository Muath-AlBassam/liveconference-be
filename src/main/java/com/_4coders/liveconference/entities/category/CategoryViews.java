package com._4coders.liveconference.entities.category;


/**
 * Contains the Views interface's for the {@code Category} class
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 12/2/2019
 */
public class CategoryViews {

    /**
     * Indicate the View of the Owner of this {@code Category} with the view of information (i.e data that are not
     * need to operate correctly)
     */
    public interface OwnerInformation {
    }

    /**
     * Indicate the View of the Owner of this {@code Category} with the view of details (i.e. data needed to operate
     * correctly)
     */
    public interface OwnerDetails {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...LITTLE...  to this {@code Category}
     */
    public interface SupportLittle {
    }

    /**
     * Indicate the View of the Support employee's with the permission ...MEDIUM...  to this {@code Category}
     */
    public interface SupportMedium extends SupportLittle {
    }


    /**
     * Indicate the View of the Support employee's with the permission ...ALL...  to this {@code Category}
     */
    public interface SupportAll extends SupportMedium {
    }

    /**
     * Indicate the View of the Admin to this {@code Category}
     */
    public interface Admin extends OwnerInformation, OwnerDetails, SupportAll {
    }

}


