package kz.iitu.springlab02.catalog;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final AuditService auditService;

    @Autowired
    private EntityManager entityManager; // <-- Добавлено для отвязки объекта

    public TransferService(AccountRepository accountRepository, AuditService auditService) {
        this.accountRepository = accountRepository;
        this.auditService = auditService;
    }

    // === ЗАДАНИЕ A: Обычный перевод ===
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = accountRepository.findById(fromId).orElseThrow();
        Account to = accountRepository.findById(toId).orElseThrow();

        from.debit(amount);
        to.credit(amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    // === ЗАДАНИЕ B: Propagation (REQUIRES_NEW) ===
    @Transactional
    public void transferWithAudit(Long fromId, Long toId, BigDecimal amount, boolean simulateError) {

        // Вызываем метод ИЗ ДРУГОГО КЛАССА
        auditService.saveAuditLog("Transfer started: " + amount);

        Account from = accountRepository.findById(fromId).orElseThrow();
        Account to = accountRepository.findById(toId).orElseThrow();

        from.debit(amount);
        to.credit(amount);

        accountRepository.save(from);
        accountRepository.save(to);

        if (simulateError) {
            throw new RuntimeException("Simulated error after transfer!");
        }
    }

    // === ЗАДАНИЕ D: Optimistic Locking ===
    @Transactional
    public void simulateOptimisticLock(Long id) {
        // 1. Пользователь А загружает данные (версия 1)
        Account userA = accountRepository.findById(id).orElseThrow();

        // ВАЖНО: Отвязываем объект от сессии, чтобы Hibernate "забыл" о нем
        entityManager.detach(userA);

        // 2. Пользователь Б загружает данные, меняет и сохраняет (версия становится 2)
        Account userB = accountRepository.findById(id).orElseThrow();
        userB.credit(new BigDecimal("100"));
        accountRepository.saveAndFlush(userB);

        // 3. Пользователь А пытается сохранить свои старые данные (версия 1) -> ВЫЛЕТИТ ОШИБКА!
        userA.credit(new BigDecimal("50"));
        accountRepository.saveAndFlush(userA);
    }
}