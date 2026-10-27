
package library_management.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TransactionDTO {

    private Long id;

    @NotNull
    private Long bookId;

    @NotNull
    private Long borrowerId;

    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, Long bookId, Long borrowerId,
                          LocalDate borrowDate, LocalDate returnDate,
                          String status) {
        this.id = id;
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getBorrowerId() { return borrowerId; }
    public void setBorrowerId(Long borrowerId) { this.borrowerId = borrowerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
