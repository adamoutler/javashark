/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.cats.battlefield.building.car;

/**
 *
 * @author adamo
 */
public class TieBreaker {
    int x=0;
    int y=0;
    int color=0;
    
    public TieBreaker(Car car){
        String[] breaker=car.getTieBreaker().split("/");
        if (breaker.length==3){
            x=Integer.parseInt(breaker[0]);
            y=Integer.parseInt(breaker[0]);
            color=Integer.parseInt(breaker[0]);
        }
    }
    @Override
    public int hashCode(){
        int tiebreaker=12893181;
        tiebreaker=tiebreaker+(201*x)+
                (23549*y)+
                (12912471*color);
        return tiebreaker;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TieBreaker other = (TieBreaker) obj;
        if (this.hashCode()==other.hashCode()){
            return true;
        }
        int xhigh=this.x+1;
        int xlow=this.x-1;
        int yhigh=this.x+1;
        int ylow=this.x-1;
        if ((other.x<xhigh && other.x>xlow) && (other.y<yhigh && other.y>ylow)){
            return true;
        }
       
        return false;
    }
    
    
}
