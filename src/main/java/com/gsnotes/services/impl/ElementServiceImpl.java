package com.gsnotes.services.impl;

import com.gsnotes.dao.IElementDao;
import com.gsnotes.services.IElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ElementServiceImpl implements IElementService {
    @Autowired
    IElementDao elementDao;

    @Override
    public double getCoefficientByElementAlias(String elementAlias) {
        return elementDao.getCoefficientByElementAlias(elementAlias);
    }
}
