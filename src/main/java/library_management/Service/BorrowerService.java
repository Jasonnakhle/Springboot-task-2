
package library_management.Service;

import library_management.Entity.Borrower;
import library_management.Repository.BorrowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerService {

    private static final Logger logger =
            LoggerFactory.getLogger(BorrowerService.class);

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower addBorrower(Borrower borrower) {
        logger.info("Adding borrower: {}", borrower.getName());

        Borrower savedBorrower = borrowerRepository.save(borrower);

        logger.info("Borrower added successfully with ID: {}",
                savedBorrower.getId());

        return savedBorrower;
    }

    public List<Borrower> getAllBorrowers() {
        logger.info("Retrieving all borrowers");
        return borrowerRepository.findAll();
    }

    public Borrower getBorrowerById(Long id) {
        logger.info("Retrieving borrower with ID: {}", id);

        return borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Borrower not found with ID: {}", id);
                    return new RuntimeException("Borrower not found");
                });
    }

    public void deleteBorrower(Long id) {
        logger.info("Deleting borrower with ID: {}", id);

        borrowerRepository.deleteById(id);

        logger.info("Borrower deleted successfully with ID: {}", id);
    }

    public Borrower updateBorrower(Long id, Borrower updatedBorrower) {
        logger.info("Updating borrower with ID: {}", id);

        Borrower existingBorrower = borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Borrower not found for update with ID: {}", id);
                    return new RuntimeException("Borrower not found");
                });

        existingBorrower.setName(updatedBorrower.getName());
        existingBorrower.setEmail(updatedBorrower.getEmail());
        existingBorrower.setPhoneNumber(updatedBorrower.getPhoneNumber());

        Borrower savedBorrower = borrowerRepository.save(existingBorrower);

        logger.info("Borrower updated successfully with ID: {}", id);

        return savedBorrower;
    }
}
