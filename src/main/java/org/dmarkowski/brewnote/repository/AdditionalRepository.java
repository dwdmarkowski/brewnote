package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Additional;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Additional entity.
 */
public interface AdditionalRepository extends JpaRepository<Additional,Long> {

}
