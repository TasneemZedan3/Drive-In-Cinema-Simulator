/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectmodeling;

import java.util.Random;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;

public class Tickets {

    public static Tickets[] ticket;
    public static int days;
    public int ticketTypeRandom;
    public String ticketType;
    public int demandRandom;
    public int demand;
    public int gainsFromSale;
    public int lostFromShortage;
    public int gainFromScrap;
    public int profit;

    // Constructor to initialize a Tickets object
    public Tickets() {
        ticketTypeRandom(this);
    }

    // Randomly assign the ticket type
    public void ticketTypeRandom(Tickets ticketpaper) {
        Random rand = new Random();
        ticketpaper.ticketTypeRandom = rand.nextInt(100);
        if (ticketpaper.ticketTypeRandom >= 1 && ticketpaper.ticketTypeRandom <= 35) {
            ticketpaper.ticketType = "Good";
        } else if (ticketpaper.ticketTypeRandom >= 36 && ticketpaper.ticketTypeRandom <= 80) {
            ticketpaper.ticketType = "Fair";
        } else {
            ticketpaper.ticketType = "Poor";
        }
    }

    // Randomly assign the demand
    public void getDemandRandom(Tickets ticketpaper) {
        Random rand = new Random();
        ticketpaper.demandRandom = rand.nextInt(100);

        if (ticketpaper.ticketType.equalsIgnoreCase("Good")) {
            if (ticketpaper.demandRandom >= 1 && ticketpaper.demandRandom <= 3) {
                ticketpaper.demand = 40;
            } else if (ticketpaper.demandRandom >= 4 && ticketpaper.demandRandom <= 8) {
                ticketpaper.demand = 50;
            } else if (ticketpaper.demandRandom >= 9 && ticketpaper.demandRandom <= 23) {
                ticketpaper.demand = 60;
            } else if (ticketpaper.demandRandom >= 24 && ticketpaper.demandRandom <= 43) {
                ticketpaper.demand = 70;
            } else if (ticketpaper.demandRandom >= 44 && ticketpaper.demandRandom <= 78) {
                ticketpaper.demand = 80;
            } else {
                ticketpaper.demand = 100;
            }
        } else if (ticketpaper.ticketType.equalsIgnoreCase("Fair")) {
            if (ticketpaper.demandRandom >= 1 && ticketpaper.demandRandom <= 10) {
                ticketpaper.demand = 40;
            } else if (ticketpaper.demandRandom >= 11 && ticketpaper.demandRandom <= 28) {
                ticketpaper.demand = 50;
            } else if (ticketpaper.demandRandom >= 29 && ticketpaper.demandRandom <= 68) {
                ticketpaper.demand = 60;
            } else if (ticketpaper.demandRandom >= 69 && ticketpaper.demandRandom <= 88) {
                ticketpaper.demand = 70;
            } else if (ticketpaper.demandRandom >= 89 && ticketpaper.demandRandom <= 96) {
                ticketpaper.demand = 80;
            } else {
                ticketpaper.demand = 90;
            }
        } else if (ticketpaper.ticketType.equalsIgnoreCase("Poor")) {
            if (ticketpaper.demandRandom >= 1 && ticketpaper.demandRandom <= 44) {
                ticketpaper.demand = 40;
            } else if (ticketpaper.demandRandom >= 45 && ticketpaper.demandRandom <= 66) {
                ticketpaper.demand = 50;
            } else if (ticketpaper.demandRandom >= 67 && ticketpaper.demandRandom <= 82) {
                ticketpaper.demand = 60;
            } else if (ticketpaper.demandRandom >= 83 && ticketpaper.demandRandom <= 94) {
                ticketpaper.demand = 70;
            } else {
                ticketpaper.demand = 80;
            }
        }
    }

    // Simulate the inventory
    public static void simulate(int days, int purchasePerDay, int newPaperPriceBuy, int newPaperPriceSell, int scrapPrice) {
        Tickets.days = days;
        ticket = new Tickets[days];
        for (int i = 0; i < days; i++) {
            ticket[i] = new Tickets();
            ticket[i].profit = 0;
            ticket[i].getDemandRandom(ticket[i]);

            if (ticket[i].demand >= purchasePerDay) { //incase etlab meni aktr mn eli ma3aya

                ticket[i].lostFromShortage = (ticket[i].demand - purchasePerDay) * (newPaperPriceSell - newPaperPriceBuy);
                ticket[i].gainsFromSale = purchasePerDay * newPaperPriceSell;
                ticket[i].gainFromScrap = 0;
                ticket[i].profit = ticket[i].gainsFromSale - (purchasePerDay * newPaperPriceBuy) - ticket[i].lostFromShortage + ticket[i].gainFromScrap;
            } else if (ticket[i].demand < purchasePerDay) { //eltab meni 2a2al mn eli ma3aya

                ticket[i].gainFromScrap = scrapPrice * Math.abs(ticket[i].demand - purchasePerDay);
                ticket[i].gainsFromSale = ticket[i].demand * newPaperPriceSell;
                ticket[i].lostFromShortage = 0;
                ticket[i].profit = ticket[i].gainsFromSale - (purchasePerDay * newPaperPriceBuy) - ticket[i].lostFromShortage + ticket[i].gainFromScrap;
            }
        }
    }

    // Print the inventory simulation table
    public static void printTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows
        for (int i = 0; i < days; i++) {
            model.addRow(new Object[]{
                i + 1, ticket[i].ticketTypeRandom, ticket[i].ticketType, ticket[i].demandRandom, ticket[i].demand, ticket[i].gainsFromSale,
                ticket[i].lostFromShortage, ticket[i].gainFromScrap, ticket[i].profit});
        }
    }

    public static void calStatistics(int p) {
        double TotalProfit = 0;
        for (int i = 0; i < days; i++) {
            TotalProfit += ticket[i].profit;
        }
        System.out.println("Total Profit is : " + TotalProfit + " when they buy " + p + " tickets everday");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Number Of days: ");
        days = sc.nextInt();
        System.out.println("How many Tickets to purchase per Day");
        int purchasePerDay = sc.nextInt();
        System.out.println("Enter Tickets price(you buy)");
        int newPaperPriceBuy = sc.nextInt();
        System.out.println("Enter Tickets price(you sell)");
        int newPaperPriceSell = sc.nextInt();
        System.out.println("Enter Scrap Price");
        int scrapPrice = sc.nextInt();

        simulate(days, purchasePerDay, newPaperPriceBuy, newPaperPriceSell, scrapPrice);
    
        calStatistics(purchasePerDay);
    }
}
