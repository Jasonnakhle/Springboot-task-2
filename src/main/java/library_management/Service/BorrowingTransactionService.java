
package library_management.Service;

import library_management.Entity.Book;
import library_management.Entity.BorrowingTransaction;
import library_management.Entity.TransactionStatus;
import library_management.Repository.BTRepository;
import library_management.Repository.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingTransactionService {

    private static final Logger logger =
            LoggerFactory.getLogger(BorrowingTransactionService.class);

    private final BTRepository btRepository;
    private final BookRepository bookRepository;

    @Value("${borrower.transaction.limit:5}")
    private int transactionLimit;

    public BorrowingTransactionService(
            BTRepository btRepository,
            BookRepository bookRepository) {
        this.btRepository = btRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BorrowingTransaction addTransaction(
            BorrowingTransaction transaction) {

        Long bookId = transaction.getBook().getId();
        Long borrowerId = transaction.getBorrower().getId();

        logger.info("Creating borrowing transaction for book ID: {}", bookId);

        // Check the borrower's existing transaction count.
        long transactionCount =
                btRepository.countByBorrower_Id(borrowerId);

        if (transactionCount >= transactionLimit) {
            logger.warn(
                    "Borrower {} has reached the transaction limit of {}",
                    borrowerId,
                    transactionLimit
            );

            throw new IllegalStateException(
                    "Borrower has reached the maximum limit of "
                            + transactionLimit + " transactions"
            );
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", bookId);
                    return new RuntimeException("Book not found");
                });

        if (!book.isAvailable()) {
            logger.warn("Book with ID {} is not available", bookId);
            throw new RuntimeException("Book is not available");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        transaction.setBorrowDate(LocalDate.now());
        transaction.setStatus(TransactionStatus.BORROWED);
        transaction.setReturnDate(null);

        BorrowingTransaction savedTransaction =
                btRepository.save(transaction);

        logger.info(
                "Borrowing transaction created successfully with ID: {}",
                savedTransaction.getId()
        );

        return savedTransaction;
    }

    @Transactional
    public BorrowingTransaction returnBook(Long id) {
        logger.info("Returning book for transaction ID: {}", id);

        BorrowingTransaction transaction = btRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            logger.warn("Transaction {} has already been returned", id);
            throw new RuntimeException("Book has already been returned");
        }

        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        transaction.setStatus(TransactionStatus.RETURNED);
        transaction.setReturnDate(LocalDate.now());

        BorrowingTransaction savedTransaction =
                btRepository.save(transaction);

        logger.info("Book returned successfully for transaction ID: {}", id);

        return savedTransaction;
    }

    public List<BorrowingTransaction> getAllTransactions() {
        logger.info("Retrieving all borrowing transactions");
        return btRepository.findAll();
    }

    public BorrowingTransaction getTransactionById(Long id) {
        logger.info("Retrieving borrowing transaction with ID: {}", id);

        return btRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });
    }

    @Transactional
    public void deleteTransaction(Long id) {
        logger.info("Deleting borrowing transaction with ID: {}", id);

        BorrowingTransaction transaction = btRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Transaction not found with ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });

        if (transaction.getStatus() == TransactionStatus.BORROWED) {
            Book book = transaction.getBook();

            book.setAvailable(true);
            bookRepository.save(book);

            logger.info(
                    "Book with ID {} made available after deleting active transaction",
                    book.getId()
            );
        }

        btRepository.delete(transaction);

        logger.info(
                "Borrowing transaction deleted successfully with ID: {}",
                id
        );
    }
}
