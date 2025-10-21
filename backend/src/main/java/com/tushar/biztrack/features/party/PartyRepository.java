package com.tushar.biztrack.features.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<Party,Long> {
    
    Page<Party> findByNameAndType(String name, PartyType type, Pageable pageable);
}
