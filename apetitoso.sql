CREATE DATABASE IF NOT EXISTS apetitoso;
USE apetitoso;

CREATE TABLE IF NOT EXISTS address(
  id_address int(11) not null AUTO_INCREMENT,
  street_address varchar(255) not null,
  number_address int(11) not null,
  neighborhood_address varchar(255) not null,
  cep_address varchar(8) not null,
  CONSTRAINT PRK_ID_ADDRESS PRIMARY KEY (id_address) 
);

CREATE TABLE IF NOT EXISTS person( 
  id_person int(11) not null AUTO_INCREMENT,
  id_address int(11) not null,
  name_person varchar(255) not null,
  CONSTRAINT PRK_ID_PERSON PRIMARY KEY (id_person),
  CONSTRAINT FRK_ID_PERSON_ADDRESS FOREIGN KEY (id_address) REFERENCES address(id_address)
);

CREATE TABLE IF NOT EXISTS phone( 
  id_phone int(11) not null AUTO_INCREMENT,
  number_phone varchar(12) not null,
  status_phone boolean not null,
  id_person int(11) not null,
  CONSTRAINT PRK_ID_PHONE PRIMARY KEY (id_phone),
  CONSTRAINT FRK_ID_PHONE_PERSON_PERSON FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS costumer(  
  status_costumer boolean not null,
  id_person int(11) not null,
  CONSTRAINT FRK_ID_COSTUMER FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS supplier(
  cnpj_supplier varchar(14) not null,
  status_supplier boolean not null,
  id_person int(11) not null,
  CONSTRAINT UNK_CNPJ_SUPPLIER UNIQUE KEY (cnpj_supplier),
  CONSTRAINT FRK_ID_SUPPLIER FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS employee(
  id_employee int(11) not null AUTO_INCREMENT,
  rg_employee varchar(10) not null,
  cpf_employee varchar(11) not null,
  role_employee varchar(255) not null,
  status_employee boolean not null,
  id_person int(11) not null,
  CONSTRAINT UNK_CPF_EMPLOYEE UNIQUE KEY (cpf_employee),
  CONSTRAINT UNK_RG_EMPLOYEE UNIQUE KEY (rg_employee),
  CONSTRAINT PRK_ID_EMPLOYEE PRIMARY KEY (id_employee),
  CONSTRAINT FRK_ID_EMPLOYEE FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS user(
  id_user int(11) not null AUTO_INCREMENT,
  login_user varchar(255) not null,
  password_user varchar(255) not null,
  status_user boolean not null,
  level_user int(11) not null default 0,
  id_employee int(11) not null,
  CONSTRAINT PRK_ID_USER PRIMARY KEY (id_user),
  CONSTRAINT UNK_LOGIN_USER UNIQUE KEY (login_user),
  CONSTRAINT FRK_ID_USER FOREIGN KEY (id_employee) REFERENCES employee(id_employee)
);

CREATE TABLE IF NOT EXISTS product_type(
  id_product_type int(11) not null AUTO_INCREMENT,
  name_product_type varchar(255) not null,
  status_product_type boolean not null,
  CONSTRAINT PRK_ID_PRODUCT_TYPE PRIMARY KEY (id_product_type)
);

CREATE TABLE IF NOT EXISTS ingredient(
  id_ingredient int(11) not null AUTO_INCREMENT,
  name_ingredient varchar(255) not null,
  status_ingredient boolean not null,
  price_ingredient float not null,
  CONSTRAINT PRK_ID_INGREDIENT PRIMARY KEY (id_ingredient)
);

CREATE TABLE IF NOT EXISTS product(
  id_product int(11) not null AUTO_INCREMENT,
  name_product varchar(255) not null,
  final_price_product decimal(15,2) not null,
  weight_product float not null,
  status_product boolean not null,
  id_product_type int(11) not null,
  CONSTRAINT PRK_ID_PRODUCT PRIMARY KEY (id_product),
  CONSTRAINT FRK_PRODUCT_TYPE FOREIGN KEY (id_product_type) REFERENCES product_type(id_product_type)
);

CREATE TABLE IF NOT EXISTS product_ingredient(
  id_product_ingredient int(11) not null AUTO_INCREMENT,
  id_product int(11) not null,
  id_ingredient int(11) not null,
  CONSTRAINT PRK_ID_PRODUCT_INGREDIENT PRIMARY KEY (id_product_ingredient),
  CONSTRAINT FRK_INGREDIENT FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient),
  CONSTRAINT FRK_PRODUCT FOREIGN KEY (id_product) REFERENCES product(id_product)
);

CREATE TABLE IF NOT EXISTS sale(
  id_sale int(11) not null AUTO_INCREMENT,
  id_user int(11) not null,
  id_costumer int(11) not null,
  sale_time datetime,
  sale_date date,
  sale_time_estimate int not null,
  sale_total decimal(10,2),
  CONSTRAINT PRK_ID_sale PRIMARY KEY (id_sale), 
  CONSTRAINT FRK_ID_COSTUMER_SALE FOREIGN KEY (id_costumer) REFERENCES costumer(id_person),
  CONSTRAINT FRK_ID_EMPLOYEE_SALE FOREIGN KEY (id_user) REFERENCES user(id_employee)
);

CREATE TABLE IF NOT EXISTS items_sale(
  id_items_sale int(11) not null AUTO_INCREMENT,
  id_sale int(11) not null,
  id_product int(11) not null,
  CONSTRAINT PRK_ID_ITEMS_SALE PRIMARY KEY (id_items_sale),
  CONSTRAINT FRK_ID_ITEMS_SALE_SALE FOREIGN KEY (id_sale) REFERENCES sale(id_sale),
  CONSTRAINT FRK_ID_ITEMS_SALE_PRODUCT FOREIGN KEY (id_product) REFERENCES product(id_product)
);