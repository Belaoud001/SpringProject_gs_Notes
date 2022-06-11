package com.gsnotes.services.impl;

import com.gsnotes.dao.IModuleDao;
import com.gsnotes.services.IModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModuleServiceImpl implements IModuleService {

    @Autowired
    private IModuleDao moduleDao;

    public List<String> getModulesByNiveauAlias(String alias) {
        return moduleDao.getModulesByNiveauAlias(alias);
    }

    @Override
    public List<String> getElementsByModuleAlias(String alias) {
        return moduleDao.getElementsByModuleAlias(alias);
    }

    @Override
    public int getNoteByModuleAlias(String cne, String alias, String year) {
        return moduleDao.getNoteByModuleAlias(cne, alias, year);
    }

    @Override
    public int moduleBySemester(String alias, String semester) {
        return moduleDao.moduleBySemester(alias, semester);
    }


}
