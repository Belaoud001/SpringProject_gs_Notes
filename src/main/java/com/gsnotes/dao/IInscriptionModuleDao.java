package com.gsnotes.dao;

import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.InscriptionModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IInscriptionModuleDao extends JpaRepository<InscriptionModule, Long> {

    @Query(
            value = "SELECT noteFinale FROM inscriptionmodule insmd,\n" +
                    "inscriptionannuelle inan, niveau nv, module md, utilisateur us\n" +
                    "WHERE insmd.idInscription = inan.idInscription\n" +
                    "AND inan.idEtudiant = us.idUtilisateur\n" +
                    "AND insmd.idModule = md.idModule\n" +
                    "AND inan.idNiveau = nv.idNiveau\n" +
                    "AND inan.annee = ?\n" +
                    "AND nv.alias = ?\n" +
                    "AND md.titre = ?\n" +
                    "ORDER BY us.nom, us.prenom, us.cin",
            nativeQuery = true
    )
    public List<Object> getNotesByModuleAndYear(String year, String niveau, String moduleName);


}
