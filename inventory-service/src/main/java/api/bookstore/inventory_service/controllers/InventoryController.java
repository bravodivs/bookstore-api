package api.bookstore.inventory_service.controllers;

import api.bookstore.inventory_service.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/// On Order Created → Reserve stock (don’t reduce immediately).
///
/// On Payment Successful → Reduce stock permanently.
///
/// On Order Canceled / Payment Failed → Release reserved stock.
///
/// On Admin Restock → Add stock.

// todo Scheduled job to check for reserved stock timeouts (if payment not done in X mins → release).

// todo shud we include the orderId coz how will we know which stock are we releasing pertaining to an order?
    //request param not workign properly.
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Integer> checkStock(@PathVariable UUID bookId){
        var qty = inventoryService.checkStock(bookId);
        return ResponseEntity.ok(qty);
    }

//    //todo- not required reservation as we can reduce once order is placed successfully
//    @PostMapping("/{id}")
//    public ResponseEntity<String> reserveStock(@RequestBody int quantity){
//        return ResponseEntity.ok("Returned stock");
//    }
//    //todo- not required reservation as we can reduce once order is placed successfully
//    @PostMapping("/{id}")
//    public ResponseEntity<String> releaseStock(@RequestBody int quantity ){
//        return ResponseEntity.ok("Returned stock");
//    }

    @PostMapping("/add/{bookId}/{qty}")
    public ResponseEntity<String> addStock(@PathVariable UUID bookId, @PathVariable int qty){
        inventoryService.addStock(bookId, qty);
        return ResponseEntity.ok("Stock added");
    }

    // todo to be used only for testing. actual reduction via events.
    @PostMapping("/reduce/{bookId}/{qty}")
    public ResponseEntity<String> reduceStock(@PathVariable UUID bookId, @PathVariable int qty ){
        inventoryService.reduceStock(bookId, qty);
        return ResponseEntity.ok("Stock reduced");
    }
}
