package menu;

import api.HotelResource;
import model.reservation.Reservation;
import model.room.IRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final String DEFAULT_DATES = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getSingleton();

    public static void mainMenu() {
        String line = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try {
            do {
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1':
                            findAndReserveRoom();
                            break;
                        case '2':
                            seeMyReservation();
                            break;
                        case '3':
                            createAccount();
                            break;
                        case '4':
                            AdminMenu.adminMenu();
                            break;
                        case '5':
                            System.out.println("Exit");
                            break;
                        default:
                            System.out.println("Unknown error please select a number from the menu\n");
                            break;
                    }
                } else {
                    System.out.println("Error: Invalid selection please choose a number from our menu\n");
                }
            } while (line.charAt(0) != '5' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Empty input received. Exiting program...");
        }
    }

    private static void findAndReserveRoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter Check-In Date in mm/dd/yyyy format");
        Date checkIn = getInputDate(scanner);

        System.out.println("Please Enter Check-Out Date in mm/dd/yyyy format");
        Date checkOut = getInputDate(scanner);

        if (checkIn != null && checkOut != null) {
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("No rooms found.");
                } else {
                    final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkIn);
                    final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOut);
                    System.out.println("Open rooms in these alternate dates:" +
                            "\nCheck-In Date:" + alternativeCheckIn +
                            "\nCheck-Out Date:" + alternativeCheckOut);

                    printRooms(alternativeRooms);
                    reserveRoom(scanner, alternativeCheckIn, alternativeCheckOut, alternativeRooms);
                }
            } else {
                printRooms(availableRooms);
                reserveRoom(scanner, checkIn, checkOut, availableRooms);
            }
        }
    }

    private static Date getInputDate(final Scanner scanner) {
        try {
            return new SimpleDateFormat(DEFAULT_DATES).parse(scanner.nextLine());
        } catch (ParseException ex) {
            System.out.println("Error: Invalid date.");
            findAndReserveRoom();
        }

        return null;
    }

    private static void reserveRoom(final Scanner scanner, final Date checkInDate,
                                    final Date checkOutDate, final Collection<IRoom> rooms) {
        System.out.println("Would you like to book this reservation? Y/N");
        final String bookRoom = scanner.nextLine().toUpperCase();

        if ("Y".equals(bookRoom)) {
            System.out.println("Do you have an account with us? Y/N");
            final String haveAccount = scanner.nextLine().toUpperCase();

            if ("Y".equals(haveAccount)) {
                System.out.println("Enter Email format: name@domain.com");
                final String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("Can not find.\nPlease create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        final IRoom room = hotelResource.getRoom(roomNumber);

                        final Reservation reservation = hotelResource
                                .bookRoom(customerEmail, room, checkInDate, checkOutDate);
                        System.out.println("Room Reserved");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room number not available.\nPLease try again");
                    }
                }

                printMainMenu();
            } else {
                System.out.println("Please, create an account.");
                printMainMenu();
            }
        } else if ("n".equals(bookRoom)){
            printMainMenu();
        } else {
            reserveRoom(scanner, checkInDate, checkOutDate, rooms);
        }
    }

    private static void printRooms(final Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void seeMyReservation() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Email format: name@domain.com");
        final String customerEmail = scanner.nextLine();

        printReservations(hotelResource.getCustomersReservations(customerEmail));
    }

    private static void printReservations(final Collection<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(reservation -> System.out.println("\n" + reservation));
        }
    }

    private static void createAccount() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Email format: name@domain.com");
        final String email = scanner.nextLine();

        System.out.println("First Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Last Name:");
        final String lastName = scanner.nextLine();

        try {
            hotelResource.createCustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");

            printMainMenu();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }

    public static void printMainMenu()
    {
        System.out.print("\nWelcome to the Hotel Reservation Application\n" +
                "--------------------------------------------\n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an Account\n" +
                "4. Admin\n" +
                "5. Exit\n" +
                "--------------------------------------------\n" +
                "Please select a number for the menu option:\n");
    }
}
