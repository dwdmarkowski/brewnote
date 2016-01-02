package org.dmarkowski.brewnote.repository;

import org.dmarkowski.brewnote.domain.Yeast;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Yeast entity.
 */
public interface YeastRepository extends JpaRepository<Yeast,Long> {

}
