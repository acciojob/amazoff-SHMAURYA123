package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderdB=new HashMap<>();
    HashMap<String,DeliveryPartner> deliverdB=new HashMap<>();
    HashMap<String, String> OrderPartnerdB=new HashMap<>();
    HashMap<String, List<String>> PartnerOrderListdB=new HashMap<>();

    public  void addPartner(String partnerId) {
        deliverdB.put(partnerId,new DeliveryPartner(partnerId));
    }

    public  void addOrder(Order order) {
      String key= order.getId();
      orderdB.put(key,order);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        OrderPartnerdB.put(orderId,partnerId);
        List<String> orderlist=new ArrayList<>();
        if(PartnerOrderListdB.containsKey(partnerId)){
            orderlist=PartnerOrderListdB.get(partnerId);
        }
        orderlist.add(orderId);
        PartnerOrderListdB.put(partnerId,orderlist);

        //now update the number of order count of that particular partner
        DeliveryPartner deliveryPartner = deliverdB.get(partnerId);
        deliveryPartner.setNumberOfOrders(orderlist.size());
 }

    public Order getOrderByID(String orderId) {
        return orderdB.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliverdB.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        List<String> orderlist= new ArrayList<>();
        orderlist=PartnerOrderListdB.get(partnerId);
        return orderlist.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orderlist= new ArrayList<>();
        orderlist=PartnerOrderListdB.get(partnerId);
        return orderlist;
    }

    public List<String> getAllOrders() {
        List<String> orderlist= new ArrayList<>();
        for(String l:orderdB.keySet()){
            orderlist.add(l);
        }
        return orderlist;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderdB.size() -OrderPartnerdB.size();
   }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
       List<String> orderlist=PartnerOrderListdB.get(partnerId);
       int count=0;
       for(String l:orderlist){
           int t=orderdB.get(l).getDeliveryTime();
           if(t>Integer.parseInt(time)){
               count++;
           }
       }
       return Integer.valueOf(count);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        List<String> orderlist=PartnerOrderListdB.get(partnerId);
        int maxTime=0;
        for(String l:orderlist){
            int currTime=orderdB.get(l).getDeliveryTime();
            maxTime=Math.max(maxTime,currTime);
        }
        String hh=Integer.toString(maxTime/60);
        String mm=Integer.toString(maxTime%60);
        if(hh.length()==1){
            hh='0'+hh;
        }
        else if(mm.length()==1){
            mm='0'+mm;
        }
        String ans=hh+':'+mm;
        return ans;
    }

    public void deletePartnerById(String partnerId) {
        PartnerOrderListdB.remove(partnerId);
    List<String> olist=new ArrayList<>(OrderPartnerdB.keySet());
       for(String l:olist ){
           if(OrderPartnerdB.get(l)==partnerId){
               OrderPartnerdB.remove(l);
           }
       }
    }

    public void deleteOrderById(String orderId) {
     if(orderdB.containsKey(orderId)){
         String partnerId=OrderPartnerdB.get(orderId);
         if(partnerId!=null){
             OrderPartnerdB.remove(orderId);
             PartnerOrderListdB.get(partnerId).remove(orderId);
             int currOrder=deliverdB.get(partnerId).getNumberOfOrders();
             deliverdB.get(partnerId).setNumberOfOrders(currOrder-1);
         }
         orderdB.remove(orderId);
     }


    }
}
