package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nWorker;

    public MultiThreadedSumMatrix(final int nWorker) {
        this.nWorker = nWorker;
    }

    private static class Worker extends Thread {
        private final double[][] matrice;
        private final int startpos;
        private final int nelem;
        private double finalres;


        Worker(final double[][] matrix, final int startpos, final int nelement) {
            super();
            this.matrice = matrix;
            this.startpos = startpos;
            this.nelem = nelement;
            this.finalres = 0;
        }

        public void run() {
            System.out.println("Working from position " + this.startpos + "to position " + (this.startpos + nelem - 1));

            /* Calcolo la somma partendo da un certo punto */
            for (int i = this.startpos; i < matrice.length && i < this.startpos + this.nelem; i++) {
                for (final double val : this.matrice[i]) {
                    this.finalres += val;
                }
            }
        }

        public double getCompute() {
            return this.finalres;
        }
    }
    @Override
    public double sum(final double[][] matrix) {
        double somma = 0;
        final int size = matrix.length % this.nWorker + matrix.length / this.nWorker;
        final List<Worker> workerList = new ArrayList<>();

        /* Popolo la workerList */
        for (int start = 0; start < matrix.length; start += size) {
            workerList.add(new Worker(matrix, start, size));
        }

        /* Attivo ogni Worker */
        for (final Thread worker : workerList) {
            worker.start();
        }

        /* Calcolo il risultato, aspettando la terminazione di ogni worker */
        for (final Worker worker : workerList) {
            try {
                worker.join();
                somma += worker.getCompute();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
        return somma;
    }

}

