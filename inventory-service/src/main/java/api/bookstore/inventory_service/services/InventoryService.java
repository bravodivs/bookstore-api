package api.bookstore.inventory_service.services;

import api.bookstore.inventory_service.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public int checkStock(UUID bookId){
        return inventoryRepository.findByBookId(bookId)
                .map(inventory -> inventory.getAvailableQuantity() - inventory.getReservedQuantity())
                .orElse(0);
    }

    @Transactional
    public void addStock(UUID bookId, int quantity){
        var inventory = inventoryRepository.findByBookId(bookId).orElseGet(() -> {
            var newInventory = new api.bookstore.inventory_service.models.Inventory();
            newInventory.setBookId(bookId);
            newInventory.setAvailableQuantity(0);
            newInventory.setReservedQuantity(0);
            return newInventory;
        });
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void reduceStock(UUID bookId, int quantity){
        var inventory = inventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for bookId " + bookId));

        int newAvailable = inventory.getAvailableQuantity() - quantity;
        if (newAvailable < 0) {
            throw new IllegalStateException("Insufficient stock to reduce");
        }
        inventory.setAvailableQuantity(newAvailable);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public boolean reserveStock(UUID bookId, int quantity) {
        var inventoryOpt = inventoryRepository.findByBookId(bookId);
        if (inventoryOpt.isEmpty()) {
            return false;
        }
        var inventory = inventoryOpt.get();
        int remaining = inventory.getAvailableQuantity() - inventory.getReservedQuantity();
        if (remaining < quantity) {
            return false;
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }

    @Transactional
    public void finalizeReservation(UUID bookId, int quantity) {
        var inventoryOpt = inventoryRepository.findByBookId(bookId);
        if (inventoryOpt.isEmpty()) {
            return;
        }
        var inventory = inventoryOpt.get();
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventory.setAvailableQuantity(Math.max(0, inventory.getAvailableQuantity() - quantity));
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void releaseReservation(UUID bookId, int quantity) {
        var inventoryOpt = inventoryRepository.findByBookId(bookId);
        if (inventoryOpt.isEmpty()) {
            return;
        }
        var inventory = inventoryOpt.get();
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
    }
}
