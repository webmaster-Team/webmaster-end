package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.Order;
import com.webmaster.end.Service.OrderService;
import com.webmaster.end.Utils.LoginAccess;
import com.webmaster.end.Utils.MyJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 根据用户id返回该用户的所有订单的简略信息
     * @param session
     * @return
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("getSimpleOrders")
    public String getSimpleOrders(HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                Map<String, Object> orderData = orderService.getOrdersByUserId(userId);
                List<Order> orders = (List<Order>) orderData.get("state");
                if (orders != null) {
                    JSONObject result = new JSONObject(true);
                    result.put("result", 1);
                    JSONArray array = new JSONArray();
                    for (Order order : orders) {
                        JSONObject temp = MyJsonConverter.convertSimpleOrderToJson(order);
                        array.add(temp);
                    }
                    result.put("data", array);
                    return result.toJSONString();
                } else
                    return MyJsonConverter.convertErrorToJson(orderData).toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("类型转换异常").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 返回对应订单的详细信息
     * @param map 传递的数据集
     * @param session
     * @return
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("getOrderData")
    public String getOrderData(@RequestBody Map<String,String > map,HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                String serial = map.get("serial");
                if(serial!=null) {
                    Map<String, Object> orderData = orderService.getOrder(serial);
                    Order order = (Order)orderData.get("state");
                    if(order!=null){
                        JSONObject result = new JSONObject(true);
                        result.put("result",1);
                        JSONObject jsonObject = MyJsonConverter.convertComplexOrderToJson(order);
                        result.put("data",jsonObject);
                        return result.toJSONString();
                    }else
                        return MyJsonConverter.convertErrorToJson(orderData).toJSONString();
                }else
                    return MyJsonConverter.createErrorToJson("订单编号不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("类型转换异常").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 取消对应的书单
     * @param map 数据集
     * @param session
     * @return 返回是否成功
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("cancelOrder")
    public String cancelOrder(@RequestBody Map<String,String > map,HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            String serial = map.get("serial");
            if(userId!=null){
                if(serial!=null){
                    Map<String, Object> orderData = orderService.cancelOrder(serial);
                    boolean flag= (boolean) orderData.get("state");
                    if(!flag)
                        return MyJsonConverter.createSuccessrToJson("取消书单成功").toJSONString();
                    else
                        return MyJsonConverter.createErrorToJson("取消书单失败").toJSONString();
                }
                else {
                    return MyJsonConverter.createErrorToJson("订单的编号不能未空").toJSONString();
                }
            }
            else
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }
}
