package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Hop;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hop entity.
 */
public interface HopRepository extends JpaRepository<Hop,Long> {

}
