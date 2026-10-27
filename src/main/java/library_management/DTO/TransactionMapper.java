
package library_management.DTO;

import library_management.Entity.BorrowingTransaction;

public class TransactionMapper {

    public static TransactionDTO toDTO(
            BorrowingTransaction transaction) {

        return new TransactionDTO(
                transaction.getId(),
                transaction.getBook() != null
                        ? transaction.getBook().getId() : null,
                transaction.getBorrower() != null
                        ? transaction.getBorrower().getId() : null,
                transaction.getBorrowDate(),
                transaction.getReturnDate(),
                transaction.getStatus() != null
                        ? transaction.getStatus().name() : null
        );
    }
}
