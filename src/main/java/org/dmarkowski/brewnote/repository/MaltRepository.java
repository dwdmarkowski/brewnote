package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Malt;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Malt entity.
 */
public interface MaltRepository extends JpaRepository<Malt,Long> {

}
