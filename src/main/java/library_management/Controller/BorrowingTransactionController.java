
package library_management.Controller;

import library_management.DTO.TransactionDTO;
import library_management.DTO.TransactionMapper;
import library_management.Entity.BorrowingTransaction;
import library_management.Service.BookService;
import library_management.Service.BorrowerService;
import library_management.Service.BorrowingTransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class BorrowingTransactionController {

    private final BorrowingTransactionService transactionService;
    private final BookService bookService;
    private final BorrowerService borrowerService;

    public BorrowingTransactionController(
            BorrowingTransactionService transactionService,
            BookService bookService,
            BorrowerService borrowerService) {
        this.transactionService = transactionService;
        this.bookService = bookService;
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public TransactionDTO addTransaction(
            @Valid @RequestBody TransactionDTO dto) {
        BorrowingTransaction transaction = new BorrowingTransaction();

        transaction.setBook(bookService.getBookById(dto.getBookId()));
        transaction.setBorrower(
                borrowerService.getBorrowerById(dto.getBorrowerId()));

        return TransactionMapper.toDTO(
                transactionService.addTransaction(transaction));
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable Long id) {
        return TransactionMapper.toDTO(
                transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}/return")
    public TransactionDTO returnBook(@PathVariable Long id) {
        return TransactionMapper.toDTO(
                transactionService.returnBook(id));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
