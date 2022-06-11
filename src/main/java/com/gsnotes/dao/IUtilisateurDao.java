package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Utilisateur;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUtilisateurDao extends JpaRepository<Utilisateur, Long> {

	public Utilisateur getUtilisateurByCin(String cin);

	@Query(
			value = "SELECT u.nom, u.prenom, noteFinale, m.validation, a.rang\n" +
					"FROM inscriptionannuelle a\n" +
					"INNER JOIN inscriptionmodule m ON m.idInscription = a.idInscription\n" +
					"INNER JOIN etudiant e ON e.idEtudiant = a.idEtudiant\n" +
					"INNER JOIN utilisateur u ON u.idUtilisateur = e.idEtudiant\n" +
					"WHERE a.idNiveau = 69 AND a.annee = 2022 AND m.idModule=1\n" +
					"group by u.idUtilisateur\n" +
					"ORDER BY nom, prenom;\n",
			nativeQuery = true
	)
	public List<List<String>> selectAllUsers();

	@Query(
			value = "SELECT COUNT(*) FROM module m, niveau n\n" +
					"WHERE m.idNiveau = n.idNiveau\n" +
					"AND n.alias = ?;",
			nativeQuery = true
	)
	public int getNumberOfModules(String alias);

	@Query(
			value = "SELECT el.nom FROM element el, module m\n" +
					"WHERE el.idModule = m.idModule AND m.titre = ?;",
			nativeQuery = true
	)
	public List<String> elementsPerModule(String titre);

	@Query(
			value = "SELECT m.titre FROM module m, niveau nv\n" +
					"WHERE m.idNiveau = nv.idNiveau AND nv.alias = ?;",
			nativeQuery = true
	)
	public List<String> getModules(String alias);

	@Query(
			value = "SELECT u.nom, u.prenom, u.cin, e.cne FROM \n" +
					"utilisateur u, etudiant e, inscriptionannuelle a, niveau nv\n" +
					"WHERE u.idUtilisateur = e.idEtudiant\n" +
					"AND a.idEtudiant = e.idEtudiant \n" +
					"AND a.idNiveau = nv.idNiveau\n" +
					"AND nv.alias = ? \n" +
					"AND a.annee = ? \n" +
					"ORDER BY nom, prenom, cin;\n",
			nativeQuery = true
	)
	public List<List<String>> getStudents(String alias, String year);

}
