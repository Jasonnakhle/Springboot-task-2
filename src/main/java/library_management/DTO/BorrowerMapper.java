
package library_management.DTO;

import library_management.Entity.Borrower;

public class BorrowerMapper {

    public static BorrowerDTO toDTO(Borrower borrower) {
        return new BorrowerDTO(
                borrower.getId(),
                borrower.getName(),
                borrower.getEmail(),
                borrower.getPhoneNumber()
        );
    }

    public static Borrower toEntity(BorrowerDTO dto) {
        Borrower borrower = new Borrower();
        borrower.setName(dto.getName());
        borrower.setEmail(dto.getEmail());
        borrower.setPhoneNumber(dto.getPhoneNumber());
        return borrower;
    }
}
