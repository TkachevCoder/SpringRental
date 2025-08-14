package com.example.rental.service;

import com.example.rental.model.Inventory;
import com.example.rental.model.Rental;
import com.example.rental.model.User;
import com.example.rental.model.enums.Status;
import com.example.rental.repository.RentalRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> findAllRentalByUserId(Long userId) {
        return rentalRepository.findAllRentalByUserId(userId);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental findRentalById(Long id) {
        Optional<Rental> rentalOptional = rentalRepository.findById(id);
        Rental rental = rentalOptional.get();
        return rental;
    }

    public List<Rental> findRentalsByIds(List<Long> ids)
    {

        List<Rental> rentals = rentalRepository.findAllById(ids);
        return rentals;
    }

    public Rental bookInventory(User user, Inventory inventory, LocalDateTime rentalDate, LocalDateTime returnDate) {
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setInventory(inventory);
        rental.setReturnDate(returnDate);
        rental.setRentalDate(rentalDate);
        rental.setStatus(Status.BOOKED);
        return rentalRepository.save(rental);
        }
    @Transactional
    public void updateSelectedRentalsStatus(Integer newStatus, List<Long> selectedRentalIds) {
        rentalRepository.updateSelectedRentalsStatus(newStatus, selectedRentalIds);
    }

    public void reportGeneration(List<Rental> rentals) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Pudge\\Desktop\\rental\\report.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        for (Rental rental : rentals)
        {
            Paragraph paragraph = new Paragraph(rental.toString() + "\n", font);
            document.add(paragraph);
        }
        document.close();

    }

    public List<Rental> findByRentalDateBetweenAndReturnDateBetween(LocalDate startDate, LocalDate  endDate) {
        return rentalRepository.findByRentalDateBetweenAndReturnDateBetween(startDate, endDate);
    }

    public List<Rental> getSortRental()
    {
        return rentalRepository.findAllByOrderByRentalDateAsc();
    }


}
