package com._4coders.liveconference.entities.category;

import com._4coders.liveconference.entities.Auditing.AuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

/**
 * Represents the {@code Category} that the {@code User} creates for his convenience(used with {@code cards})
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 30/1/2019
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Category extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "category", nullable = false)
    private String name;

//    @OneToMany
//    @JoinColumn(name = "fk_user_member", referencedColumnName = "id")
//    private Set<User> members;todo fix this issue solve by ManyToMany

}
