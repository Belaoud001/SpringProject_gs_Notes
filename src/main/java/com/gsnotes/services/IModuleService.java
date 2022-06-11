package com.gsnotes.services;

import java.util.List;

public interface IModuleService {

    public List<String> getModulesByNiveauAlias(String alias);

    public List<String> getElementsByModuleAlias(String alias);

    public int getNoteByModuleAlias(String cne, String alias, String year);

    public int moduleBySemester(String alias, String semester);

}
