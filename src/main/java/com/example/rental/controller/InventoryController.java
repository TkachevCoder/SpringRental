package com.example.rental.controller;

import com.example.rental.model.Inventory;
import com.example.rental.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final String UPLOAD_DIR = "uploads/";
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать каталог для загрузки!", e);
        }
    }

    @GetMapping
    public String findAll(Model model) {
        List<Inventory> inventory = inventoryService.findAllActive();
        model.addAttribute("inventory", inventory);
        return "inventory";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new Inventory()); // Передаем пустой объект Inventory
        return "add_inventory";
    }

    @GetMapping("/delete/{id}")
    public String softDelete(@PathVariable Long id) {
    inventoryService.softDelete(id);
    return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.findById(id);
        model.addAttribute("inventory", inventory);
        return "edit_inventory";
    }

    @GetMapping("/description/{id}")
    public String showDescribeForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.findById(id);
        model.addAttribute("inventory", inventory);
        return "description_inventory";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Inventory inventory) {
        inventory.setId(id);
        inventoryService.update(id,inventory);
        return "redirect:/inventory";
    }
    @PostMapping
    public String addInventory(@ModelAttribute Inventory inventory,
                               @RequestParam("image") MultipartFile file,
                               RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                // Генерируем уникальное имя файла
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);

                // Сохраняем файл
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Сохраняем путь к изображению
                inventory.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "Ошибка при загрузке изображения");
                return "redirect:/inventory/add";
            }
        }

        inventoryService.create(inventory);
        return "redirect:/inventory";
    }
}
