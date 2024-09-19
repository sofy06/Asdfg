/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.Scanner;

/**
 *
 * @author Sofy
 */
public class Simplex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);
        int numVariables, numRestricciones;

        System.out.println("Ingrese el número de variables: ");
        numVariables = lector.nextInt();
        System.out.println("Ingrese el número de restricciones: ");
        numRestricciones = lector.nextInt();

        double[][] matriz = new double[numRestricciones][numVariables + numRestricciones + 1]; 
        double[] funcionObjetivo = new double[numVariables + numRestricciones]; 

        System.out.println("Ingrese los coeficientes de las restricciones:");
        for (int i = 0; i < numRestricciones; i++) {
            System.out.println("Restricción " + (i + 1) + ":");
            for (int j = 0; j < numVariables; j++) {
                System.out.print("Coeficiente de x" + (j + 1) + ": ");
                matriz[i][j] = lector.nextDouble();
            }

            matriz[i][numVariables + i] = 1;

            System.out.print("Valor del lado derecho: ");
            matriz[i][numVariables + numRestricciones] = lector.nextDouble();
        }

        System.out.println("\nIngrese los coeficientes de la función objetivo:");
        for (int i = 0; i < numVariables; i++) {
            System.out.print("Coeficiente de x" + (i + 1) + ": ");
            funcionObjetivo[i] = -lector.nextDouble(); 
        }

        imprimirTabla(matriz, funcionObjetivo);

        while (true) {
            
            int columnaPivote = encontrarColumnaPivote(funcionObjetivo);
            if (columnaPivote == -1) {
                break; 
            }

            int filaPivote = encontrarFilaPivote(matriz, columnaPivote);
            if (filaPivote == -1) {
                System.out.println("Solución ilimitada.");
                return;
            }

            realizarPivoteo(matriz, funcionObjetivo, filaPivote, columnaPivote);

            imprimirTabla(matriz, funcionObjetivo);
        }

        System.out.println("\nSolución óptima encontrada:");
        mostrarResultados(matriz, funcionObjetivo);
    }

    private static int encontrarColumnaPivote(double[] funcionObjetivo) {
        int indice = -1;
        double valorMin = 0;
        for (int i = 0; i < funcionObjetivo.length; i++) {
            if (funcionObjetivo[i] < valorMin) {
                valorMin = funcionObjetivo[i];
                indice = i;
            }
        }
        return indice;
    }

    private static int encontrarFilaPivote(double[][] matriz, int columnaPivote) {
        int filaPivote = -1;
        double menorCociente = Double.MAX_VALUE;
        for (int i = 0; i < matriz.length; i++) {
            double valorColumna = matriz[i][columnaPivote];
            if (valorColumna > 0) {
                double cociente = matriz[i][matriz[i].length - 1] / valorColumna;
                if (cociente < menorCociente) {
                    menorCociente = cociente;
                    filaPivote = i;
                }
            }
        }
        return filaPivote;
    }

    private static void realizarPivoteo(double[][] matriz, double[] funcionObjetivo, int filaPivote, int columnaPivote) {
        double valorPivote = matriz[filaPivote][columnaPivote];
        for (int j = 0; j < matriz[filaPivote].length; j++) {
            matriz[filaPivote][j] /= valorPivote;
        }

        for (int i = 0; i < matriz.length; i++) {
            if (i != filaPivote) {
                double factor = matriz[i][columnaPivote];
                for (int j = 0; j < matriz[i].length; j++) {
                    matriz[i][j] -= factor * matriz[filaPivote][j];
                }
            }
        }

        double factor = funcionObjetivo[columnaPivote];
        for (int j = 0; j < funcionObjetivo.length; j++) {
            funcionObjetivo[j] -= factor * matriz[filaPivote][j];
        }
    }

    private static void imprimirTabla(double[][] matriz, double[] funcionObjetivo) {
        System.out.println("\nTabla Simplex:");
        for (double[] fila : matriz) {
            for (double valor : fila) {
                System.out.printf("%8.2f", valor);
            }
            System.out.println();
        }
        System.out.print("Z = ");
        for (double coef : funcionObjetivo) {
            System.out.printf("%8.2f", coef);
        }
        System.out.println();
    }

    private static void mostrarResultados(double[][] matriz, double[] funcionObjetivo) {
        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("x%d = %.2f%n", (i + 1), matriz[i][matriz[i].length - 1]);
        }
        System.out.printf("Valor óptimo de Z = %.2f%n", funcionObjetivo[funcionObjetivo.length - 1]);
    }
}
