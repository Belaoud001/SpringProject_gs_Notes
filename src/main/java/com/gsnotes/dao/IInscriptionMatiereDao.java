package com.gsnotes.dao;

import com.gsnotes.bo.InscriptionMatiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IInscriptionMatiereDao extends JpaRepository<InscriptionMatiere, Long> {

    @Query(
            value = "SELECT insm.noteFinale , insm.idInscription FROM inscriptionmatiere insm,\n" +
                    "element el, inscriptionannuelle insa, etudiant etd, utilisateur us\n" +
                    "WHERE insm.idMatiere = el.idMatiere\n" +
                    "AND insm.idInscription = insa.idInscription\n" +
                    "AND insa.idEtudiant = etd.idEtudiant\n" +
                    "AND etd.idEtudiant = us.idUtilisateur\n" +
                    "AND us.cin = ?\n" +
                    "AND el.nom= ?\n" +
                    "AND insa.annee = ?;",
            nativeQuery = true
    )
    public int getElementNotes(String cin, String alias, String year);

    @Query(
            value = "SELECT noteFinale FROM inscriptionmatiere insma,\n" +
                    "inscriptionannuelle inan, niveau nv, element el, utilisateur us\n" +
                    "WHERE insma.idInscription = inan.idInscription\n" +
                    "AND inan.idEtudiant = us.idUtilisateur\n" +
                    "AND insma.idMatiere = el.idMatiere\n" +
                    "AND inan.idNiveau = nv.idNiveau\n" +
                    "AND inan.annee = ?\n" +
                    "AND nv.alias = ?\n" +
                    "AND el.nom = ?\n" +
                    "ORDER BY us.nom, us.prenom, us.cin",
            nativeQuery = true
    )
    public List<Double> getNotesByElementAndYear(String year, String niveau, String elementName);

}
