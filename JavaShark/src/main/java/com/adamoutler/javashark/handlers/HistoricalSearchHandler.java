/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.javashark.handlers;

import com.adamoutler.records_search.BattleRecordSearch;
import com.adamoutler.records_search.BattlefieldSearchParser;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import com.babysharks.messaging.SearchQuery;

/**
 *
 * @author adamo
 */
public class HistoricalSearchHandler extends AbstractPapaHandler{

    @Override
    public void handle(HttpExchange he) throws IOException {
        SearchQuery sq=request.getData().getQuery();
        new BattlefieldSearchParser(sq);
        
        
    }
    
    
    
    
    
    
    
}
