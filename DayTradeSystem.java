/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package StockSim;


import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import GenCol.entity;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;



public class DayTradeSystem extends ViewableDigraph{
	
	private ArrayList<String> stockSymbols;
	private Map <String, Map> stocksMap;


public DayTradeSystem(String name){
    super(name);
    ReadExcel stockReader = new ReadExcel();
	try {
		stocksMap    = stockReader.read();
		stockSymbols = stockReader.getStockSymbolList();
	} catch (IOException e) {
		e.printStackTrace();
	}
	DayTradeSystemConstruct(1,100);
}

public DayTradeSystem(String nm,double int_arr_t,double observe_t){
    super(nm);
    DayTradeSystemConstruct(int_arr_t,observe_t);
}

public void initialize(){
	
	
	super.initialize();
}

public void DayTradeSystemConstruct(double int_arr_t,double observe_t){
	 addInport("InPrices");
	 addInport("InState");
     addOutport("OutTrendBalance");
     addOutport("OutScalpBalance");
	
     ViewableAtomic macd  = new MACD("MACD");
     ViewableAtomic dtt   = new StockDayTradeTrend("DayTradeTrend", stockSymbols);
     ViewableAtomic dts   = new StockDayTradeScalp("DayTradeScalp", stockSymbols);

     add(macd);
     add(dtt);
     add(dts);

     initialize();

     addCoupling(this,"InPrices",macd,"InPrices");
     addCoupling(this,"InState",macd,"InState");
     
     addCoupling(this,"InPrices",dtt,"InPrices");
     addCoupling(this,"InState",dtt,"InState");
     addCoupling(macd,"MACD",dtt,"InMACD");
     addCoupling(macd,"Signal",dtt,"InSignal");
     
     addCoupling(this,"InPrices",dts,"InPrices");
     addCoupling(this,"InState",dts,"InState");
     addCoupling(macd,"MACD",dts,"InMACD");
     addCoupling(macd,"Signal",dts,"InSignal");
     

     addCoupling(dtt,"BuySellBalance",this,"OutTrendBalance");
     
     addCoupling(dts,"BuySellBalance",this,"OutScalpBalance");

     preferredSize = new Dimension(600, 350);
     macd.setPreferredLocation(new Point(15, 100));
     dtt.setPreferredLocation(new Point(154, 267));
     dts.setPreferredLocation(new Point(154, 267));
}


    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(600, 342);
        ((ViewableComponent)withName("DayTradeScalp")).setPreferredLocation(new Point(156, 53));
        ((ViewableComponent)withName("MACD")).setPreferredLocation(new Point(-3, 141));
        ((ViewableComponent)withName("DayTradeTrend")).setPreferredLocation(new Point(151, 229));
    }
}