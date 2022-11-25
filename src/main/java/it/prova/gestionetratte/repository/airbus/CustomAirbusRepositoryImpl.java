package it.prova.gestionetratte.repository.airbus;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.prova.gestionetratte.model.Airbus;

public class CustomAirbusRepositoryImpl implements CustomAirbusRepository{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Airbus> findByExample(Airbus example) {
		// TODO Auto-generated method stub
		return null;
	}

}
