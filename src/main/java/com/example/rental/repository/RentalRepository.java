package com.example.rental.repository;


import com.example.rental.model.Inventory;
import com.example.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query(value = "SELECT * FROM rental WHERE user_id = :userId", nativeQuery = true)
    List<Rental> findAllRentalByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE rental SET status = :status WHERE id IN :ids", nativeQuery = true)
    void updateSelectedRentalsStatus(@Param("status") Integer status,
                                     @Param("ids") List<Long> ids);


    @Query(value = """
        SELECT * FROM rental 
        WHERE (rental_date BETWEEN :startDate AND :endDate)
           OR (return_date BETWEEN :startDate AND :endDate)
           OR (rental_date <= :startDate AND return_date >= :endDate)
        """, nativeQuery = true)
    List<Rental> sortRentalByDate( @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate  endDate);
}
