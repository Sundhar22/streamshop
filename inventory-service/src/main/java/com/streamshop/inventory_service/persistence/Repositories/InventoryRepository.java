package com.streamshop.inventory_service.persistence.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.streamshop.inventory_service.persistence.Entities.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
  @Query("SELECT i FROM Inventory i WHERE i.quantity < i.lowStockThreshold")
  List<Inventory> findLowStockItems();

}
