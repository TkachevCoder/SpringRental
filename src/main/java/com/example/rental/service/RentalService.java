package com.example.rental.service;

import com.example.rental.model.Inventory;
import com.example.rental.model.Rental;
import com.example.rental.model.User;
import com.example.rental.model.dto.RentalDto;
import com.example.rental.model.enums.Status;
import com.example.rental.repository.RentalRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
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

    public Rental bookInventory(User user, Inventory inventory, LocalDate rentalDate, LocalDate returnDate) {
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

    public void reportGeneration(List<Rental> rentals) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\New\\Desktop\\rental\\report.pdf"));
        document.open();

        BaseFont baseFont = null;

            baseFont = BaseFont.createFont(
                    "c:/windows/fonts/arial.ttf", // путь к шрифту Arial
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

        Font font = new Font(baseFont, 12, Font.NORMAL);

        for (Rental rental : rentals) {
            String content = "ID аренды: " + rental.getId() +
                    "\nПользователь: " + (rental.getUser() != null ? rental.getUser().getUsername() : "Неизвестно") +
                    "\nИнвентарь: " + (rental.getInventory() != null ? rental.getInventory().getName() : "Неизвестно") +
                    "\nДата аренды: " + rental.getRentalDate() +
                    "\nДата возврата: " + rental.getReturnDate() +
                    "\nСтатус: " + (rental.getStatus() != null ? rental.getStatus() : "Неизвестно") +
                    "\n----------------------------------------";

            Paragraph paragraph = new Paragraph(content, font);
            document.add(paragraph);
        }
        document.close();
    }
    public List<Rental> findByRentalDateBetweenAndReturnDateBetween(LocalDate startDate, LocalDate  endDate) {
        return rentalRepository.findByRentalDateBetweenAndReturnDateBetween(startDate, endDate);
    }

    public List<Rental> getSortRental()
    {
        //return rentalRepository.findAllByOrderByRentalDateAsc();
        return rentalRepository.findAll(Sort.by("rentalDate"));
    }

    public List<Rental> getAllRentalsSorted(String sortBy, String sortOrder) {
        Sort sort = createSort(sortBy, sortOrder);
        return rentalRepository.findAll(sort);
    }

    public List<Rental> findAllRentalByUserIdSorted(Long userId, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        return rentalRepository.findAllRentalByUserId(userId, sort);
    }

    private Sort createSort(String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }

    public List<Rental> findByRentalDateBetweenAndReturnDateBetweenWithDetails(LocalDate rentalDate, LocalDate returnDate) {
        return rentalRepository.findByRentalDateBetweenAndReturnDateBetweenWithDetails(rentalDate, returnDate);
    }
}
