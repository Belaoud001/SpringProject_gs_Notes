package com.gsnotes.services.impl;

import com.gsnotes.dao.IInscriptionMatiereDao;
import com.gsnotes.services.IInscriptionMatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InscriptionMatiereImpl implements IInscriptionMatiereService {

    @Autowired
    IInscriptionMatiereDao iInscriptionMatiereDao;

    @Override
    public int getElementNotes(String cin, String alias, String year) {
        return iInscriptionMatiereDao.getElementNotes(cin, alias, year);
    }

    @Override
    public List<Double> getNotesByElementAndYear(String year, String niveau, String elementName) {
        return iInscriptionMatiereDao.getNotesByElementAndYear(year, niveau, elementName);
    }
}
