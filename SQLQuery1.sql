--Create table--
CREATE TABLE ACCOUNT
(Account_name char(10) not null PRIMARY KEY,
 Pass varchar(10) not null , 
 Position varchar(10) not null 
 );
 CREATE TABLE STAFF
 (
     Staff_ID char(10) not null PRIMARY KEY , 
     Name_of_staff varchar(20) not null ,
     Position varchar(10) not null ,
     Gender char(5) not null ,
     PhoneNumber char(10) not null ,
     Email varchar(15) not null ,
     ProvidedImage image not null ,
     Salary_of_staff money not null ,
     Status char(10) not null,
 );
ALTER TABLE STAFF
ADD FOREIGN KEY (Staff_ID) REFERENCES ACCOUNT(Account_name);
CREATE TABLE SUPPLIER
(
    Supplier_ID char(10) not null PRIMARY KEY , 
    Name_of_supplier varchar(20) not null ,
    Address varchar(30) not null ,
    Email varchar(15) not null , 
    PhoneNumber char(10) not null,
    Logo IMAGE not null ,
    Status char(10) not null ,
);
CREATE TABLE WAREHOUSEINVOICE
(
    WarehouseInvoice_ID char(10) not null PRIMARY KEY , 
    Staff_ID char(10) not null FOREIGN KEY REFERENCES STAFF(Staff_ID),
    Supplier_ID char(10) not null FOREIGN KEY REFERENCES SUPPLIER(Supplier_ID),
    DateTime_of_WarehouseInvoice DATETIME not null ,
    Price_of_WarehouseInvoice money not null
);
CREATE TABLE DETAILWAREHOUSEINVOICE
(
    WarehouseInvoice_ID char(10) not null ,
    Product_ID char(10) not null ,
    Quantity tinyint not null ,
    Unit char(10) not null ,
    IncomePrice SMALLMONEY not null ,
    OutcomePrice SMALLMONEY not null , 
    Profit SMALLMONEY not null,
    CONSTRAINT PK_DETAILWAREHOUSEINVOICE PRIMARY KEY (WarehouseInvoice_ID,Product_ID)
);
CREATE TABLE PRODUCT
(
  Product_ID char(10) not null PRIMARY KEY ,
  Category_ID char(10) not null FOREIGN KEY REFERENCES CATEGORY(Category_ID),
  Name_of_Product varchar(20) not null ,
  Branding varchar(10) not null ,
  Price SMALLMONEY not null ,
  Unit char(10) not null ,
  Description varchar(40) not null ,
  Quantity tinyint not null ,
  Image_Product IMAGE not null
);
ALTER TABLE PRODUCT
ADD Size char(10) not null
CREATE TABLE CATEGORY
(
  Category_ID char(10) not null PRIMARY KEY ,
  Name_of_Category varchar(20) not null
);
CREATE TABLE INVOICE
(
  Invoice_ID char(10) not null PRIMARY KEY , 
  Staff_ID char(10) not null FOREIGN KEY REFERENCES STAFF(Staff_ID),
  DateTime_Invoice Datetime not null ,
  Price_of_Invoice money not null
);
CREATE TABLE DETAILINVOICE
(
    Invoice_ID char(10) not null,
    Product_ID char(10) not null,
    Size char(10) not null,
    Quantity tinyint not null ,
    Price_of_product money not null , 
    CONSTRAINT PK_DETAILINVOICE PRIMARY KEY (Invoice_ID , Product_ID)
);
CREATE TABLE EXPENSESTAFFSALARY
(
    StaffSalaryInvoice_ID char(10) not null PRIMARY KEY , 
    Price_of_invoice money not null ,
    Staff_ID char(10) not null FOREIGN KEY REFERENCES STAFF(Staff_ID),
    DateTime_of_Excel Datetime not null ,
    Status char(10) not null,
    LinkExcel varchar(50) not null ,
    PaymentMethod varchar(20) not null ,
    DateTime_of_Invoice DATETIME not null,
);
CREATE TABLE OTHEREXPENSE
(
  Invoice_ID char(10) not null PRIMARY KEY , 
  Kind_of_Invoice char(15) not null ,
  Price_of_Invoice money not null ,
  Staff_ID char(10) not null FOREIGN KEY REFERENCES STAFF(Staff_ID),
  PaymentMethod varchar(20) not null ,
  DateTime_of_Invoice DATETIME not null,
  Image_of_Invoice IMAGE not null
);
CREATE TABLE EXPENSEWAREHOUSEINVOICE
(
  Invoice_ID char(10) not null PRIMARY KEY , 
  Price_of_Invoice money not null ,
  Staff_ID char(10) not null FOREIGN KEY REFERENCES STAFF(Staff_ID),
  DateTime_of_Invoice DATETIME not null,
  Quantity_of_invoices TINYINT not null ,

);
CREATE TABLE DETAILEXPENSEWAREHOUSEINVOICE
(
   Invoice_ID char(10) not null ,
   WarehouseInvoice_ID char(10) not null ,
   DateTime_of_Invoice DATETIME not null,
   CONSTRAINT PK_DETAILEXPENSEWAREHOUSEINVOICE PRIMARY KEY (Invoice_ID ,WarehouseInvoice_ID)
);
--Foreign key--
ALTER TABLE DETAILEXPENSEWAREHOUSEINVOICE
ADD FOREIGN KEY (WarehouseInvoice_ID) REFERENCES WAREHOUSEINVOICE(WarehouseInvoice_ID);
ALTER TABLE DETAILINVOICE
ADD FOREIGN KEY (Product_ID) REFERENCES PRODUCT(Product_ID);
ALTER TABLE DETAILWAREHOUSEINVOICE
ADD FOREIGN KEY (Product_ID) REFERENCES PRODUCT(Product_ID);