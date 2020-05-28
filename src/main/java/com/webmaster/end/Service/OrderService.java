package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;
import com.webmaster.end.IMapper.IBookMapper;
import com.webmaster.end.IMapper.IOrderMapper;
import com.webmaster.end.IMapper.IRentalMapper;
import com.webmaster.end.IMapper.IUserMapper;
import com.webmaster.end.Utils.MyDateUtil;
import com.webmaster.end.Utils.MyRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IOrderMapper iOrderMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IRentalMapper iRentalMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IUserMapper iUserMapper;


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IBookMapper iBookMapper;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private MyRedisUtil redisUtil;

    /**
     * 3分钟后运行该方法将对应的书单推送到数据库中
     * @param serial 书单的编号
     * @return
     */
    public String startRedisTimeTask(String serial){
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = pushOrdertoDB(serial);
                boolean result= (boolean) map.get("state");
                //如果推送失败就抛出异常
                if(!result)
                    throw new RuntimeException();
            }
        }, new Date(MyDateUtil.getCurrentTime()+1000*10));//先设置为10s，查看效果
        return "创建完毕";
    }


    /**
     * 判断是否存在该书单
     * @param serial 订单的编号
     * @return booleanl类型
     */
    public Map<String,Object> isExist(String serial){
        try {
            boolean flag = redisUtil.get(serial) != null ? true : false;
            if (flag)
                return ResultMap.getResultMap(true, "缓存存在该书单");
            else {
                boolean b = iOrderMapper.isExistBySerial(serial);
                if (b)
                    return ResultMap.getResultMap(true, "数据库存在该书单");
                else
                    return ResultMap.getResultMap(false, "不存在该书单");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false, "系统内部错误");
        }
    }


    /**
     * 添加一个书单信息到redis中
     * @param order 书单的信息
     * @return boolean
     */
    public Map<String,Object> addOrder(Order order){
        try{
            redisUtil.putOrder(order);
            //添加成功，开启定时任务
            startRedisTimeTask(order.getSerial());
            return ResultMap.getResultMap(true,"添加书单成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"添加书单到缓存失败");
        }
    }

    /**
     * 根据订单编号返回对应的订单
     * @param serail 订单编号
     * @return Order
     */
    public Map<String,Object> getOrder(String serail){
        try{
            Order order = redisUtil.getOrder(serail);
            if(order!=null)
                return ResultMap.getResultMap(order,"缓存中有此订单直接返回");
            else{
                order = new Order();
                order.setId(iOrderMapper.getOrderIdBySerial(serail));
                Order trueOrder = iOrderMapper.getOrder(order.getId());
                if(trueOrder!=null)
                    return ResultMap.getResultMap(trueOrder,"获取订单成功");
                else
                    return ResultMap.getResultMap(null,"该订单不存在");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null, "系统内部错误");
        }
    }


    /**
     * 根据用户的id来获得该用户所有的订单信息
     * @param userId 用户id
     * @return List<Order>
     */
    public  Map<String,Object> getOrdersByUserId(int userId){
        try {
            List<Order> orders = iOrderMapper.getSimpleOrdersByUserId(userId);
            if(orders!=null){
                for (Order order : orders) {
                    List<Book> books = iBookMapper.getBooksByOrderId(order.getId());
                    if(books!=null)
                        order.setBooks(books);
                    else
                        return ResultMap.getResultMap(null,"获取对应书籍失败");
                }
                return ResultMap.getResultMap(orders,"获取该用户的所有订单信息成功");
            }
            else
                return ResultMap.getResultMap(null,"获得简单的订单内容");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false, "系统内部错误");
        }
    }





    /**
     * 取消书单，修改redis中书单的状态
     * @param serial 书单的编号
     * @return boolean
     */
    @Transactional
    public Map<String,Object> cancelOrder(String serial){
        try{
            Order order = redisUtil.getOrder(serial);
            if(order!=null) {
                User user = iUserMapper.getUser(order.getUserId());
                if (user != null) {
                    //设置订单状态为取消预约
                    order.setState(4);
                    //重新设置数值，由于在第一次添加的时候有触发器了，所以这个地方不用添加定时任务
                    redisUtil.putOrder(order);
                    return ResultMap.getResultMap(true, "书单取消成功");
                } else
                    return ResultMap.getResultMap(false, "该用户不存在");
            } else
                return ResultMap.getResultMap(false, "缓存中无该书单");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false, "系统内部错误");
        }
    }



    /**
     * 将Redis中的Order推送到数据库中
     * @param serial 订单的编号
     * @return boolean
     */
    @Transactional
    public Map<String,Object> pushOrdertoDB(String serial){
        try{
            Order order = redisUtil.getOrder(serial);
            if(order!=null) {
                User user = iUserMapper.getUser(order.getUserId());
                if(user!=null) {
                    //添加书单
                    boolean b = iOrderMapper.addOrder(order);
                    //添加所有的流水
                    if (b) {
                        //获得OrderId
                        order.setId(iOrderMapper.getOrderIdBySerial(serial));


                        //非取消订单的情况
                        if(order.getState()!=4) {
                            List<Rental> rentals = new ArrayList<>();
                            for (Book book : order.getBooks()) {
                                Rental rental = new Rental();
                                rental.setBookId(book.getId());
                                rental.setUserId(order.getUserId());
                                rental.setBorrowTime(order.getCreateTime());
                                rental.setDuration(user.getIdentity() == 0 ? 30 : 180);
                                rental.setIsReborrow(0);
                                rentals.add(rental);
                            }
                            //添加流水
                            boolean b1 = iRentalMapper.addAllRentals(rentals);
                            //给所有添加后的流水添加id
                            for (Rental rental : rentals)
                                rental.setId(iRentalMapper.getRentalId(rental.getBookId(),rental.getUserId()));
                            if(b1) {
                                //添加联系
                                boolean b2 = iOrderMapper.addOrderAssoic(order.getId(), rentals);
                                if (b2){
                                    redisUtil.remove(order.getSerial());
                                    return ResultMap.getResultMap(true, "推送书单成功");
                                }
                                else {
                                    //添加失败就回滚
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    return ResultMap.getResultMap(false, "添加联系失败");
                                }
                            }
                            else {
                                //添加失败就回滚
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return ResultMap.getResultMap(false, "推送书单失败");
                            }
                        }


                        //如果是一个已经取消的订单
                        else{
                            List<Rental> rentals=new ArrayList<>();
                            for (Book book : order.getBooks()) {
                                Rental rental=new Rental();
                                rental.setId(0);
                                rental.setBookId(book.getId());
                                rentals.add(rental);
                            }
                            //添加联系
                            boolean b1 = iOrderMapper.addOrderAssoic(order.getId(), rentals);
                            if (b1){
                                redisUtil.remove(order.getSerial());
                                return ResultMap.getResultMap(true, "推送书单成功");
                            }
                            else {
                                //添加失败就回滚
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return ResultMap.getResultMap(false, "添加联系失败");
                            }
                        }


                    } else
                        return ResultMap.getResultMap(false, "添加书单失败");
                }else
                    return ResultMap.getResultMap(false, "该用户不存在");
            }
            else
                return ResultMap.getResultMap(false, "缓存中无该书单");
        }catch (Exception e){
            e.printStackTrace();
            //添加失败就回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultMap.getResultMap(false, "系统内部错误");
        }
    }





}
