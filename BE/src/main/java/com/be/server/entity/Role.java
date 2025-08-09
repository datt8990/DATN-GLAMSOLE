package com.be.server.entity;

import com.be.server.entity.base.PrimaryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "role")
public class Role extends PrimaryEntity implements Serializable {

    @Column(name = "role_code", unique = true, nullable = false)
    private String code;

    @Column(name = "role_name", nullable = false)
    private String name;
}

