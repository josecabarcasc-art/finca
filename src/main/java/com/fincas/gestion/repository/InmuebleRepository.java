package com.fincas.gestion.repository;

import com.fincas.gestion.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, String> {
}
