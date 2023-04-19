package service;

import model.customer.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class CustomerService {
    private final Map<String, Customer> customers = new HashMap<>();
    private static final CustomerService SINGLETON = new CustomerService();


    public CustomerService() {
    }
    public static CustomerService getSingleton() {
        return SINGLETON;
    }
    public void addCustomer(String email, String firstName, String lastName){
        customers.put(email, new Customer(firstName,lastName, email));
    };
    public Customer getCustomer(String email) {
    return customers.get(email);
    }
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }


}
