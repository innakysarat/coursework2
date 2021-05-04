package com.example.coursework.organizations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizaitionRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
   // Organization findById(Long organization_id);
}
