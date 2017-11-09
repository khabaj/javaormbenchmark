package com.khabaj.ormbenchmark.benchmarks.entities;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user_table", indexes = {@Index(name = "lastNameIndex", columnList = "lastName")})
@SequenceGenerator(name="user_sequence", allocationSize=100, sequenceName = "user_sequence")
@BatchSize(size = 1000)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private int id;

    @Column
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Phone> phones;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.updateDate = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date creationDate) {
        this.updateDate = creationDate;
    }

    public void update() {
        this.updateDate = new Timestamp(System.currentTimeMillis());
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }
}
