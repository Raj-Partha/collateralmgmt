package com.sec.lending.collateral.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Builder
public class User{
    @Id
    @GeneratedValue
    private long id;
    private String shortName;
    @ElementCollection(targetClass = Roles.class)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles_id")
    private Collection<Roles> roles;

}
