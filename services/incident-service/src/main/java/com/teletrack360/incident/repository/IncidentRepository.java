package com.teletrack360.incident.repository;

import com.teletrack360.common.enums.IncidentStatus;
import com.teletrack360.incident.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    @Query("SELECT i FROM Incident i WHERE i.deleted = false")
    Page<Incident> findAllActive(Pageable pageable);
    
    @Query("SELECT i FROM Incident i WHERE i.id = :id AND i.deleted = false")
    Optional<Incident> findByIdAndNotDeleted(Long id);
    
    Page<Incident> findByStatusAndDeletedFalse(IncidentStatus status, Pageable pageable);
    
    Page<Incident> findByAssignedToAndDeletedFalse(String assignedTo, Pageable pageable);
    
    Page<Incident> findByCreatedByAndDeletedFalse(String createdBy, Pageable pageable);
}
