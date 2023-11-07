package cl.ms.maquinarias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.ms.maquinarias.models.RollBackAudit;

public interface AuditRepository extends JpaRepository<RollBackAudit, Long> {

}
