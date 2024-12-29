package gr.hua.dit.ds.ds_project_2024.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (exception instanceof SQLException sqlException) {
            if (sqlException.getSQLState().equals("23505")) { // Unique constraint violation
                return "error/error-SQL";
            }
        }


        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error-404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/error-403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error-500";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/error-400";
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                return "error/error-405";
            }
        }
        return "error/error";
    }
}