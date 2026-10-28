
package library_management.Repository;

import library_management.Entity.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BTRepository
        extends JpaRepository<BorrowingTransaction, Long> {

    long countByBorrower_Id(Long borrowerId);
}
