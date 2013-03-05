package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.domain.Report;
import com.ncsu.gradiance.domain.SelectedCourse;
import com.ncsu.gradiance.service.DataBaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
@SessionAttributes({"user", "selectedCourse"})
public class ReportController {

    private DataBaseQuery dbQuery;

    @Autowired
    public ReportController(DataBaseQuery dbQuery){
        this.dbQuery = dbQuery;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("selectedCourse") SelectedCourse
                                       selectedCourse){
        selectedCourse.setReport(new Report());
        return "reports";

    }

    @RequestMapping(method = RequestMethod.POST)
    public String showResult(
            @ModelAttribute("selectedCourse") SelectedCourse selectedCourse,
            BindingResult result, Model model) {
        ResultSet rs = this.dbQuery.executeQuery(selectedCourse.getReport().
                getQuery(), result);
        selectedCourse.getReport().setShowQuery(true);

        int colCount = 0;
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            List<List<String>> cols = new ArrayList<>();
            colCount = rsmd.getColumnCount();
            List<String> header = new ArrayList<>();
            for (int i = 1; i <= colCount; i++)
                header.add(rsmd.getColumnLabel(i));
            cols.add(header);
            while (rs.next()) {
                cols.add(getRow(rs, colCount));
            }
            model.addAttribute("data", cols);
        } catch (SQLException | NullPointerException e) {
            result.rejectValue("", "sql.exception", e.getMessage());
        }
        return "reports";
    }

    private List<String> getRow(ResultSet rs, int cols) throws SQLException {
        List<String> row = new ArrayList<>();
        for(int i = 1; i<=cols;i++)
            row.add((rs.getString(i)));
        return row;
    }

}
