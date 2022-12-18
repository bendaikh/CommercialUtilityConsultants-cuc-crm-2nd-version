package mycrm.repositories;


import mycrm.models.LimitedCompany;
import mycrm.models.LinkedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitedCompanyRepository extends JpaRepository<LimitedCompany, Long> {

}
