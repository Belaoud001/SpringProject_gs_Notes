package com.gsnotes.dao;

import com.gsnotes.bo.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IElementDao extends JpaRepository<Element, Long> {
    @Query(
            value = "SELECT currentCoefficient from element el\n" +
                    "WHERE el.nom = ?;",
            nativeQuery = true
    )
    double getCoefficientByElementAlias(String elementAlias);
}
