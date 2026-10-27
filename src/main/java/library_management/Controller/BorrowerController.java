
package library_management.Controller;

import library_management.DTO.BorrowerDTO;
import library_management.DTO.BorrowerMapper;
import library_management.Entity.Borrower;
import library_management.Service.BorrowerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public BorrowerDTO addBorrower(
            @Valid @RequestBody BorrowerDTO dto) {
        Borrower borrower = BorrowerMapper.toEntity(dto);
        return BorrowerMapper.toDTO(borrowerService.addBorrower(borrower));
    }

    @GetMapping
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerService.getAllBorrowers()
                .stream()
                .map(BorrowerMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BorrowerDTO getBorrowerById(@PathVariable Long id) {
        return BorrowerMapper.toDTO(
                borrowerService.getBorrowerById(id));
    }

    @PutMapping("/{id}")
    public BorrowerDTO updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody BorrowerDTO dto) {
        Borrower borrower = BorrowerMapper.toEntity(dto);
        return BorrowerMapper.toDTO(
                borrowerService.updateBorrower(id, borrower));
    }

    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
    }
}
