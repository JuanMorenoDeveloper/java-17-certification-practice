DROP TABLE Item IF EXISTS;
DROP TABLE Invoice IF EXISTS;
DROP TABLE Product IF EXISTS;
DROP TABLE Customer IF EXISTS;
CREATE TABLE Customer(ID INTEGER PRIMARY KEY,FirstName VARCHAR(20),LastName VARCHAR(30),Street VARCHAR(50),City VARCHAR(25));
CREATE TABLE Product(ID INTEGER PRIMARY KEY,Name VARCHAR(30),Price DECIMAL);
CREATE TABLE Invoice(ID INTEGER PRIMARY KEY,CustomerID INTEGER,Total DECIMAL, FOREIGN KEY (CustomerId) REFERENCES Customer(ID) ON DELETE CASCADE);
CREATE TABLE Item(InvoiceID INTEGER,Item INTEGER,ProductID INTEGER,Quantity INTEGER,Cost DECIMAL,PRIMARY KEY(InvoiceID,Item), FOREIGN KEY (InvoiceId) REFERENCES Invoice (ID) ON DELETE CASCADE, FOREIGN KEY (ProductId) REFERENCES Product(ID) ON DELETE CASCADE);