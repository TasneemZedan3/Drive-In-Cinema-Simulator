/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectmodeling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;

public class EventSchedual {

    public static eventtable[] event; //array of obj mn el table el keber
    public static int CustNum;
    public static int cond;
    public static custmoreEvent cust[]; //array of obj mn el table el so8yar
    public static eventNotice e;
    public static ArrayList<eventNotice> FEL = new ArrayList<>();
//-------------------------------------------------------------------------------------------------------------

    public static void simulate() {
//        Random r = new Random(10);
        cust = new custmoreEvent[CustNum];
        cust[0] = new custmoreEvent();
        cust[0].ServT = (int) (Math.random() * (8 - 1) + 1);
        cust[0].IAT = (int) (Math.random() * (8 - 1) + 1);

        cust[0].ArrivT = cust[0].IAT; // arrival awel customer
        cust[0].BeginT = cust[0].ArrivT; // begin awel customer
        cust[0].EndT = cust[0].BeginT + cust[0].ServT; //end awel custmore

        for (int i = 1; i < CustNum; i++) {
            cust[i] = new custmoreEvent(); // Initialize each cust
            cust[i].IAT = (int) (Math.random() * (8 - 1) + 1);
            cust[i].ServT = (int) (Math.random() * (8 - 1) + 1);

            cust[i].ArrivT = cust[i - 1].ArrivT + cust[i].IAT; // arrival el ba2y

            cust[i].BeginT = Math.max(cust[i].ArrivT, cust[i - 1].EndT); // begin el ba2y

            cust[i].EndT = cust[i].BeginT + cust[i].ServT; // end el ba2y

        }

        //-----------------------------------------------------------------------------------------------  
        event = new eventtable[CustNum * 2]; //cause each custmore has and arrival w departuer
        //event = new eventtable[cust[CustNum-1].EndT];
        //initialize each event 3shan my3ml nullpointer excpetion
        for (int i = 0; i < event.length; i++) {
            event[i] = new eventtable();
        }

        event[0].clk = cust[0].ArrivT;//awel clock = awel custmore arrival
        event[0].type = "A";//awel type 3altol byb2a arrivals
        event[0].LS = 1; //LS=1 b3d ma awel cust y5osh

        //pointers 3shan a3rf a-add inside el FEL
        int custIDArriv = 0;
        int custIDDepart = 0;
        for (int i = 0; i < event.length; i++) {
            //custIDArriv bet-loop 3ala el  arrivTime
            if (custIDArriv < cust.length) { //tol ma fe arrival yed5alha fe el FEL as A
                FEL.add(new eventNotice("A", cust[custIDArriv].ArrivT));
                custIDArriv++;
            } else {//yeba2a dah departuer w yed5lha D gowa el FEL
                FEL.add(new eventNotice("D", cust[custIDDepart].EndT));
                custIDDepart++;
            }

        }

        algorthim();

        custIDArriv = 0;
        custIDDepart = 0;
        int spentTime = 0;
        int MQ = 0;
        int F = 0;
        int N = 0;
        int B = 0;
        //calculate  S,N,F,MQ
        int Anumber = 1;
        int Dnumber = 1;
        for (int i = 0; i < event.length; i++) { //1
            String custType;
            if (event[i].type.equals("A")) {
                custType = event[i].type + Anumber;
                Anumber++;
            } else {
                custType = event[i].type + Dnumber;
                Dnumber++;
            }

            event[i].type = custType.intern();//b7welha string

            if (event[i].type.contains("D")) {
                spentTime += cust[custIDDepart].EndT - cust[custIDArriv].ArrivT;
            }
            event[i].S = spentTime;

            if (cust[custIDDepart].EndT - cust[custIDArriv].ArrivT >= cond && event[i].type.contains("D")) { //low el time spent akbr mn el el condition
                F = F + 1;
                custIDDepart++;
                custIDArriv++;
            } else if (event[i].type.contains("D")) {
                custIDDepart++;
                custIDArriv++;
            }
            event[i].F = F;

            if (event[i].type.contains("D")) {
                N++;
            }
            event[i].N = N;

            if (event[i].LQ > MQ) {
                MQ = event[i].LQ;
                //event[i].MQ = Math.max(event[i].LQ, MQ);
            }
            event[i].MQ = MQ;

            //CALULATE EL B
            if (i > 0) {
                if ((event[i].LS == 1 && event[i - 1].LS == 1) || (event[i].LS == 0 && event[i - 1].LS == 1)) {
                    B += event[i].clk - event[i - 1].clk;

                }
                event[i].B = B;
            }
        }

    }
//-------------------------------------------------------------------------------------------------------------
    //FEL W CLK

    public static void algorthim() {
        // int custIDArriv = 0;
        //int custIDDepart = 0;
        for (int i = 0; i < event.length; i++) {

          //  if (i > 0) { //tol ma e7na mesh awel event 3shan awel event(clk,type) already et3mal fo2
                // get el type w el clk bt3 ba2y el events mn el FEL bt3 el event eli abli
                event[i].type = FEL.get(i).getType(); //i-1 ,i
                event[i].clk = FEL.get(i).getClk(); //i-1,i
         //}
            //sort el FEL 3ala 7asabe el clk
//       Collections.sort(event[i].FEL, (event1, event2) -> Integer.compare(event1.clk, event2.clk));
            Collections.sort(FEL, Comparator.comparingInt(eventNotice::getClk));
            //calculate el LS w el LQ @arrival 
            if (event[i].type.equals("A")) {
                if (i == 0) { //awel event el LS=1 w LQ=0
                    event[i].LQ = 0;
                    event[i].LS = 1;
                } else if (event[i - 1].LS == 1) { //ba2y el events low el server at el event eli abli sha8al
                    event[i].LQ = event[i - 1].LQ + 1; //hanzwed one 3ala el LQ bt3t abl keda
                    event[i].LS = 1; //hyfdal busy
//                    FEL.add(new eventNotice("A", cust[custIDArriv].ArrivT));
//                    custIDArriv++;
                } else { //low el el LS maknetsh busy 
                    event[i].LS = 1; //ha5leha busy
                }
            }
            //calculate el LS w el LQ @departuer
            if (event[i].type.equals("D")) {
                if (event[i - 1].LQ > 0) { //low el queu feha aktr mn 7ad
                    event[i].LQ = event[i - 1].LQ - 1; //hashelo 3shan fe 7ad meshy
                    event[i].LS = 1; //keep el server busy 3shan lesa fe 7ad fe el queu 3shan akbr mn zero
//                    event[i].FEL.add(new eventNotice("D", cust[custIDDepart].EndT));
//                    custIDDepart++;
                } else { //=0 fa keda el kolo meshy fa el server idle
                    event[i].LS = 0;
                }
            }

        }

    }

    public static void calcStatics() {
        double AvgResponseTime = 0;
        AvgResponseTime = event[(CustNum * 2) - 1].S / event[(CustNum * 2) - 1].N;
        System.out.println("Average Response Time = " + AvgResponseTime);
    }

    public static void printTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows

        for (int i = 0; i < event.length; i++) {
            model.addRow(new Object[]{
                event[i].clk, event[i].type, String.valueOf(event[i].LS), event[i].LQ, FEL.get(i), FEL.get(i), event[i].B, event[i].MQ, event[i].S, event[i].N, event[i].F});

        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Number of Customers: ");
        CustNum = sc.nextInt();

        System.out.print("Enter Contion to calculate F : ");
        cond = sc.nextInt();

        simulate();
        calcStatics();
    }
}
