package nl.tudelft.sem.yumyumnow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.yumyumnow.database.TestOrderRepository;
import nl.tudelft.sem.yumyumnow.database.TestRatingRepository;
import nl.tudelft.sem.yumyumnow.model.Customer;
import nl.tudelft.sem.yumyumnow.model.Order;
import nl.tudelft.sem.yumyumnow.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnalyticsServiceTest {

    private TestRatingRepository testRatingRepository;
    private TestOrderRepository testOrderRepository;
    private CustomerService customerService;
    private AnalyticsService analyticsService;
    private VendorService vendorService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.customerService = mock(CustomerService.class);
        this.vendorService = Mockito.mock(VendorService.class);
        this.orderService = Mockito.mock(OrderService.class);
        this.testRatingRepository = new TestRatingRepository();
        this.testOrderRepository = new TestOrderRepository();
        this.analyticsService = new AnalyticsService(customerService,
            testRatingRepository, orderService, testOrderRepository);
    }

    @Test
    public void testEmptyRepo() {
        Long id = 420L;
        assertEquals(this.analyticsService.getRatingById(id), Optional.empty());

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("findById", this.testRatingRepository.getMethodCalls().get(0));
    }

    @Test
    public void testAddDish() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("save", this.testRatingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
    }

    @Test
    public void testGetRatingById() {
        Rating rating = new Rating();
        rating.setComment("The pizza was great!");
        Rating savedRating = this.analyticsService.createNewRating(rating);

        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals("save", this.testRatingRepository.getMethodCalls().get(0));

        Optional<Rating> retrievedRating = this.analyticsService.getRatingById(savedRating.getId());
        assertEquals(2, this.testRatingRepository.getMethodCalls().size());
        assertEquals("findById", this.testRatingRepository.getMethodCalls().get(1));
        assertEquals(rating.getComment(), retrievedRating.get().getComment());
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

    @Test
    public void getOrdersPerMonthTestNullCustomer() {
        when(customerService.getCustomer(13L)).thenReturn(null);
        assertNull(this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestNullOrders() {
        Customer customer = new Customer();
        List<Order> orderList = null;
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertNull(this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestTestAllMonths() {
        String time1 = "2022-01-01T12:00:00+02:00";
        Order order1 = new Order();
        order1.setTime(OffsetDateTime.parse(time1));

        String time2 = "2022-02-01T12:00:00+02:00";
        Order order2 = new Order();
        order2.setTime(OffsetDateTime.parse(time2));

        String time3 = "2022-03-01T12:00:00+02:00";
        Order order3 = new Order();
        order3.setTime(OffsetDateTime.parse(time3));

        String time4 = "2022-04-01T12:00:00+02:00";
        Order order4 = new Order();
        order4.setTime(OffsetDateTime.parse(time4));

        String time5 = "2022-05-01T12:00:00+02:00";
        Order order5 = new Order();
        order5.setTime(OffsetDateTime.parse(time5));

        String time6 = "2022-06-01T12:00:00+02:00";
        Order order6 = new Order();
        order6.setTime(OffsetDateTime.parse(time6));

        String time7 = "2022-07-01T12:00:00+02:00";
        Order order7 = new Order();
        order7.setTime(OffsetDateTime.parse(time7));

        String time8 = "2022-08-01T12:00:00+02:00";
        Order order8 = new Order();
        order8.setTime(OffsetDateTime.parse(time8));

        String time9 = "2022-09-01T12:00:00+02:00";
        Order order9 = new Order();
        order9.setTime(OffsetDateTime.parse(time9));

        String time10 = "2022-10-01T12:00:00+02:00";
        Order order10 = new Order();
        order10.setTime(OffsetDateTime.parse(time10));

        String time11 = "2022-11-01T12:00:00+02:00";
        Order order11 = new Order();
        order11.setTime(OffsetDateTime.parse(time11));

        String time12 = "2022-12-01T12:00:00+02:00";
        Order order12 = new Order();
        order12.setTime(OffsetDateTime.parse(time12));

        List<Order> orderList = List.of(order1, order2, order3, order4, order5, order6,
            order7, order8, order9, order10, order11, order12);
        Customer customer = new Customer();
        customer.setId(13L);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(1.0, this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void getOrdersPerMonthTestTestSomeMonths() {
        String time1 = "2022-01-01T12:00:00+02:00";
        Order order1 = new Order();
        order1.setTime(OffsetDateTime.parse(time1));

        String time2 = "2022-01-01T12:00:00+02:00";
        Order order2 = new Order();
        order2.setTime(OffsetDateTime.parse(time2));

        String time3 = "2022-03-01T12:00:00+02:00";
        Order order3 = new Order();
        order3.setTime(OffsetDateTime.parse(time3));

        String time4 = "2021-05-01T12:00:00+02:00";
        Order order4 = new Order();
        order4.setTime(OffsetDateTime.parse(time4));

        String time5 = "2021-05-01T12:00:00+02:00";
        Order order5 = new Order();
        order5.setTime(OffsetDateTime.parse(time5));

        List<Order> orderList = List.of(order1, order2, order3, order4, order5);
        Customer customer = new Customer();
        customer.setId(13L);
        customer.setPastOrders(orderList);
        when(customerService.getCustomer(13L)).thenReturn(customer);
        assertEquals(5.0 / 3.0, this.analyticsService.getOrdersPerMonth(13L));
    }

    @Test
    public void setOrderRatingTestException() {
        Order order = null;
        Rating rating = new Rating();
        when(this.orderService.getOrderById(12L)).thenThrow(NoSuchElementException.class);
        this.analyticsService.setOrderRating(12L, rating);
        assertEquals(0, this.testRatingRepository.getMethodCalls().size());
        assertEquals(0, this.testOrderRepository.getMethodCalls().size());
    }

    @Test
    public void setOrderRatingTestNullOrder() {
        Order order = null;
        Rating rating = new Rating();
        when(this.orderService.getOrderById(12L)).thenReturn(null);
        this.analyticsService.setOrderRating(12L, rating);
        assertEquals(0, this.testRatingRepository.getMethodCalls().size());
        assertEquals(0, this.testOrderRepository.getMethodCalls().size());
    }

    @Test
    public void setOrderRatingTestNullRating() {
        Order order = new Order();
        Rating rating = null;
        when(this.orderService.getOrderById(12L)).thenReturn(order);
        this.analyticsService.setOrderRating(12L, rating);
        assertEquals(0, this.testRatingRepository.getMethodCalls().size());
        assertEquals(0, this.testOrderRepository.getMethodCalls().size());
    }

    @Test
    public void setOrderRatingTestNullId() {
        Order order = new Order();
        Rating rating = new Rating();
        rating.setId(null);
        when(this.orderService.getOrderById(12L)).thenReturn(order);
        this.analyticsService.setOrderRating(12L, rating);
        assertEquals(0, this.testRatingRepository.getMethodCalls().size());
        assertEquals(0, this.testOrderRepository.getMethodCalls().size());
    }

    @Test
    public void setOrderRatingTestValid() {
        Order order = new Order();
        order.setRatingId(2L);
        order.setOrderId(this.testOrderRepository.count());
        Rating rating = new Rating();
        rating.setId(2L);
        when(this.orderService.getOrderById(12L)).thenReturn(order);
        this.analyticsService.setOrderRating(12L, rating);
        assertEquals(1, this.testRatingRepository.getMethodCalls().size());
        assertEquals(Optional.of(rating), this.testRatingRepository.findById(rating.getId()));
        assertEquals(2, this.testOrderRepository.getMethodCalls().size());
        assertEquals("save", this.testOrderRepository.getMethodCalls().get(1));
        assertEquals(rating.getId(), this.testOrderRepository.findById(order.getOrderId()).get().getRatingId());
    }
}
