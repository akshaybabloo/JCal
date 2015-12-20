/*
 * The MIT License
 *
 * Copyright 2015 Akshay Raj Gollahalli.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
    
    public double GramToKilogram(double gram){
        double kilogram = gram / 1000;
        return kilogram;
    }
}
