package com.ath.solutions.shopping;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingData {


    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void generateFiles(String input_file_name) throws IOException {
        Map<String, String> productQtyMap = new HashMap<>();
        Map<String, String> popularBrandMap;
        Map<String, Map<String, Integer>> productBrandMap = new HashMap<>();
        String qtyFileName = "0_" + input_file_name;
        String brandFileName = "1_" + input_file_name;
        int totalOrderLines = readOrderLines(input_file_name, productQtyMap, productBrandMap);
        calculateAverageProductQty(productQtyMap, totalOrderLines);
        popularBrandMap = findPopularBrand(productBrandMap);
        writeFile(productQtyMap, qtyFileName);
        writeFile(popularBrandMap, brandFileName);
    }

    public static int readOrderLines(String input_file_name, Map<String, String> productQtyMap,
                                     Map<String, Map<String, Integer>> productBrandMap) throws IOException {
        int totalOrderLines = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(input_file_name))) {
            String orderLine;
            while ((orderLine = bufferedReader.readLine()) != null) {
                String[] orderLineDetails = orderLine.split(COMMA_DELIMITER);
                String productName = orderLineDetails[2];
                String brandName = orderLineDetails[3];
                String orderQty = orderLineDetails[5];
                createProductQtyMap(productQtyMap, productName, orderQty);
                createProductBrandMap(productBrandMap, productName, brandName);
            }
        }
        return totalOrderLines;
    }

    public static void createProductQtyMap(Map<String, String> productQtyMap,
                                           String productName, String orderQty) {
        if (productQtyMap.containsKey(productName)) {
            BigDecimal qty = new BigDecimal(productQtyMap.get(productName));
            orderQty = qty.add(new BigDecimal(orderQty)).toPlainString();
        }
        productQtyMap.put(productName, orderQty);
    }

    public static void createProductBrandMap(Map<String, Map<String, Integer>> productBrandMap,
                                             String productName, String brandName) {
        Map<String, Integer> brandQtyMap = new HashMap<>();
        int qty = 1;
        if (productBrandMap.containsKey(productName)) {
            brandQtyMap = productBrandMap.get(productName);
            if (brandQtyMap.containsKey(brandName)) {
                qty = brandQtyMap.get(brandName);
                qty++;
            }
        }
        brandQtyMap.put(brandName, qty);
        productBrandMap.put(productName, brandQtyMap);
    }

    public static void calculateAverageProductQty(Map<String, String> productQtyMap, int totalOrderLines) {
        BigDecimal noOfLines = new BigDecimal(String.valueOf(totalOrderLines));
        productQtyMap.entrySet().forEach(entry -> entry.setValue
                (new BigDecimal(entry.getValue()).divide
                        (noOfLines, 2, RoundingMode.HALF_EVEN).toPlainString()));

    }

    public static Map<String, String> findPopularBrand(Map<String, Map<String, Integer>> productBrandMap) {
        Map<String, String> popularBrandMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entrySet : productBrandMap.entrySet()) {
            popularBrandMap.put(entrySet.getKey(),
                    Collections.max(entrySet.getValue().entrySet(), Map.Entry.comparingByValue()).getKey());
        }
        return popularBrandMap;
    }

    public static void writeFile(Map<String, String> inputMap, String fileName) throws IOException {

        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Map.Entry<String, String> entry : inputMap.entrySet()) {
                bufferedWriter.append(entry.getKey());
                bufferedWriter.append(COMMA_DELIMITER);
                bufferedWriter.append(entry.getValue());
                bufferedWriter.append(NEW_LINE_SEPARATOR);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input_file_name = bufferedReader.readLine();
        generateFiles(input_file_name);
        bufferedReader.close();
    }
}


