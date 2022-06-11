package com.gsnotes.services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IExportFile {

    void exportFile(HttpServletResponse response, String niveau, String year) throws IOException;

}
