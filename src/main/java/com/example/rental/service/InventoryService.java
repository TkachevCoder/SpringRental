package com.example.rental.service;

import com.example.rental.model.Inventory;
import com.example.rental.repository.InventoryRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public List<Inventory> findAllActive() {
        return inventoryRepository.findAllActive();
    }

    public Inventory create(Inventory inventory) {
        //todo
        return inventoryRepository.save(inventory);
    }

    public Inventory findById(Long id) {
        Optional<Inventory> inventoryOptional= inventoryRepository.findById(id);
        Inventory inventory = inventoryOptional.get();
        return  inventory;
    }

    public void softDelete(Long id) {
        Optional<Inventory> inventoryOptional= inventoryRepository.findById(id);
        Inventory inventory = inventoryOptional.get();
        inventory.setDeleted(true);
        inventoryRepository.save(inventory);
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

    public void update(Long id, Inventory inventory) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if (inventoryOptional.isEmpty()) {
            throw new IllegalStateException("Inventory с id " + id + " не существует");
        }
        Inventory inventoryToUpdate = inventoryOptional.get();
        inventoryToUpdate.setName(inventory.getName());
        inventoryToUpdate.setPrice(inventory.getPrice());
        inventoryToUpdate.setType(inventory.getType());
        inventoryRepository.save(inventoryToUpdate);
    }
}
