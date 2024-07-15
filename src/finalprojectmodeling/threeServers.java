/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectmodeling;

import java.util.*;
import javax.swing.table.DefaultTableModel;

class Customer {

    public int IAT_random;
    public int IAT;
    public int Arrival_Time;
    public int Service_Time_Random;
    public int Service_Time_A;
    public int Service_Time_B;
    public int Service_Time_C;
    public int Service_BeginA = 0;
    public int Service_EndA = 0;
    public int Service_BeginB = 0;
    public int Service_EndB = 0;
    public int Service_BeginC = 0;
    public int Service_EndC = 0;
    public int Waiting_Time;
}

public class threeServers {

    public static int Simulation_Length;
    public static Customer[] customers;
    public static ArrayList<Integer> A = new ArrayList<>();
    public static ArrayList<Integer> B = new ArrayList<>();
    public static ArrayList<Integer> C = new ArrayList<>();

    public static void calcArrivalTime() {
        customers[0].Arrival_Time = customers[0].IAT;
        for (int i = 1; i < Simulation_Length; i++) {
            customers[i].Arrival_Time = customers[i - 1].Arrival_Time + customers[i].IAT;
        }
    }

    public static int generateServiceTimeRandom() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public static void calculateServiceTimeA(int i) {
        if (customers[i].Service_Time_Random >= 1 && customers[i].Service_Time_Random <= 30) {
            customers[i].Service_Time_A = 2;
        }
        if (customers[i].Service_Time_Random >= 31 && customers[i].Service_Time_Random <= 58) {
            customers[i].Service_Time_A = 3;
        }
        if (customers[i].Service_Time_Random >= 59 && customers[i].Service_Time_Random <= 83) {
            customers[i].Service_Time_A = 4;
        }
        if (customers[i].Service_Time_Random >= 84 && customers[i].Service_Time_Random <= 100) {
            customers[i].Service_Time_A = 5;
        }
    }

    public static void calculateServiceTimeB(int i) {
        if (customers[i].Service_Time_Random >= 1 && customers[i].Service_Time_Random <= 35) {
            customers[i].Service_Time_B = 3;
        } else if (customers[i].Service_Time_Random >= 36 && customers[i].Service_Time_Random <= 60) {
            customers[i].Service_Time_B = 4;
        } else if (customers[i].Service_Time_Random >= 61 && customers[i].Service_Time_Random <= 80) {
            customers[i].Service_Time_B = 5;
        } else if (customers[i].Service_Time_Random >= 81 && customers[i].Service_Time_Random <= 100) {
            customers[i].Service_Time_B = 6;
        }
    }

    public static void calculateServiceTimeC(int i) {
        if (customers[i].Service_Time_Random >= 1 && customers[i].Service_Time_Random <= 40) {
            customers[i].Service_Time_C = 5;
        } else if (customers[i].Service_Time_Random >= 41 && customers[i].Service_Time_Random <= 60) {
            customers[i].Service_Time_C = 6;
        } else if (customers[i].Service_Time_Random >= 61 && customers[i].Service_Time_Random <= 90) {
            customers[i].Service_Time_C = 7;
        } else if (customers[i].Service_Time_Random >= 91 && customers[i].Service_Time_Random <= 100) {
            customers[i].Service_Time_C = 8;
        }
    }

    public static void Simulate() {
        customers = new Customer[Simulation_Length];

        for (int i = 0; i < Simulation_Length; i++) {
            customers[i] = new Customer();
            customers[i].IAT_random = generateServiceTimeRandom();

            if (customers[i].IAT_random >= 1 && customers[i].IAT_random <= 25) {
                customers[i].IAT = 1;
            }
            if (customers[i].IAT_random >= 26 && customers[i].IAT_random <= 65) {
                customers[i].IAT = 2;
            }
            if (customers[i].IAT_random >= 66 && customers[i].IAT_random <= 85) {
                customers[i].IAT = 3;
            }
            if (customers[i].IAT_random >= 86 && customers[i].IAT_random <= 100) {
                customers[i].IAT = 4;
            }

            customers[i].Service_Time_Random = generateServiceTimeRandom();
        }

        calcArrivalTime();

        for (int i = 0; i < Simulation_Length; i++) {
            if (i == 0) {//3SHAN AWEL CUST KEDA KEDA HY5OSH 3AND A
                customers[i].Service_BeginA = customers[i].Arrival_Time;
                calculateServiceTimeA(i);
                //service end bt3t able 3and awel cust
                customers[i].Service_EndA = customers[i].Service_BeginA + customers[i].Service_Time_A;
                A.add(i);
            } //LOW 'A' FADY 
            else if (A.isEmpty() || customers[i].Arrival_Time >= customers[A.get(A.size() - 1)].Service_EndA) {
                customers[i].Service_BeginA = customers[i].Arrival_Time;
                calculateServiceTimeA(i);
                customers[i].Service_EndA = customers[i].Service_BeginA + customers[i].Service_Time_A;
                customers[i].Waiting_Time = 0;
                A.add(i);
            } //LOW 'B' FADY   
            else if (B.isEmpty() || customers[i].Arrival_Time >= customers[B.get(B.size() - 1)].Service_EndB) {
                customers[i].Service_BeginB = customers[i].Arrival_Time;
                calculateServiceTimeB(i);
                customers[i].Service_EndB = customers[i].Service_BeginB + customers[i].Service_Time_B;
                customers[i].Waiting_Time = 0;
                B.add(i);
            } //LOW 'C' FADY
            else if (C.isEmpty() || customers[i].Arrival_Time >= customers[C.get(C.size() - 1)].Service_EndC) {
                customers[i].Service_BeginC = customers[i].Arrival_Time;
                calculateServiceTimeC(i);
                customers[i].Service_EndC = customers[i].Service_BeginC + customers[i].Service_Time_C;
                customers[i].Waiting_Time = 0;
                C.add(i);
            } else {//LOW  A and B and C are busy (priority A>B>C)
                if (customers[A.get(A.size() - 1)].Service_EndA <= customers[B.get(B.size() - 1)].Service_EndB) {
                    customers[i].Service_BeginA = customers[A.get(A.size() - 1)].Service_EndA;
                    calculateServiceTimeA(i);
                    A.add(i);
                } else if (customers[B.get(B.size() - 1)].Service_EndB <= customers[C.get(C.size() - 1)].Service_EndC) {
                    customers[i].Service_BeginB = customers[B.get(B.size() - 1)].Service_EndB;
                    calculateServiceTimeB(i);
                    B.add(i);
                } else {
                    customers[i].Service_BeginC = customers[C.get(C.size() - 1)].Service_EndC;
                    calculateServiceTimeC(i);
                    C.add(i);
                }

            }
        }
    }

    public static void printTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows
        for (int i = 0; i < Simulation_Length; i++) {
            model.addRow(new Object[]{
                (i + 1), customers[i].IAT_random, customers[i].IAT, customers[i].Arrival_Time,
                customers[i].Service_Time_Random, customers[i].Service_BeginA,
                customers[i].Service_Time_A, customers[i].Service_EndA,
                customers[i].Service_BeginB, customers[i].Service_Time_B,
                customers[i].Service_EndB, customers[i].Service_BeginC,
                customers[i].Service_Time_C, customers[i].Service_EndC,
                customers[i].Waiting_Time});
        }
    }

    public static void calStatistics() {
        int totalWaitTime = 0;
        int totalServiceTime = 0;

        for (int i = 0; i < Simulation_Length; i++) {
            totalWaitTime += customers[i].Waiting_Time;

            totalServiceTime += (customers[i].Service_EndA - customers[i].Service_BeginA)
                    + (customers[i].Service_EndB - customers[i].Service_BeginB)
                    + (customers[i].Service_EndC - customers[i].Service_BeginC);
        }

        double avgWaitTime = (double) totalWaitTime / Simulation_Length;
        double avgServTime = (double) totalServiceTime / Simulation_Length;

        System.out.println("Average Wait Time: " + avgWaitTime);
        System.out.println("Average Service Time: " + avgServTime);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Customers Count ");
        Simulation_Length = sc.nextInt();
        Simulate();
        calStatistics();
    }

}
