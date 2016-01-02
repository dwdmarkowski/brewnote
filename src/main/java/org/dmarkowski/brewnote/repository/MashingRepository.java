package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Mashing;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mashing entity.
 */
public interface MashingRepository extends JpaRepository<Mashing,Long> {

}
