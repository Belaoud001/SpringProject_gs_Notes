package com.gsnotes.services;

import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IInscriptionMatiereDao;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.IUtilisateurDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class testTest {

    @Autowired
    private IUtilisateurDao service;

    @Autowired
    private IInscriptionMatiereDao iInscriptionMatiereDao;

    @Autowired
    private IModuleDao moduleDao;

    @Autowired
    private IInscriptionModuleService iInscriptionModuleService;

    @Test
    public void test2() {
        System.out.println(" = " + iInscriptionModuleService.getNotesByModuleAndYear("2022", "GI2", "Entreprenariat 2"));

    }

}