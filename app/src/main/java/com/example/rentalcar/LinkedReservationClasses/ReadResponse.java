package com.example.rentalcar.LinkedReservationClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadResponse {
    //metodo che consente di prendere in ingresso lo stream di input e metterlo in una certa forma
    public static String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        //appendiamo tutto in un'unica stringa(tutte le righe) andando a capo a ogni riga che leggiamo
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}
