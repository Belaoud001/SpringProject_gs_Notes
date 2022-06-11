package com.gsnotes.web.controllers;

import com.gsnotes.services.IExportFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/prof")
public class ProfController {

    @Autowired
    IExportFile exportFile;

    @GetMapping("/exportfile")
    public void ExportFile(HttpServletResponse response, @RequestParam(name = "year") String year, @RequestParam(name = "niveau") String niveau) throws IOException {
        System.out.println(year);
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=Delibration-" + year + "-" + niveau + ".xls";
        response.setHeader(headerKey, headerValue);
        exportFile.exportFile(response, year.split("/")[1], niveau);
    }
}
