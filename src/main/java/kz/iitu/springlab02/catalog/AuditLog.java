package kz.iitu.springlab02.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    protected AuditLog() {}

    public AuditLog(String message) {
        this.message = message;
    }

    public Long getId() { return id; }
    public String getMessage() { return message; }
}