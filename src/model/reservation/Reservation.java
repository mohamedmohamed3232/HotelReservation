package model.reservation;

import model.customer.Customer;
import model.room.IRoom;

import java.util.Date;

public class Reservation {
    Customer customer;
    IRoom room;
    Date checInDate;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public Date getChecInDate() {
        return checInDate;
    }

    public void setChecInDate(Date checInDate) {
        this.checInDate = checInDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", room=" + room +
                ", checInDate=" + checInDate +
                '}';
    }
}
