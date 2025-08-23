package com.example.rental.controller;

import com.example.rental.model.Inventory;
import com.example.rental.model.Rental;
import com.example.rental.model.User;
import com.example.rental.model.enums.Role;
import com.example.rental.service.*;
import com.example.rental.util.RentalDateComparator;
import com.itextpdf.text.DocumentException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rental")
public class RentalController {
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
    public String bookInventory(@PathVariable Long id,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate rentalDate,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Inventory inventory = inventoryService.findById(id);

        rentalService.bookInventory(user, inventory, rentalDate, returnDate);
        return "redirect:/rental";
    }

    @GetMapping
    public String rental(Model model,
                         @RequestParam(defaultValue = "rentalDate") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortOrder) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        List<Rental> rentals;
        if (user.getRole().equals(Role.ADMIN)) {
            rentals = rentalService.getAllRentalsSorted(sortBy, sortOrder);
        } else {
            rentals = rentalService.findAllRentalByUserIdSorted(user.getId(), sortBy, sortOrder);
        }

        model.addAttribute("rentals", rentals);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("nextSortOrder", sortOrder.equals("asc") ? "desc" : "asc");

        return "rental";
    }

    @PostMapping("/process")
    public String updateSelectedRentalsStatus(@RequestParam(value = "selectedRentals", required = false) List<Long> selectedRentalIds,
                                              RedirectAttributes redirectAttributes) {
        if (selectedRentalIds == null || selectedRentalIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Выберите хотя бы один элемент");
            return "redirect:/rental";
        }

        rentalService.updateSelectedRentalsStatus(1, selectedRentalIds);
        List<Rental> rentals = rentalService.findRentalsByIds(selectedRentalIds);
        notificationService.sendNotification(rentals);
        return "redirect:/rental";
    }

//    @PostMapping("/sort")
//    public String sortRentalByDate1(Model model,
//                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate rentalDate,
//                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate) {
//        List<Rental> rental = rentalService.findByRentalDateBetweenAndReturnDateBetween(rentalDate, returnDate);
//        model.addAttribute("rentals", rental);
//        //rentalService.reportGeneration(rental);
//        return "rental";
//    }

    @PostMapping("/sortDate")
    public String sortRentalByDate(Model model)
    {
               List<Rental> rental = rentalService.getSortRental();
               model.addAttribute("rentals", rental);
               return "rental";
    }

    @PostMapping("/report")
    public String reportGeneration(RedirectAttributes redirectAttributes,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate rentalDate,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate) throws DocumentException, IOException {
        List<Rental> rentals = rentalService.findByRentalDateBetweenAndReturnDateBetweenWithDetails(rentalDate, returnDate);
        rentalService.reportGeneration(rentals);
        redirectAttributes.addFlashAttribute("message", "Отчет успешно сформирован");
              return "redirect:/rental";
    }
}
