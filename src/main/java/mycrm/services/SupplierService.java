package mycrm.services;

import mycrm.models.Supplier;

import java.util.List;

public interface SupplierService {
    
    List<Supplier> findAll();

    List<Supplier> findAllOrderByBusinessName();
    List<Supplier> findLatest5();
    Supplier findById(Long id);
    Supplier create(Supplier supplier);
    Supplier edit(Supplier supplier);

    void deleteById(Long id);

    Supplier findByBusinessName(String gasSupplier);
}
