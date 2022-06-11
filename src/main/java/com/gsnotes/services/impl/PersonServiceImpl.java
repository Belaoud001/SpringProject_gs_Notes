package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.IPersonService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private IUtilisateurDao personDao;

	public List<Utilisateur> getAllPersons() {

		return personDao.findAll();
	}

	public void deletePerson(Long id) {
		personDao.deleteById(id);

	}

	public Utilisateur getPersonById(Long id) {
		return personDao.getById(id);

	}

	public void addPerson(Utilisateur pPerson) {
		personDao.save(pPerson);

	}

	public void updatePerson(Utilisateur pPerson) {
		personDao.save(pPerson);

	}

	public ExcelExporter preparePersonExport(List<Utilisateur> persons) {
		String[] columnNames = new String[] { "Nom", "Prénom", "CIN", "Email", "Télé" };
		String[][] data = new String[persons.size()][5];

		int i = 0;
		for (Utilisateur u : persons) {
			data[i][0] = u.getNom();
			data[i][1] = u.getPrenom();
			data[i][2] = u.getCin();
			data[i][3] = u.getEmail();
			data[i][4] = u.getTelephone();
			i++;
		}

		return new ExcelExporter(columnNames, data, "persons");

	}

	@Override
	public List<List<String>> getStudents(String alias, String year) {
		return personDao.getStudents(alias, year);
	}

	public Utilisateur getPersonByCin(String cin) {

		return personDao.getUtilisateurByCin( cin);

	}

	public List<List<String>> selectAllUsers() {
		return personDao.selectAllUsers();
	}

	public int getNumberOfModules(String alias) {
		return personDao.getNumberOfModules(alias);
	}

	public List<String> getModules(String alias) {
		return personDao.getModules(alias);
	}

	public List<String> getElements(String titre) {
		return personDao.elementsPerModule(titre);
	}

	public List<List<String>> getEtudiants(String alias, String year) {
		return personDao.getStudents(alias, year);
	}

}
