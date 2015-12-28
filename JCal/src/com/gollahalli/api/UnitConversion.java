/*
 * Copyright (c) 2015 Akshay Raj Gollahalli
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.gollahalli.api;

/**
 *
 * @author Akshay Raj Gollahalli
 */
public class UnitConversion {
    
// ******************************* TEMPERATURE *********************************
    
    /**
     * This method converts Celsius to Fahrenheit
     * 
     * @param celsius A double value
     * @return fahrenheit A double value
     */
    public double CelsiusToFahrenheit(double celsius){
        double fahrenheit = celsius * 9/5 + 32;
        return fahrenheit;
    }
    
    /**
     * This method converts Fahrenheit to Celsius
     * 
     * @param fahrenheit A double value
     * @return celsius A double value
     */
    public double FahrenheitToCelsius(double fahrenheit){
        double celsius = (fahrenheit - 32) * 5/9;
        return celsius;
    }
    
    /**
     * This method converts Celsius to Kelvin
     *
     * @param celsius
     * @return
     */
    public double CelsiusToKelvin(double celsius){
        double kelvin = celsius * 273.15;
        return kelvin;
    }
    
    /**
     * This method converts Fahrenheit to Kelvin
     *
     * @param fahrenheit
     * @return
     */
    public double FahrenheitToKelvin(double fahrenheit){
        double kelvin = (fahrenheit + 459.67) * 5/9;
        return kelvin;
    }
    
    /**
     * This method converts Kelvin to Celsius
     * 
     * @param kelvin
     * @return
     */
    public double KelvinToCelsius(double kelvin){
        double celsius = kelvin - 273.15;
        return celsius;
    }
    
    /**
     * This method converts Kelvin to Fahrenheit
     *
     * @param kelvin
     * @return
     */
    public double KelvinToFahrenheit(double kelvin){
        double fahrenheit = kelvin * 9/5 - 459.67;
        return fahrenheit;
    }
    
// ******************************* WEIGHT *********************************
    
    public double KilogramToGram(double kilogram){
        double gram = kilogram * 1000;
        return gram;
    }
    
    public double KilogramToMetricTon(double kilogram){
        double metricTon = kilogram / 1000;
        return metricTon;
    }
    
    public double GramToKilogram(double gram){
        double kilogram = gram / 1000;
        return kilogram;
    }
    
    public double GramToMetricTon(double gram){
        double metricTon = gram / 1000000;
        return metricTon;
    }
    
    public double MetricTonToKilogram(double metricTon){
        double kilogram = metricTon / 1000000;
        return kilogram;
    }
}
