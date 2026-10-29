package kz.iitu.springlab02.web;

import kz.iitu.springlab02.catalog.TransferService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam BigDecimal amount) {
        transferService.transfer(fromId, toId, amount);
        return "Transfer successful";
    }

    @PostMapping("/transfer-with-audit")
    public String transferWithAudit(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam BigDecimal amount, @RequestParam boolean simulateError) {
        transferService.transferWithAudit(fromId, toId, amount, simulateError);
        return "Transfer with audit successful";
    }

    @GetMapping("/{id}/simulate-lock")
    public String simulateLock(@PathVariable Long id) {
        transferService.simulateOptimisticLock(id);
        return "Lock test passed";
    }
}