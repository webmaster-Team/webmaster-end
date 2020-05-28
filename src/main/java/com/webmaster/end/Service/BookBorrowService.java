package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;
import com.webmaster.end.IMapper.IRentalMapper;
import com.webmaster.end.Utils.ImageUtil;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookBorrowService extends BookServiceCore {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected  OrderService orderService;


    /**
     * 借书任务
     * @param order 订单
     * @return String
     */
    @Transactional
    public Map<String, Object> borrow(Order order) {
        try {
            //1.用户是否存在
            if (iUserMapper.isExist(order.getUserId())) {
                for (Book book : order.getBooks()) {
                    Boolean exist = iBookMapper.isExist(book.getId());
                    if(exist) {
                        boolean exist1 = iRentalMapper.isExistByUserBook(book.getId(), order.getUserId());
                        if(exist1)
                            return ResultMap.getResultMap(null,"用户已经借阅过该书");
                    }
                    else
                        return ResultMap.getResultMap(null,"书籍不存在");
                }
                //生成对应的二维码
                Map<String, Object> qrCodeData = ImageUtil.createQRCode(order.getSerial());
                String qrcode= (String) qrCodeData.get("state");
                if(qrcode!=null){
                    order.setQrcode(qrcode);
                    //所有书籍都存在的过程,且当前没有借阅过
                    Map<String, Object> orderData = orderService.addOrder(order);
                    boolean flag= (boolean) orderData.get("state");
                    if(flag)
                        return ResultMap.getResultMap(order.getSerial(),"借书完成");
                        //如果添加失败，就返回对应的内容
                    else
                        return ResultMap.getResultMap(null,(String)orderData.get("msg"));
                }
                else
                    return ResultMap.getResultMap(null,(String)qrCodeData.get("msg"));
            } else
                return ResultMap.getResultMap(null, "用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null, "系统内部错误");
        }
    }
}
