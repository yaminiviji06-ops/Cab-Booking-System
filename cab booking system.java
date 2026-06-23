import java.util.*;

class Cab {
    String cabId;
    String driverName;
    String cabType;
    String status; // Available, Booked

    public Cab(String cabId, String driverName, String cabType) {
        this.cabId = cabId;
        this.driverName = driverName;
        this.cabType = cabType;
        this.status = "Available";
    }
}

class RideData {
    String bookingId;
    String userName;
    String pickup;
    String drop;
    String cabId;
    String status; 
    double fare;

    public RideData(String bookingId, String userName, String pickup, String drop, String cabId, double fare) {
        this.bookingId = bookingId;
        this.userName = userName;
        this.pickup = pickup;
        this.drop = drop;
        this.cabId = cabId;
        this.fare = fare;
        this.status = "Booked";
    }
}

class DataHandler {

    ArrayList<Cab> cabList = new ArrayList<>();
    ArrayList<RideData> bookings = new ArrayList<>();

    public void addCab(Cab cab) {
        cabList.add(cab);
    }

    public List<Cab> getAvailableCabs() {
        List<Cab> available = new ArrayList<>();
        for (Cab c : cabList) {
            if (c.status.equals("Available")) {
                available.add(c);
            }
        }
        return available;
    }

    public void addBooking(RideData ride) {
        bookings.add(ride);
    }

    public Cab getCabById(String cabId) {
        for (Cab c : cabList) {
            if (c.cabId.equals(cabId)) {
                return c;
            }
        }
        return null;
    }

    public RideData getBookingById(String bookingId) {
        for (RideData r : bookings) {
            if (r.bookingId.equals(bookingId)) {
                return r;
            }
        }
        return null;
    }

    public List<RideData> getAllBookings() {
        return bookings;
    }
}

class CabBookingManager {

    DataHandler dataHandler;

    public CabBookingManager(DataHandler handler) {
        this.dataHandler = handler;
    }

    // FARE CALCULATION
    public double calculateFare(String cabType) {
        switch (cabType) {
            case "Mini": return 15 * 10;   // Rs.150
            case "Sedan": return 20 * 10;  // Rs.200
            case "SUV": return 25 * 10;    // Rs.250
            default: return 0;
        }
    }

    // BOOK CAB
    public String bookCab(String userName, String pickup, String drop) {

        List<Cab> available = dataHandler.getAvailableCabs();

        if (available.isEmpty()) {
            return "❌ No cabs available right now!";
        }

        Cab cab = available.get(0);
        cab.status = "Booked";

        double fare = calculateFare(cab.cabType);

        String bookingId = "B" + new Random().nextInt(9999);
        RideData ride = new RideData(bookingId, userName, pickup, drop, cab.cabId, fare);
        dataHandler.addBooking(ride);

        return "✔ Booking Successful!\nBooking ID: " + bookingId +
               "\nCab: " + cab.cabId + " (" + cab.driverName + ")" +
               "\nFare: Rs." + fare;
    }

    // COMPLETE RIDE
    public String completeRide(String bookingId) {
        RideData ride = dataHandler.getBookingById(bookingId);

        if (ride == null) return "❌ Booking not found!";
        if (ride.status.equals("Completed")) return "❌ Ride already completed!";
        if (ride.status.equals("Cancelled")) return "❌ Ride was cancelled earlier!";

        Cab cab = dataHandler.getCabById(ride.cabId);
        cab.status = "Available";

        ride.status = "Completed";

        return "✔ Ride Completed Successfully!\nTotal Fare: Rs." + ride.fare;
    }

    // CANCEL RIDE
    public String cancelBooking(String bookingId) {
        RideData ride = dataHandler.getBookingById(bookingId);

        if (ride == null) return "❌ Booking not found!";
        if (ride.status.equals("Completed")) return "❌ Ride already completed!";
        if (ride.status.equals("Cancelled")) return "❌ Ride already cancelled!";

        Cab cab = dataHandler.getCabById(ride.cabId);
        if (cab != null) cab.status = "Available";

        ride.status = "Cancelled";

        return "✔ Ride Cancelled Successfully!";
    }
}

public class CabBookingSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DataHandler handler = new DataHandler();
        CabBookingManager manager = new CabBookingManager(handler);

        // Sample Cabs
        handler.addCab(new Cab("C101", "Rohit", "Mini"));
        handler.addCab(new Cab("C102", "Arun", "Sedan"));
        handler.addCab(new Cab("C103", "Karan", "SUV"));

        while (true) {
            System.out.println("\n========= CAB BOOKING SYSTEM =========");
            System.out.println("1. View Available Cabs");
            System.out.println("2. Book a Cab");
            System.out.println("3. View All Bookings");
            System.out.println("4. Complete Ride");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {

            case 1: // AVAILABLE CABS (added)
                List<Cab> cabs = handler.getAvailableCabs();
                if (cabs.isEmpty()) {
                    System.out.println("❌ No cabs available!");
                } else {
                    System.out.println("\nAvailable Cabs:");
                    for (Cab c : cabs) {
                        System.out.println(c.cabId + " - " + c.driverName + " (" + c.cabType + ")");
                    }
                }
                break;

            case 2:
                System.out.print("Enter Your Name: ");
                String name = sc.next();

                System.out.print("Enter Pickup Location: ");
                String pickup = sc.next();

                System.out.print("Enter Drop Location: ");
                String drop = sc.next();

                System.out.println(manager.bookCab(name, pickup, drop));
                break;

            case 3:
                List<RideData> rides = handler.getAllBookings();
                System.out.println("\nAll Bookings:");
                for (RideData r : rides) {
                    System.out.println(r.bookingId + " | " + r.userName + " | " +
                            r.pickup + " → " + r.drop + " | Cab: " + r.cabId +
                            " | Fare: Rs." + r.fare + " | Status: " + r.status);
                }
                break;

            case 4:
                System.out.print("Enter Booking ID to complete: ");
                String compId = sc.next();
                System.out.println(manager.completeRide(compId));
                break;

            case 5:
                System.out.print("Enter Booking ID to cancel: ");
                String cancelId = sc.next();
                System.out.println(manager.cancelBooking(cancelId));
                break;

            case 6:
                System.out.println("Exiting... Thank you!");
                System.exit(0);

            default:
                System.out.println("Invalid choice!");
            }
        }
    }
}
