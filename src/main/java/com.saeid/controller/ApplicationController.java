package com.saeid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

@Controller
@RequestMapping("/")
public class ApplicationController {

    @RequestMapping(value="/Test", method = RequestMethod.GET)
    public String welcome(ModelMap model)  {

        Context context = null;
        Context environmentContext = null;
        DataSource dataSource = null;
        try {
            context = new InitialContext();
            environmentContext  = (Context) context.lookup("java:comp/env");
            String dataSourceName = "jdbc/TestResource";
            dataSource = (DataSource) environmentContext.lookup(dataSourceName);

        } catch (NamingException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query = "show tables";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next())
                stringBuilder.append(resultSet.getString("Tables_in_Test"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        model.addAttribute("msgArgument",
                "Maven Java Web Application Project: Success!"+stringBuilder.toString());

        return "index";
    }

    @RequestMapping(value="/Print/{arg}", method = RequestMethod.GET)
    public String welcomeName(@PathVariable String arg, ModelMap model) {
        model.addAttribute("msgArgument", "Maven Java Web Application Project, input variable: " + arg);

        return "index";
    }
}
