package Cucumber.Forever;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph = graph.readGraph();

        String graphInDot = graph.toDot();
        System.out.println("Введённый граф в виде программы на языке Dot: \n" + graphInDot);

        Scanner scn = new Scanner(System.in);
        System.out.println("Введите N: ");
        int N = scn.nextInt();

        graph.searchGamiltonCicle(N);
        graphInDot = graph.toDot();
        System.out.println(graphInDot);
    }
}
