import Model.Data;
import Model.Perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Data> abaloneData = readData();

        Perceptron perceptron = new Perceptron(abaloneData.size(), 3, 0.01);

        for (int i = 0; i < 10000; i++) {
            double error = 0; // Erro da época
            for (Data data : abaloneData) {
                double[] x = data.getInput();
                double[] y = data.getOutput();
                double[] theta = perceptron.train(x, y);
                error += sampleError(y, theta);
            }
            System.out.println("Época: " + (i + 1) + " - Erro: " + error);
        }

    }

    private static List<Data> readData() {
        List<Data> abaloneData = new LinkedList<>();
        String fileName = "abalone.data";

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                double[] input = new double[split.length - 1];
                double[] output = null;
                List<String> acceptedEntries = Arrays.asList("8", "9", "10");

                for (int i = 0; i < input.length; i++) {
                    if (!acceptedEntries.contains(split[split.length - 1]))
                        break;

                    input[i] = (i == 0) ? classifySex(split[i]) :  Double.parseDouble(split[i]);

                    if (i == input.length - 1)
                        output = handleOutput(split[i + 1]);
                }

                if (!Arrays.stream(input).allMatch(x -> x == 0.0) && output != null)
                    abaloneData.add(new Data(input, output));
            }

            br.close();
        } catch (IOException e) {
            System.err.println("Arquivo " + fileName + " não encontrado!");
        }

        return abaloneData;
    }

    private static double classifySex(String sex) {
        if (sex.equals("M"))
            return -1;
        else if (sex.equals("F"))
            return 0;
        else if (sex.equals("I"))
            return 1;

        return 0;
    }

    private static double[] handleOutput(String value) {
        if (value.equals("8"))
            return new double[]{0, 0, 0};
        else if (value.equals("9"))
            return new double[]{0, 0, 1};
        else if (value.equals("10"))
            return new double[]{0, 1, 0};

        return null;
    }

    private static double sampleError(double[] y, double[] theta) {
        double error = 0;

        for (int i = 0; i < y.length; i++)
            error += Math.abs(y[i] - theta[i]);

        return error;
    }

}