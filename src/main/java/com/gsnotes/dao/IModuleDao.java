package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gsnotes.bo.Module;


import java.util.List;

public interface IModuleDao extends JpaRepository<Module, Long> {

    @Query(
            value = "SELECT m.titre FROM module m, niveau nv\n" +
                    "WHERE m.idNiveau = nv.idNiveau AND nv.alias = ?;",
            nativeQuery = true
    )
    public List<String> getModulesByNiveauAlias(String alias);

    @Query(
            value = "SELECT el.nom FROM element el, module m\n" +
                    "WHERE el.idModule = m.idModule AND m.titre = ?;",
            nativeQuery = true
    )
    public List<String> getElementsByModuleAlias(String alias);


    @Query(
            value = "SELECT insmd.noteFinale FROM inscriptionmodule insmd,\n" +
                    "module md, inscriptionannuelle insa, etudiant etd, utilisateur us\n" +
                    "WHERE insmd.idModule = md.idModule\n" +
                    "AND insmd.idInscription = insa.idInscription\n" +
                    "AND insa.idEtudiant = etd.idEtudiant\n" +
                    "AND etd.idEtudiant = us.idUtilisateur\n" +
                    "AND us.cin = ?\n" +
                    "AND md.titre= ?\n" +
                    "AND insa.annee = ?;",
            nativeQuery = true
    )
    public int getNoteByModuleAlias(String cne, String alias, String year);

    @Query(
            value = "SELECT COUNT(*) FROM module md, niveau nv\n" +
                    "WHERE nv.idNiveau = md.idNiveau\n" +
                    "AND nv.alias = ?\n" +
                    "AND md.semester = ?",
            nativeQuery = true
    )
    public int moduleBySemester(String alias, String semester);

}
