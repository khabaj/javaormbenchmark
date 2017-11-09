package com.khabaj.ormbenchmark.benchmarks.entities;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="phone_sequence", allocationSize=100, sequenceName = "phone_sequence")
@BatchSize(size = 1000)
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_sequence")
    private Long id;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    public Phone() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void generatePhoneNumber(String startWith, String fillWith) {

        StringBuilder phone = new StringBuilder(startWith);

        while (phone.length() <= 9) {
            phone.append(fillWith);
        }

        this.phoneNumber = phone.toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
