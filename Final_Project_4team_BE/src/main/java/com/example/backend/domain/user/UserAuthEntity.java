package com.example.backend.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthEntity {

    @Id
    @Column( name = "auth_id")
    private String id;

    @Column( columnDefinition = "boolean default false")
    private Boolean isAdmin;

    @OneToOne(mappedBy = "userAuthEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private UserProfileEntity userProfileEntity;

}
