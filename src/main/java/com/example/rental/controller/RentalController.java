package com.example.rental.controller;

import com.example.rental.model.Inventory;
import com.example.rental.model.Rental;
import com.example.rental.model.User;
import com.example.rental.model.enums.Role;
import com.example.rental.model.enums.Status;
import com.example.rental.service.*;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/rental")
public class RentalController
{
    private final RentalService rentalService;
    private final InventoryService inventoryService;
    private final UserService userService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public RentalController(RentalService rentalService, InventoryService inventoryService,
                            NotificationService notificationService, UserService userService,
                            EmailService emailService) {
        this.rentalService = rentalService;
        this.inventoryService = inventoryService;
        this.userService = userService;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @GetMapping("/book/{id}")
    public String showBookForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.findById(id);
        model.addAttribute("inventory", inventory); // Передаем объект inventory в модель
        return "book_inventory";
    }

    @PostMapping("/book/{id}")
    public String bookInventory( @PathVariable Long id,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date rentalDate,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Inventory inventory = inventoryService.findById(id);

        rentalService.bookInventory(user, inventory, rentalDate, returnDate);
        return "redirect:/rental";
    }

    @GetMapping
    public String rental(Model model)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        if (user.getRole().equals(Role.ADMIN))
        {
            List<Rental> rental = rentalService.getAllRentals();
            model.addAttribute("rentals", rental);
            return "rental";
        }
        else
        {
        List<Rental> rental = rentalService.findAllRentalByUserId(user.getId());
        model.addAttribute("rentals", rental);
        return "rental";
        }
    }

    @PostMapping("/process")
    public String updateSelectedRentalsStatus(@RequestParam("selectedRentals") List<Long> selectedRentalIds) {

        rentalService.updateSelectedRentalsStatus(1, selectedRentalIds);
        List<Rental> rentals = rentalService.findRentalsByIds(selectedRentalIds);
        notificationService.sendNotification(rentals);
        return "redirect:/rental";
    }

    @PostMapping("/sort")
    public String sortRentalByDate(Model model,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date rentalDate,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate) throws DocumentException, FileNotFoundException {
        List<Rental> rental = rentalService.sortRentalByDate(rentalDate, returnDate);
        model.addAttribute("rentals", rental);
        rentalService.reportGeneration(rental);
        return "rental";
    }
    @PostMapping("/report")
    public String reportGeneration(Model model,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date rentalDate,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate) throws DocumentException, FileNotFoundException {
        List<Rental> rentals = rentalService.sortRentalByDate(rentalDate, returnDate);
        rentalService.reportGeneration(rentals);
        model.addAttribute("rentals", rentals);
        return "rental";
    }
}
