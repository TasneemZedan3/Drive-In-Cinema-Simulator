/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finalprojectmodeling;

public class eventNotice {
    public String type;
    public int clk;

    public eventNotice(String type, int clk) {
        this.type = type;
        this.clk = clk;
    }

    public int getClk() {
        return clk;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "(" + type + "" + clk + ')';
    }
     
    
    
    
}
