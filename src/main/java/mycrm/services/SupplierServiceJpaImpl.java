package mycrm.services;

import mycrm.models.Supplier;
import mycrm.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SupplierServiceJpaImpl implements SupplierService {

	private final SupplierRepository supplierRepo;

	@Autowired
	public SupplierServiceJpaImpl(SupplierRepository supplierRepo) {
		this.supplierRepo = supplierRepo;
	}

	@Override
	public List<Supplier> findAll() {
		return this.supplierRepo.findAll();
	}

    @Override
    public List<Supplier> findAllOrderByBusinessName() {
        return this.supplierRepo.findAllOrderByBusinessName();
    }

	@Override
	public List<Supplier> findLatest5() {
		return this.supplierRepo.findLatest5Posts(new PageRequest(0, 5));
	}

	@Override
	public Supplier findById(Long id) {
		return this.supplierRepo.findOne(id);
	}

	@Override
	public Supplier create(Supplier Supplier) {
		return this.supplierRepo.save(Supplier);
	}

	@Override
	public Supplier edit(Supplier Supplier) {
		return this.supplierRepo.save(Supplier);
	}

	@Override
    public void deleteById(Long id) {
        this.supplierRepo.delete(id);
    }

    @Override
    public Supplier findByBusinessName(String gasSupplier) {
        return this.supplierRepo.findByBusinessName(gasSupplier);
    }
}
