package jdbc;
import java.sql.*;
import java.util.Scanner;

public class sql {

    public static Connection con;

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Transit",
                    "root", "password123");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{


        System.out.println("Scheduling System Menu:");
        System.out.println("1-Display Schedule\n2-Edit Schedule\n3-Display Stops\n4-Display Weekly Schedule\n5-Add Driver\n6-Add Bus\n7-Delete a Bus\n8-Record Actual Trip Stop Info\n9-Exit");


        Scanner scanner = new Scanner(System.in);
        System.out.println("Pick a choice: ");
        String choice = scanner.nextLine();

        while(!choice.equals("9")){

            switch(choice){
                case "1":
                    System.out.println("Enter Start Location Name: ");
                    String startLocationName = scanner.nextLine();
                    System.out.println("Enter Destination Name: ");
                    String destinationName = scanner.nextLine();
                    System.out.println("Enter Date: ");
                    String date = scanner.nextLine();
                    displaySchedule(startLocationName,destinationName,date);
                    break;
                case "2":
                    System.out.println("Sub-Menu\na-Delete Trip Offering\nb-Add Trip Offering\nc-Change Driver\nd-Change Bus");
                    System.out.println("Pick a choice: ");
                    String choice2 = scanner.nextLine();
                    switch(choice2){
                        case "a":
                            System.out.println("Enter Trip Number: ");
                            int tripNumber = Integer.parseInt(scanner.nextLine());
                            System.out.println("Enter Date: ");
                            date = scanner.nextLine();
                            System.out.println("Enter Scheduled Start Time: ");
                            String scheduledStartTime = scanner.nextLine();
                            deleteTripOffering(tripNumber,date,scheduledStartTime);
                            break;
                        case "b":
                            boolean done=false;
                            while(!done) {
                                System.out.println("Enter Trip Number: ");
                                tripNumber = Integer.parseInt(scanner.nextLine());
                                System.out.println("Enter Date: ");
                                date = scanner.nextLine();
                                System.out.println("Enter Scheduled Start Time: ");
                                scheduledStartTime = scanner.nextLine();
                                System.out.println("Enter Scheduled Arrival Time: ");
                                String scheduledArrivalTime = scanner.nextLine();
                                System.out.println("Enter Driver Name: ");
                                String driver = scanner.nextLine();
                                System.out.println("Enter BusID: ");
                                int bus = Integer.parseInt(scanner.nextLine());
                                addTripOffering(tripNumber, date, scheduledStartTime, scheduledArrivalTime, driver, bus);

                                System.out.println("Add More Trip Offerings? (Y/N): ");
                                String c = scanner.nextLine();
                                if(c.equals("N") || c.equals("n"))
                                    done=true;
                            }
                            break;
                        case "c":
                            System.out.println("Enter Trip Number: ");
                            tripNumber = Integer.parseInt(scanner.nextLine());
                            System.out.println("Enter Date: ");
                            date = scanner.nextLine();
                            System.out.println("Enter Scheduled Start Time: ");
                            scheduledStartTime = scanner.nextLine();
                            System.out.println("Enter Driver Name: ");
                            String driver = scanner.nextLine();
                            editDriver(tripNumber, date, scheduledStartTime, driver);
                            break;
                        case "d":
                            System.out.println("Enter Trip Number: ");
                            tripNumber = Integer.parseInt(scanner.nextLine());
                            System.out.println("Enter Date: ");
                            date = scanner.nextLine();
                            System.out.println("Enter Scheduled Start Time: ");
                            scheduledStartTime = scanner.nextLine();
                            System.out.println("Enter BusID: ");
                            int bus = Integer.parseInt(scanner.nextLine());
                            editBus(tripNumber,date,scheduledStartTime,bus);
                            break;
                    }
                    break;
                case "3":
                    System.out.println("Enter Trip Number: ");
                    int tripNumber = Integer.parseInt(scanner.nextLine());
                    displayStops(tripNumber);
                    break;
                case "4":
                    System.out.println("Enter Driver Name: ");
                    String driver = scanner.nextLine();
                    System.out.println("Enter Date: ");
                    date = scanner.nextLine();
                    displayWeeklySchedule(driver,date);
                    break;
                case "5":
                    System.out.println("Enter Driver Name: ");
                    driver = scanner.nextLine();
                    System.out.println("Enter Telephone Number: ");
                    String number = scanner.nextLine();
                    addDriver(driver,number);
                    break;
                case "6":
                    System.out.println("Enter BusID: ");
                    int bus = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter Bus Model: ");
                    String model = scanner.nextLine();
                    System.out.println("Enter Model Year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    addBus(bus,model,year);
                    break;
                case "7":
                    System.out.println("Enter BusID: ");
                    bus = Integer.parseInt(scanner.nextLine());
                    deleteBus(bus);
                    break;
                case "8":
                    System.out.println("Enter Trip Number: ");
                    tripNumber = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter Date: ");
                    date = scanner.nextLine();
                    System.out.println("Enter Scheduled Start Time: ");
                    String scheduledStartTime = scanner.nextLine();
                    System.out.println("Enter Stop Number: ");
                    int stopNumber = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter Scheduled Arrival Time: ");
                    String scheduledArrivalTime = scanner.nextLine();
                    System.out.println("Enter Actual Start Time: ");
                    String actualStarTime = scanner.nextLine();
                    System.out.println("Enter Actual Arrival Time: ");
                    String actualArrivalTime = scanner.nextLine();
                    System.out.println("Enter Passengers In: ");
                    int pIn = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter Passengers Out: ");
                    int pOut = Integer.parseInt(scanner.nextLine());

                    recordInfo(tripNumber, date, scheduledStartTime, stopNumber,scheduledArrivalTime,actualStarTime,actualArrivalTime,pIn,pOut);
                    break;
                default:
                    break;
            }
            System.out.println("1-Display Schedule\n2-Edit Schedule\n3-Display Stops\n4-Display Weekly Schedule\n5-Add Driver\n6-Add Bus\n7-Delete a Bus\n8-Record Actual Trip Stop Info\n9-Exit");
            System.out.println("Pick a choice: ");
            choice = scanner.nextLine();

        }


        con.close();

    }


    public static void displaySchedule(String startLocationName, String destinationName, String date) throws SQLException {

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT T.startLocationName,T.DestinationName, O.ScheduledStartTime, O.ScheduledArrivalTime, O.DriverName, O.BusID " +
                                            "FROM Trip T,TripOffering O WHERE T.TripNumber=O.TripNumber AND " +
                                            "T.StartLocationName=\""+startLocationName+"\" AND T.DestinationName=\""+destinationName+ "\" "+ "AND O.Date=\""+date+"\"");

        System.out.println("Schedule for "+startLocationName+" to "+destinationName+" on "+ date);
        System.out.println("ScheduledStartTime ScheduledArrivalTime DriverName BusID");
        while(rs.next()) {
            System.out.println(rs.getString("ScheduledStartTime")+"           "+
                            rs.getString("ScheduledArrivalTime")+"             "+ rs.getString("DriverName")+"        "+rs.getString("BusID"));
        }

        st.close();

    }

    public static void deleteTripOffering(int tripN, String date, String tStartTime) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("DELETE FROM TripOffering WHERE TripNumber="+tripN+" AND Date=\""+date+"\" AND ScheduledStartTime=\""+tStartTime+"\"");

        st.close();

    }

    public static void addTripOffering(int tripN, String date, String startTime, String arrivalTime, String driverName, int BusID) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("INSERT into TripOffering VALUES("+tripN+",\""+date+"\""+",\""+startTime+"\""+",\""+arrivalTime+"\""+",\""+driverName+"\","+BusID+")");

        st.close();

    }


    public static void editDriver(int tripN, String date, String tStartTime, String driver) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("UPDATE TripOffering SET DriverName=\""+driver+"\" WHERE TripNumber=\""+tripN+"\" AND Date=\""+date+"\" AND ScheduledStartTime=\""+tStartTime+"\"");

        st.close();

    }


    public static void editBus(int tripN, String date, String tStartTime, int BusID) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("UPDATE TripOffering SET BusID="+BusID+" WHERE TripNumber=\""+tripN+"\" AND Date=\""+date+"\" AND ScheduledStartTime=\""+tStartTime+"\"");

        st.close();

    }

    public static void displayStops(int tripN) throws SQLException {

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM TripStopInfo WHERE TripNumber=" + tripN);
        System.out.println("TripNumber\tStopNumber\tSequenceNumber\tDrivingTime");
        while(rs.next()) {

            System.out.println(rs.getString("TripNumber")+"\t\t\t"+rs.getString("StopNumber")+"\t\t\t"+rs.getString("SequenceNumber")+"\t\t\t\t"+rs.getString("DrivingTime"));
        }

        st.close();

    }


    public static void displayWeeklySchedule(String DriverName, String date) throws SQLException {

        Statement st = con.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM TripOffering O,Trip T WHERE DriverName=\""+DriverName+"\" AND WEEK(date)=WEEK(\""+date+"\") AND T.TripNumber=O.TripNumber");

        System.out.println("Weekly Schedule for "+DriverName+" week of " +date);

        System.out.println("StartLocationName     DestinationName   Date         ScheduledStartTime ScheduledArrivalTime DriverName BusID");
        while(rs.next()) {
            System.out.println(rs.getString("StartLocationName")+"     "+rs.getString("DestinationName")+"  "+rs.getString("Date")
                            +"     "+rs.getString("ScheduledStartTime")+"           "+ rs.getString("ScheduledArrivalTime")
                            +"             "+ rs.getString("DriverName")+"        "+rs.getString("BusID"));
        }

        st.close();

    }

    public static void addDriver(String DriverName, String DriverTeleponeNumber) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("INSERT into Driver VALUES(\""+DriverName+"\",\""+DriverTeleponeNumber+"\")");

        st.close();

    }

    public static void addBus(int BusID, String Model, int Year) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("INSERT into Bus VALUES("+BusID+",\""+Model+"\","+Year+")");

        st.close();

    }

    public static void deleteBus(int BusID) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("DELETE FROM Bus WHERE BusID="+BusID);

        st.close();

    }

    public static void recordInfo(int tNumber,String date, String sStartTime, int sNumber, String sArrivalTime, String aStartTime, String aArrivalTime, int passengersIn, int passengersOut ) throws SQLException {

        Statement st = con.createStatement();
        st.executeUpdate("INSERT into ActualTripStopInfo VALUES("+tNumber+",\""+date+"\",\""+sStartTime+"\","+sNumber+",\""+sArrivalTime
                                                                    +"\",\""+aStartTime+"\",\""+aArrivalTime+"\","+passengersIn+","+passengersOut+")");

        st.close();

    }
}
