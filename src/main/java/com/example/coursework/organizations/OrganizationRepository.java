package com.example.coursework.organizations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
    @Query("SELECT s FROM Organization s WHERE s.name = ?1")
    Organization findByName(String name);
    // Organization findById(Long organization_id);
}
