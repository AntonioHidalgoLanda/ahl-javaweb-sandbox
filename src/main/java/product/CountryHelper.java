/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

/**
 *
 * @author antonio
 */
public class CountryHelper {
    public static String getIsoCode (String country){
        if (country.length() == 3){
            return country.toUpperCase();
        }
        switch (country){
            case "England":
            case "Great Britain":
            case "GB":
            case "UK":
            case "U.K.":
            case "United Kingdom":
            case "uk":
                return "GBR";
            case "Spain":
            case "España":
            case "spain":
            case "españa":
                return "ESP";
            default:
                return "";
        }
    }
}
