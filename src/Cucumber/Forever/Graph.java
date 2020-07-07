package Cucumber.Forever;

import java.util.*;

public class Graph {
    private int[][] matrixGraph;
    private String[] colorNodes;
    private String[][] colorAdges;

    public int size () {
        if (isMatrixEmpty()) {
            return 0;
        }
        return matrixGraph.length;
    }

    public void addAdge(int v1, int v2, int value) {
        if (isMatrixEmpty()) {
            int max = v1 > v2 ? v1: v2;
            createMatrix(max + 1);
        }
        if (v1 >= matrixGraph.length || v2 >= matrixGraph.length) {
            int max = v1 > v2 ? v1: v2;
            recreateMatrix(max + 1);
        }

        matrixGraph[v1][v2] = value;
        matrixGraph[v2][v1] = value;
    }

    public void addColorOfNode(int v, String color) {
        if (isMatrixEmpty()) {
            recreateMatrix(v + 1);
        }
        colorNodes[v] = color;
    }

    public void addColorEdge(int v1, int v2, String color) {
        if (isMatrixEmpty()) {
            int max = v1 > v2 ? v1: v2;
            recreateMatrix(max + 1);
        }
        colorAdges[v1][v2] = color;
        colorAdges[v2][v1] = color;
    }

    public String toDot () {
        boolean[][] isAdgeAdded = new boolean[matrixGraph.length][matrixGraph.length];
        String dotProgram = "graph {\n";
        for (int i = 0; i < matrixGraph.length; i++) {
            if (colorNodes[i] != "") {
                dotProgram +="    " + i +  " [color=" + colorNodes[i] +"]\n";
            }

            for (int j = i + 1; j < matrixGraph.length; j++) {
                if (matrixGraph[i][j] != 0 && !isAdgeAdded[j][i]) {
                    dotProgram += "    " + i + " -- " + j;

                    boolean isColorAdges = false;
                    if (colorAdges[i][j] != "") {
                        dotProgram+= " [color=" + colorAdges[i][j];
                        isColorAdges= true;
                    }

                    if (isColorAdges) dotProgram+= " ";
                    else dotProgram += " [";

                    dotProgram+= "label=" + matrixGraph[i][j] + "]";

                    dotProgram += "\n";
                    isAdgeAdded[i][j] = true;
                }
            }
        }
        dotProgram += "}";
        return dotProgram;
    }

    private int[][] intDoubleArrayCopyOf(int[][] doubleArray, int newLength) {
        int[][] newDoubleArray = new int[newLength][newLength];
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                if (i < doubleArray.length && j < doubleArray.length) {
                    newDoubleArray[i][j] = doubleArray[i][j];
                } else {newDoubleArray[i][j] = 0;}
            }
        }
        return newDoubleArray;
    }

    private String[][] stringDoubleArrayCopyOf(String[][] doubleArray, int newLength) {
        String[][] newDoubleArray = new String[newLength][newLength];
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                if (i < doubleArray.length && j < doubleArray.length) {
                    newDoubleArray[i][j] = doubleArray[i][j];
                } else {newDoubleArray[i][j] = "";}
            }
        }
        return newDoubleArray;
    }

    private String[] stringArrayCopyOf(String[] array, int newLength) {
        String[] newArray = new String[newLength];
        for (int i = 0; i < newArray.length; i++) {
            if (i < array.length) {
                newArray[i] = array[i];
            } else newArray[i] = "";
        }
        return newArray;
    }

    private boolean isMatrixEmpty () {
        if (matrixGraph == null || colorNodes == null || colorAdges == null) {
            return true;
        }
        else return false;
    }

    private void createMatrix(int v) {
        matrixGraph = new int[v][v];
        colorNodes = new String[v];
        colorAdges = new String[v][v];

        for (int i = 0; i < v; i++) {
            colorNodes[i] = "";
            for (int j = 0; j < v; j++) {
                matrixGraph[i][j] = 0;
                colorAdges[i][j] = "";
            }
        }
    }

    private void recreateMatrix(int v) {
        matrixGraph = intDoubleArrayCopyOf(matrixGraph, v);
        colorAdges = stringDoubleArrayCopyOf(colorAdges, v);
        colorNodes = stringArrayCopyOf(colorNodes, v);
    }

    public void searchGamiltonCicle(int N) {
        ArrayList<int[][]> allCicles = new ArrayList<>();
        boolean[] ifNodeVisited = new boolean[size()];
        int[][] adgeSystem = new int[size()][size()];
        ifNodeVisited[0] = true;
        int node = 0;
        int lengthAdges = 0;
        searchGamiltonCicles(N, allCicles, node, lengthAdges, ifNodeVisited, adgeSystem, node);
        addColorOfSubgraph(allCicles.get(0));
    }

    private void addColorOfSubgraph(int[][] subgraph) {
        String color = "blue";
        for (int i = 0; i < size(); i++) {
            addColorOfNode(i, color);
            for (int j = 0; j < size(); j++) {
                if (subgraph[i][j] != 0) {
                    addColorEdge(i, j, color);
                }
            }
        }
    }

    private boolean searchGamiltonCicles(int N, ArrayList<int[][]> allCicles, int firstNode, int lengthAdges, boolean[] ifNodeVisited,
                                      int[][] adgeSystem, int node) {
        if (conjunction(ifNodeVisited) && matrixGraph[node][firstNode] != 0) {
            adgeSystem[node][firstNode] = matrixGraph[node][firstNode];
            adgeSystem[firstNode][node] = matrixGraph[firstNode][node];
            if (lengthAdges + matrixGraph[firstNode][node] < N) {
                allCicles.add(adgeSystem);
                return true;
            } else {
                return false;
            }
        } else {
            for (int i = 0; i < size(); i++) {
                if (!ifNodeVisited[i] && matrixGraph[node][i] != 0) {
                    boolean[] newIfNodeVisited = Arrays.copyOf(ifNodeVisited, size());
                    newIfNodeVisited[i] = true;
                    int[][] newAdgeSystem = intDoubleArrayCopyOf(adgeSystem, size());
                    newAdgeSystem[node][i] = matrixGraph[node][i];
                    newAdgeSystem[i][node] = matrixGraph[i][node];
                    int newNode = i;
                    int newlengthAdges = lengthAdges + matrixGraph[i][node];
                    if (searchGamiltonCicles(N, allCicles, firstNode, newlengthAdges, newIfNodeVisited, newAdgeSystem, newNode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean conjunction(boolean[] array) {
        boolean booleanArray = true;
        for (int i = 0; i < array.length; i++) {
            booleanArray = booleanArray && array[i];
        }
        return booleanArray;
    }

    public Graph readGraph() {
        Scanner scn = new Scanner(System.in);
        Graph graph = new Graph();
        System.out.println("Введите количество рёбер: ");
        int countAdges = scn.nextInt();
        for (int i = 0; i < countAdges; i++) {
            System.out.println("Введите, разделяя пробелом, вершину-начало ребра, вершину-конец ребра и стоимость пути между ними(вес ребра): ");
            int[] newAdge = new int[3];
//            Edge newEdge = new Edge();
            for (int j = 0; j < newAdge.length; j++) {
                newAdge[j] = Integer.parseInt(scn.next());
            }
            graph.addAdge(newAdge[0], newAdge[1], newAdge[2]);
        }
        return graph;
    }
}