package com.maharjanworks.cafe.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name ="User.findByEmail", query = "select u from User u where u.email = :email")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user_tbl")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    private String firstname;
    private String lastname;
    private String contactNumber;
    private String email;
    private String password;
    private String status;
    private String role;

}
