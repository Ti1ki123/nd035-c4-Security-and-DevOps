package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private  UserController userController;

    private ItemController itemController;

    private CartController cartController;

    private OrderController orderController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository  = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);

        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);

        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
    }

    @Test
    public void create_user_happy_path() throws Exception {

        when(encoder.encode("testPassword")).thenReturn("Hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void create_user_bad_password() throws Exception {

//        when(encoder.encode("testPassword")).thenReturn("Hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("test");
        createUserRequest.setPassword("tet");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

    }

    @Test
    public void getAllItem() throws Exception {



        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void modifyCartWithouUser() throws Exception {


        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(11);
        modifyCartRequest.setUsername("NotFound");
        modifyCartRequest.setItemId(123);


        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void deleteCartWithouUser() throws Exception {


        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(11);
        modifyCartRequest.setUsername("NotFound");
        modifyCartRequest.setItemId(123);


        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void deleteCartWithouItem() throws Exception {

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response2 = userController.createUser(createUserRequest);

        assertNotNull(response2);
        assertEquals(200,response2.getStatusCodeValue());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(11);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(123);


        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void findHistoryWithouUser() throws Exception {



        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void getItemByName() throws Exception {



        final ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void submit() throws Exception {



        final ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void getko() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(11);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(123);
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }


    @Test
    public void findByuserName() throws Exception {

        final ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }

    @Test
    public void findByuserNameOK() throws Exception {



        final ResponseEntity<User> response2 = userController.findById(11L);

        assertNotNull(response2);
        assertEquals(404,response2.getStatusCodeValue());
        User user = response2.getBody();

        assertNull(user);


//        List<Item> itemList = response.getBody();

//        assertNotNull(itemList);
//        assertEquals(0,user.getId());
//        assertEquals("test",user.getUsername());
//        assertEquals("Hashed",user.getPassword());
    }
}
