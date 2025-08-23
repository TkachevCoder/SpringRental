package com.example.rental.repository;


import com.example.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    //List<Rental> findAllByRentalDate();

    @Query(value = "SELECT * FROM rental WHERE user_id = :userId", nativeQuery = true)
    List<Rental> findAllRentalByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Rental r WHERE r.user.id = :userId")
    List<Rental> findAllRentalByUserId(@Param("userId") Long userId, Sort sort);


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
    List<Rental> findByRentalDateBetweenAndReturnDateBetween(@Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate  endDate);

    List<Rental> findAllByOrderByRentalDateAsc();

    @Query("SELECT r FROM Rental r JOIN FETCH r.user JOIN FETCH r.inventory WHERE r.rentalDate BETWEEN :startDate AND :endDate OR r.returnDate BETWEEN :startDate AND :endDate")
    List<Rental> findByRentalDateBetweenAndReturnDateBetweenWithDetails(@Param("startDate") LocalDate startDate,
                                                                        @Param("endDate") LocalDate endDate);
}
