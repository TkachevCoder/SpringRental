package com.example.rental.model.dto;

import java.time.LocalDate;

public class RentalDto {
    private Long id;
    private String userName;
    private String inventoryName;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private String status;

    public RentalDto() {
    }

    public RentalDto(Long id, String userName, String inventoryName, LocalDate rentalDate, LocalDate returnDate, String status) {
        this.id = id;
        this.userName = userName;
        this.inventoryName = inventoryName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  "ID аренды: " + id +
                "\n, пользователь: " + userName +
                "\n, инвентарь: " + inventoryName +
                "\n, дата аренды: " + rentalDate +
                "\n, дата возврата: " + returnDate +
                "\n, текщий статус - " + status;
    }
}