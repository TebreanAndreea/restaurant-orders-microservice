package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnalyticsServiceTest {
    private CustomerService customerService;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        this.analyticsService = new AnalyticsService(customerService);
    }

    @Test
    public void getCustomerAveragePriceTestValid() {
        Customer customer = new Customer();
        customer.setId(13L);
        Order order1 = new Order();
        order1.setPrice(10.1);
        Order order2 = new Order();
        order2.setPrice(12.3);
        Order order3 = new Order();
        order3.setPrice(50.67);
        Order order4 = new Order();
        order4.setPrice(10.9);
        Order order5 = new Order();
        order5.setPrice(34.78);
        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(23.75, this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullCustomer() {
        when(customerService.getCustomer(13L)).thenReturn(null);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullOrders() {
        Customer customer = new Customer();
        List<Order> orderList = null;
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }

    @Test
    public void getCustomerAveragePriceTestNullPrice() {
        Customer customer = new Customer();
        customer.setId(13L);
        Order order1 = new Order();
        order1.setPrice(10.1);
        Order order2 = new Order();
        order2.setPrice(12.3);
        Order order3 = new Order();
        order3.setPrice(50.67);
        Order order4 = new Order();
        order4.setPrice(null);
        Order order5 = new Order();
        order5.setPrice(34.78);
        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getCustomerAveragePrice(13L));
    }


}
