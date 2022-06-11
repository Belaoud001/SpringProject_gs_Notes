package com.gsnotes.services;

import java.util.List;

public interface IInscriptionMatiereService {

    public int getElementNotes(String cin, String alias, String year);

    public List<Double> getNotesByElementAndYear(String year, String niveau, String elementName);

}
