package com.d2c.order.business.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.d2c.order.business.client.MemberClient;
import com.d2c.order.business.client.ProductClient;
import com.d2c.order.business.mapper.OrderMapper;
import com.d2c.order.business.model.Order;
import com.d2c.order.business.service.OrderService;
import com.d2c.order.elasticsearch.document.OrderSearch;
import com.d2c.order.elasticsearch.repository.OrderSearchRepository;
import com.d2c.order.mongodb.document.OrderMongo;
import com.d2c.order.mongodb.repository.OrderMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberClient memberClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderMongoRepository orderMongoRepository;
    @Autowired
    private OrderSearchRepository orderSearchRepository;

    @Override
    public Order findBySn(String sn) {
        Order order = orderMapper.findBySn(sn);
        redisTemplate.opsForValue().set("Order::order:" + sn, order);
        orderMongoRepository.save(new OrderMongo(order));
        orderSearchRepository.save(new OrderSearch(order));
        return order;
    }

    @Override
    @Cacheable(value = "Order", key = "'order:'+#sn", unless = "#result == null")
    public Order findCacheBySn(String sn) {
        return null;
    }

    @Override
    public List<OrderMongo> findMongoBySn(String sn) {
        return orderMongoRepository.findBySn(sn);
    }

    @Override
    public List<OrderSearch> findSearchBySn(String sn) {
        return orderSearchRepository.findBySn(sn);
    }

    @Override
    @LcnTransaction
    @Transactional
    public int doSomeThing(String sn, Long productId, Long memberId) {
        int rs1 = orderMapper.updateAmountBySn(sn, new BigDecimal((int) (Math.random() * 100 + 1)));
        int rs2 = memberClient.updatePasswdById(memberId, String.valueOf((int) (Math.random() * 100 + 1)));
        int rs3 = productClient.updatePriceById(productId, new BigDecimal((int) (Math.random() * 100 + 1)));
        //return rs1 + rs2 + rs3;
        throw new RuntimeException("doSomeThing更新失败");
    }

}
