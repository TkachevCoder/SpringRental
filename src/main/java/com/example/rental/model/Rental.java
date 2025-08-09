package com.example.rental.model;

import com.example.rental.model.enums.Status;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Inventory inventory;
    private Date rentalDate;
    private Date returnDate;
    private Status status;

    public Rental() {}

    public Rental(Long id, User user, Inventory inventory, Date rentalDate, Date returnDate, Status status) {
        this.id = id;
        this.user = user;
        this.inventory = inventory;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", user=" + user +
                ", inventory=" + inventory +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
