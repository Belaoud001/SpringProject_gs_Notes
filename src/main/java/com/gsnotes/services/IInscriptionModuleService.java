package com.gsnotes.services;

import java.util.List;

public interface IInscriptionModuleService {
    public List<Object> getNotesByModuleAndYear(String year, String niveau, String moduleName);
}
