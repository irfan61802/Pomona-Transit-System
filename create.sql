CREATE DATABASE Transit; 
USE Transit; 
CREATE TABLE Trip (
    TripNumber INT,
    startLocationName VARCHAR(255),
    DestinationName VARCHAR(255),
    PRIMARY KEY (TripNumber)
); 
CREATE TABLE TripOffering (
    TripNumber INT,
    Date Date,
    ScheduledStartTime Time,
    ScheduledArrivalTime Time,
    DriverName VARCHAR(255),
    BusID INT,
    PRIMARY KEY (TripNumber,Date,ScheduledStartTime),
    FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber) 
    ON DELETE CASCADE ON UPDATE CASCADE
); 
CREATE TABLE Bus (
    BusID INT,
    Model VARCHAR(255),
    Year INT,
    PRIMARY KEY (BusID)
); 
CREATE TABLE Driver (
    DriverName VARCHAR(255),
    DriverTelephoneNumber VARCHAR(255),
    PRIMARY KEY (DriverName)
);
CREATE TABLE Stop (
    StopNumber INT,
    StopAddress VARCHAR(255),
    PRIMARY KEY (StopNumber)
); 
CREATE TABLE ActualTripStopInfo (
    TripNumber INT,
    Date Date,
	ScheduledStartTime Time,
    StopNumber INT,
    ScheduledArrivalTime Time,
    ActualStartTime Time,
    ActualArrivalTime time,
    NumberOfPassengerIn INT,
    NumberOfPassengerOut INT,
    FOREIGN KEY (TripNumber,Date,ScheduledStartTime) REFERENCES TripOffering (TripNumber,Date,ScheduledStartTime)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (StopNumber) REFERENCES Stop (StopNumber)
    ON DELETE CASCADE ON UPDATE CASCADE
    
); 
CREATE TABLE TripStopInfo (
    TripNumber INT,
    StopNumber INT,
    SequenceNumber INT,
    DrivingTime time,
    FOREIGN KEY (TripNumber) REFERENCES Trip (TripNumber)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (StopNumber) REFERENCES Stop (StopNumber)
    ON DELETE CASCADE ON UPDATE CASCADE
); 
