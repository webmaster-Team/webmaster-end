package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.Order;
import com.webmaster.end.Entity.Rental;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOrderMapper {
    /**
     * 增加一条书单
     * @param order 书单
     * @return 返回是否增加成功
     */
    public boolean addOrder(Order order);

    /**
     * 根据对应的订单编号获得Order的id
     * @param serial 订单编号
     * @return 返回OrderId
     */
    public int getOrderIdBySerial(String serial);

    /**
     * 增加对应的关系表数据以及根据情况增加流水
     * @param orderId 书单的id
     * @param rental 流水对象
     * @return 返回是否添加成功
     */
    public boolean addOrderAssoic(@Param("id") int orderId, @Param("rentals") List<Rental> rental);


    /**
     * 获得一条订单
     * @param id 书单的id
     * @return 返回对应的书单
     */
    public Order getOrder(int id);


    /**
     * 获得某个用户的所有订单
     * @param usrId 用户的id
     * @return 返回对应的书单
     */
    public List<Order> getSimpleOrdersByUserId(int usrId);




//    /**
//     * 添加全部的订单
//     * @param orders 订单集
//     * @return 返回是否成功
//     */
//    public boolean addAllOrders(List<Order> orders);

    /**
     * 更新完成时间
     * @param orderId 书单的id
     * @param completeTime 完成时间
     * @return 更新完成的时间
     */
    public boolean updateCompleteTime(@Param("orderId") int orderId,@Param("completeTime") String completeTime);


    /**
     * 获得对应的书单的状态
     * @param orderId 书单的ID
     * @return 返回对应的状态
     */
    public int getSate(int orderId);




}
