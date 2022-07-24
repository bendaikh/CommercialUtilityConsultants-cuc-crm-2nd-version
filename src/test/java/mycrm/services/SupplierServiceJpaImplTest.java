package mycrm.services;

import mycrm.models.Supplier;
import mycrm.repositories.SupplierRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceJpaImplTest {

    @InjectMocks
    private SupplierServiceJpaImpl supplierServiceJpaImpl;

    @Mock
    private Supplier mockedSupplier;

    @Mock
    private SupplierRepository mockedSupplierRepository;

    @Test
    public void shouldFindAllSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        supplierList.add(new Supplier());

        when(mockedSupplierRepository.findAll()).thenReturn(supplierList);

        List<Supplier> findAll = supplierServiceJpaImpl.findAll();

        assertEquals(supplierList, findAll);
    }

    @Test
    public void shouldReturnFiveSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        supplierList.add(new Supplier());

        when(mockedSupplierRepository.findLatest5Posts(new PageRequest(0, 5))).thenReturn(supplierList);

        List<Supplier> findLatest5 = supplierServiceJpaImpl.findLatest5();

        assertEquals(supplierList, findLatest5);
    }

    @Test
    public void shouldFindSuppliersById() {
        Supplier supplier = aSupplier();

        when(mockedSupplierRepository.findOne(Long.valueOf(5))).thenReturn(supplier);

        Supplier findById = supplierServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(supplier, findById);
        assertEquals(supplier.getId(), findById.getId());
        assertEquals(supplier.getVersion(), findById.getVersion());
        assertEquals(supplier.getSupplierReference(), findById.getSupplierReference());
        assertEquals(supplier.getBusinessName(), findById.getBusinessName());
        assertEquals(supplier.getBusinessAddr(), findById.getBusinessAddr());
        assertEquals(supplier.getBusinessAddr1(), findById.getBusinessAddr1());
        assertEquals(supplier.getBusinessCity(), findById.getBusinessCity());
        assertEquals(supplier.getBusinessPostcodeIn(), findById.getBusinessPostcodeIn());
        assertEquals(supplier.getBusinessPostcodeOut(), findById.getBusinessPostcodeOut());
        assertEquals(supplier.getFirstName(), findById.getFirstName());
        assertEquals(supplier.getLastName(), findById.getLastName());
        assertEquals(supplier.getContactNumber(), findById.getContactNumber());
        assertEquals(supplier.getMobileNumber(), findById.getMobileNumber());
        assertEquals(supplier.getEmailAddress(), findById.getEmailAddress());
        assertEquals(supplier.getDob(), findById.getDob());
        assertEquals(supplier.getElecCustomerContracts(), findById.getElecCustomerContracts());
        assertEquals(supplier.getGasCustomerContracts(), findById.getGasCustomerContracts());
    }

    private Supplier aSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(5));
        supplier.setVersion(1);
        supplier.setSupplierReference("reference");
        supplier.setBusinessName("businessname");
        supplier.setBusinessAddr("addr");
        supplier.setBusinessAddr1("addr1");
        supplier.setBusinessCity("city");
        supplier.setBusinessPostcodeIn("IN1");
        supplier.setBusinessPostcodeOut("OUT1");
        supplier.setFirstName("firstname");
        supplier.setLastName("lastname");
        supplier.setContactNumber("012374");
        supplier.setMobileNumber("07548");
        supplier.setEmailAddress("email@email.com");
        supplier.setDob("1984-01-01");
        supplier.setElecCustomerContracts(new HashSet<>());
        supplier.setGasCustomerContracts(new HashSet<>());
        return supplier;
    }

    @Test
    public void shouldSaveSuppliers() {
        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(5));

        when(mockedSupplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier savedSupplier = supplierServiceJpaImpl.create(supplier);

        assertEquals(supplier, savedSupplier);
    }

    @Test
    public void shouldEditAndSaveSuppliers() {
        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(5));

        when(mockedSupplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier savedSupplier = supplierServiceJpaImpl.edit(supplier);

        assertEquals(supplier, savedSupplier);
    }

    @Test
    public void shouldDeleteSuppliers() {
        supplierServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedSupplierRepository, times(1)).delete(Long.valueOf(541));
    }

}
