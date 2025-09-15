package api.bookstore.inventory_service.services;

import api.bookstore.inventory_service.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public int checkStock(UUID bookId){
        var count = inventoryRepository.findAllById(Collections.singleton(bookId)).size();
        return count;
    }

    public void addStock(UUID bookId, int quantity){

    }

    public void reduceStock(UUID bookId, int quantity){

    }
}
