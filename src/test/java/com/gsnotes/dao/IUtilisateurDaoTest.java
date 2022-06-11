package com.gsnotes.dao;

import com.gsnotes.bo.Utilisateur;
import com.gsnotes.services.IPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class IUtilisateurDaoTest {

    @Autowired
    IUtilisateurDao utilisateurDao;

    @Autowired
    IPersonService personService;

    @Test
    public void test() {
        List<List<String>> users = utilisateurDao.selectAllUsers();

        for(Object obj: users){
            System.out.println("obj = " + obj);
        }
    }

    @Test
    public void moduleCountTest() {
        int number = (utilisateurDao.getNumberOfModules("GC1"));

        System.out.println("number = " + number);
    }

    @Test
    public void elementsTest() {
        List<String> number = utilisateurDao.elementsPerModule("JEE & .NET");

        System.out.println("number = " + number.size());
    }

    @Test
    public void modulesTest() {
        List<String> number = utilisateurDao.getModules("GI2");

        System.out.println("number = " + number);
    }

    @Test
    public void etudiantsTest() {
        List<List<String>> etds = utilisateurDao.getStudents("GI2", "2022");

        for (List<String> etudiant: etds) {
            System.out.println("etudiant = " + etudiant);
        }
    }



}