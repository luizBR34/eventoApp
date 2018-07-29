package com.eventoApp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eventoApp.model.Convidado;
import com.eventoApp.model.Evento;

@Repository
public interface ConvidadoRepository extends CrudRepository<Convidado, String> {
	
	Iterable<Convidado> findByEvento(Evento evento);
	Convidado findByRg(String rg);
}
