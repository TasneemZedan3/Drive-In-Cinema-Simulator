/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectmodeling;

import java.util.Random;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;

public class Stocks {

    public static Stocks[][] stock;
    public static int cycle;
    public static int simulationDaysPerCycle;
    public static int orderQuantity;
    public static int orderCondition;
    public static boolean getOrder = false;
    public static int backOrder = 0; //a-save feha el shortage low fe etenen shortage 7asalo fe kaza youm 3ara b3d
    public static int waitDays = 0; // a-save feha iterations el ayam 3shan -- menhom el daysleft 
    public int inventoryBegin;
    public int widgets;
    public int widgetsRandom;
    public int inventoryEnd;
    public int shortage;
    public int quantity;
    public int leadTimeRandom;
    public int daysLeft;

    public static void RandomWidgets(Stocks inventory) {
        Random rand = new Random();
        inventory.widgetsRandom = rand.nextInt(100);
        if (inventory.widgetsRandom >= 1 && inventory.widgetsRandom <= 33) {
            inventory.widgets = 0;
        } else if (inventory.widgetsRandom >= 34 && inventory.widgetsRandom <= 58) {
            inventory.widgets = 10;
        } else if (inventory.widgetsRandom >= 59 && inventory.widgetsRandom <= 78) {
            inventory.widgets = 20;
        } else if (inventory.widgetsRandom >= 79 && inventory.widgetsRandom <= 90) {
            inventory.widgets = 30;
        } else {
            inventory.widgets = 40;
        }
    }

    public static void RandomLead(Stocks inventory) {
        Random rand = new Random();
        inventory.leadTimeRandom = rand.nextInt(100);
        if (inventory.leadTimeRandom >= 1 && inventory.leadTimeRandom <= 30) {
            inventory.daysLeft = 1;
        } else if (inventory.leadTimeRandom >= 31 && inventory.leadTimeRandom <= 80) {
            inventory.daysLeft = 2;
        } else {
            inventory.daysLeft = 3;
        }
        waitDays = inventory.daysLeft;
    }

    public static void simulate(int simDaysPerCycle, int numCycles, int initialStocks, int orderQty, int orderCond) {
        simulationDaysPerCycle = simDaysPerCycle;
        cycle = numCycles;
        orderQuantity = orderQty;
        orderCondition = orderCond;
        stock = new Stocks[cycle][simulationDaysPerCycle];

        for (int i = 0; i < cycle; i++) {
            for (int j = 0; j < simulationDaysPerCycle; j++) {
                stock[i][j] = new Stocks();

                if (i == 0 && j == 0) { //first day stockBegin is given by the user
                    stock[i][j].inventoryBegin = initialStocks;
                } else {
                    if (j > 0) {//lesa fe nafs el cycle bas diffrent day 
                        stock[i][j].inventoryBegin = stock[i][j - 1].inventoryEnd;
                    } else if (i > 0) { //last day of previous cycle
                        //a5er youm fe el cycle eli ablha mesh j 3shan low katabt j hatgbli awell you fe a5er cycle
                        stock[i][j].inventoryBegin = stock[i - 1][simulationDaysPerCycle - 1].inventoryEnd;
                    }

                }

                RandomWidgets(stock[i][j]);

                if (waitDays > 0) {  //low gali order be el lead time an2as meno lama el youm y3di
                    stock[i][j].daysLeft = waitDays - 1;
                    waitDays--;
                } else if (getOrder) { //fe order et3mal 
                    //lama waitday =0 keda el order weslt w a7oto 3ala el stock begin
                    stock[i][j].inventoryBegin += orderQuantity;
                    getOrder = false; //el order wasal
                }

                if (stock[i][j].widgets + backOrder > stock[i][j].inventoryBegin) { //7asal shortage
                    stock[i][j].inventoryEnd = 0; //bnsafar el stockEnd low etlab meni zeyada 3an eli ma3aya
                    //low 7asal shoratge abl keda ne-addo 3ala el shortage el geded 
                    stock[i][j].shortage = stock[i][j].widgets - stock[i][j].inventoryBegin + backOrder;
                    backOrder = stock[i][j].shortage; //ne-add 3aleh bt3 kol el days eli fato
                } else { //ma7slsh shortage
                    stock[i][j].inventoryEnd = stock[i][j].inventoryBegin - stock[i][j].widgets - backOrder;
                    backOrder = 0;
                }
                //low weslna "a5er youm" fe el cycle w "7a22na el condtion" eli el user da5lo han3ml order geded
                if (stock[i][j].inventoryEnd <= orderCondition && j + 1 == simulationDaysPerCycle) {
                    RandomLead(stock[i][j]);
                    getOrder = true;
                    stock[i][j].quantity = orderQuantity;
                }
            }
        }
    }

    public static void printTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows

        for (int i = 0; i < cycle; i++) {
            for (int j = 0; j < simulationDaysPerCycle; j++) {
                model.addRow(new Object[]{
                    i + 1,
                    j + 1, stock[i][j].inventoryBegin, stock[i][j].quantity,
                    stock[i][j].widgets, stock[i][j].widgetsRandom,
                    stock[i][j].inventoryEnd, stock[i][j].shortage,
                    stock[i][j].leadTimeRandom, stock[i][j].daysLeft});
            }
        }
    }

    public static void calStatistics() {
        int totalStock = 0;
        int countShortage = 0;
        int Pecentage = 0;

        for (int i = 0; i < cycle; i++) {
            for (int j = 0; j < simulationDaysPerCycle; j++) {
                totalStock += stock[i][j].inventoryEnd;

                if (stock[i][j].shortage > 0) {
                    countShortage++;

                }
                Pecentage = (countShortage / (cycle * simulationDaysPerCycle)) * 100;
            }
        }
        System.out.println("count of shoratge is " + countShortage + " days out of " + cycle * simulationDaysPerCycle);
        System.out.println("Average Ending Inventory: " + totalStock / (cycle * simulationDaysPerCycle));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter cycle length: "); // 4
        int numCycles = scanner.nextInt();
        System.out.print("Enter no of simulation days (per cycle): "); // 7
        int simDaysPerCycle = scanner.nextInt();
        System.out.print("Enter Order Quantity: "); // 10
        int orderQty = scanner.nextInt();
        System.out.print("Enter Stocks Starting value: "); // 10
        int initialStocks = scanner.nextInt();
        System.out.print("Enter Order Condition: ");// 6
        int orderCond = scanner.nextInt();
        simulate(simDaysPerCycle, numCycles, initialStocks, orderQty, orderCond);
        calStatistics();

    }

}
