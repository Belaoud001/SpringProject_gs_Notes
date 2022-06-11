package com.gsnotes.web.controllers;

import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.IExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class test {
    @Autowired
    IExportFile exportFile;


    @GetMapping("/test")
    public void test(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=notesfile.xls";
        response.setHeader(headerKey, headerValue);
        exportFile.exportFile(response, "2021/2022".split("/")[1], "GI2");
    }
}