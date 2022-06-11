package com.gsnotes.services.impl;

import com.gsnotes.dao.IInscriptionModuleDao;
import com.gsnotes.services.IInscriptionModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inscriptionModuleServiceImpl implements IInscriptionModuleService {
    @Autowired
    IInscriptionModuleDao iInscriptionModuleDao;

    @Override
    public List<Object> getNotesByModuleAndYear(String year, String niveau, String moduleName) {
        return iInscriptionModuleDao.getNotesByModuleAndYear(year, niveau, moduleName);
    }
}
