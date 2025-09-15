package api.bookstore.order_service.utils;

import api.bookstore.order_service.models.Order;
import api.bookstore.order_service.models.OrderDTO;
import org.springframework.beans.BeanUtils;

public class OrderUtil {

    public static OrderDTO toDto(Order dao){
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(dao, dto);
        return dto;
    }

    public static Order toDao(OrderDTO dto){
        Order dao = new Order();
        BeanUtils.copyProperties(dto, dao);
        return dao;
    }
}
