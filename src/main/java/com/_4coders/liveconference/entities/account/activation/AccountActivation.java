package com._4coders.liveconference.entities.account.activation;

import com._4coders.liveconference.entities.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Holds activation Code for any request of activation such as new Account security issue ...etc
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 26/02/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_activation")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountActivation implements Serializable {

    @Transient
    private static final long serialVersionUID = 478432134182217L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_account_activation"
    )
    @GenericGenerator(
            name = "native_account_activation",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_account_id", referencedColumnName = "id")
    private Account accountToActivate;

    @Column(name = "activation_set_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime activationSetDate;

    //the expiry date will be set 5 min in the feature
    @Column(name = "expiry_date", nullable = false, updatable = false)
    private LocalDateTime expiryDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    //true if activated, false if denied, null if still waiting
    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "code", updatable = false, nullable = false, length = 6)
    private String code;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;


    /*the activation code is formed from 6 random chars ranging from 0-9A-Z in ascii 0-9 [48,57] A-Z [65,90]*/

    /**
     * Sets the code, expiry date and activated to false
     */
    public void setUp(String reason) {
        setActivationCode();
        setExpiryDate();
        this.reason = reason;
    }

    private void setActivationCode() {
        final byte CODE_CHARS_LIMIT = 6;
        final char[] code = new char[CODE_CHARS_LIMIT];
        for (byte i = 0; i < CODE_CHARS_LIMIT; i++) {
            code[i] = generateRandomCodeChar();
        }
        this.code = new String(code);
    }

    //the expiry date will be set 5 min in the feature
    private void setExpiryDate() {
        this.expiryDate = LocalDateTime.now().plusMinutes(5);
    }

    private char generateRandomCodeChar() {
        byte randomNumber = (byte) (Math.random() * 2);//0 or 1
        if (randomNumber == 0) {//generate number
            return (char) ('0' + Math.random() * ('9' - '0' + 1));
        } else {//generate char
            return (char) ('A' + Math.random() * ('Z' - 'A' + 1));
        }
    }
}
