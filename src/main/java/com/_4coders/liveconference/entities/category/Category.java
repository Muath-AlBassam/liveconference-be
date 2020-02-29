package com._4coders.liveconference.entities.category;

import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Represents the {@code Category} that the {@code User} creates for his convenience(used with {@code cards})
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "categories")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Category extends RepresentationModel<Category> implements Serializable {

    @Transient
    private static final long serialVersionUID = 94274512355714L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_categories"
    )
    @GenericGenerator(
            name = "native_categories",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "category", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_user_owner", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "categories_users",
            joinColumns = @JoinColumn(name = "fk_categories_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_users_id", referencedColumnName = "id"))
    private Set<User> members;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

}
