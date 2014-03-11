/**
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/**
 * @version 1.0, 11-Mar-2014
 * @author kuldeep
 */

package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class Company {
    private List<CompanyDTO> companyDTOs;
    private int              lastIndex = 0;

    public Company() {

    }

    private CompanyDTO getNextCompanyData() {
        if (companyDTOs == null) {
            lastIndex = 0;
            try {
                loadCompanies();
            } catch (Exception e) {
                
            }
        }
        if (companyDTOs == null)
            return null;
        if (lastIndex < companyDTOs.size())
            return companyDTOs.get(lastIndex++);
        return null;
    }

    public void loadCompanies() throws Exception {
        Scanner s = null;
        try {
            companyDTOs = new ArrayList<CompanyDTO>();
            File f = new File("company.csv");
            System.out.println(f.getAbsolutePath());
            s = new Scanner(new FileInputStream(f));
            String[] headers = readLine(s);
            System.out.println("headers: " + Arrays.toString(headers));
            if (headers != null && headers.length > 0) {
                String[] data = null;
                while ((data = readLine(s)) != null) {
                    System.out.println("data: " + Arrays.toString(data));
                    if (data.length != headers.length) {
                        companyDTOs = null;
                        throw new Exception("Invalid Data - headers count " + headers.length + " does not match with data count " + data.length);
                    }
                    String year = data[0];
                    String month = data[1];
                    for (int x = 2; x < data.length; x++) {
                        double price = new Double(data[x]).doubleValue();
                        CompanyDTO dto = new CompanyDTO(headers[x], year, month, price);
                        companyDTOs.add(dto);
                    }
                }
            }
        } finally {
            if (s != null)
                s.close();
        }
    }

    private String[] readLine(Scanner s) {
        if (s.hasNextLine()) {
            return s.nextLine().trim().split(",");
        }
        return null;
    }

    public void processCompanies() {
        Map<String, CompanyDTO> companies = new HashMap<String, CompanyDTO>();
        CompanyDTO newCompany = null;

        // repeat until all company data processed from CSV file
        while ((newCompany = getNextCompanyData()) != null) {
            CompanyDTO oldCompany = companies.get(newCompany.getName());
            if (oldCompany == null || newCompany.getPrice() > oldCompany.getPrice())
                companies.put(newCompany.getName(), newCompany);
        }

        // Done, now display the winners
        for (String name : companies.keySet()) {
            CompanyDTO company = companies.get(name);
            System.out.println(company.getName() + " highest price " + company.getPrice() + " is " + company.getMonth() + " " + company.getYear());
        }
    }

    public static void main(String[] args) {
        Company company = new Company();
        company.processCompanies();
    }

    private static class CompanyDTO {
        private String name;
        private String year;
        private String month;
        private double price;

        public CompanyDTO(String name, String year, String month, double price) {
            this.name = name;
            this.year = year;
            this.month = month;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getYear() {
            return year;
        }

        public String getMonth() {
            return month;
        }

        public double getPrice() {
            return price;
        }
    }

}
