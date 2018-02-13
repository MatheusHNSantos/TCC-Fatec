CREATE DATABASE IF NOT EXISTS apetitoso;
USE apetitoso;

CREATE TABLE IF NOT EXISTS address(
  id_address int not null auto_increment,
  street varchar(255) not null,
  number int not null,
  neighborhood varchar(255) not null,
  cep varchar(8) not null,
  CONSTRAINT PRK_ID_ADDRESS PRIMARY KEY (id_address) 
);


CREATE TABLE IF NOT EXISTS person( 
  id_person int not null auto_increment,
  id_address int not null,
  name_person varchar(255) not null,
  status boolean not null,
  CONSTRAINT PRK_ID_PERSON PRIMARY KEY (id_person),
  CONSTRAINT FRK_ID_PERSON_ADDRESS FOREIGN KEY (id_address) REFERENCES address(id_address)
);



CREATE TABLE IF NOT EXISTS phone( 
  id_phone int not null auto_increment,
  phone varchar(12) not null,
  id_person int not null,
  CONSTRAINT PRK_ID_PHONE PRIMARY KEY (id_phone), 
  CONSTRAINT FRK_ID_PHONE_PERSON_PERSON FOREIGN KEY (id_person) REFERENCES person(id_person)
);


CREATE TABLE IF NOT EXISTS customer(  
  id_person int not null,
  CONSTRAINT FRK_ID_CUSTOMER FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS supplier(
  cnpj varchar(14) not null,
  id_person int not null,
  CONSTRAINT UNK_CNPJ_SUPPLIER UNIQUE KEY (cnpj),
  CONSTRAINT FRK_ID_SUPPLIER FOREIGN KEY (id_person) REFERENCES person(id_person)
);

CREATE TABLE IF NOT EXISTS employee(
  id_employee int not null auto_increment,
  role varchar(255) not null,
  id_person int not null,
  CONSTRAINT PRK_ID_EMPLOYEE PRIMARY KEY (id_employee),
  CONSTRAINT FRK_ID_EMPLOYEE FOREIGN KEY (id_person) REFERENCES person(id_person)
);


/* 4- operador 5- administrador*/
CREATE TABLE IF NOT EXISTS user( 
  login varchar(255) not null,
  password varchar(255) not null,
  id_employee int not null,
  status boolean,
  level int,
  CONSTRAINT UNK_LOGIN_USER UNIQUE KEY (login),
  CONSTRAINT FRK_ID_USER FOREIGN KEY (id_employee) REFERENCES employee(id_employee)
);

CREATE TABLE IF NOT EXISTS product_type(
  id_product_type int not null auto_increment,
  name_product_type varchar(255) not null,
  CONSTRAINT PRK_ID_PRODUCT_TYPE PRIMARY KEY (id_product_type)
);

CREATE TABLE IF NOT EXISTS ingredient(
  id_ingredient int not null AUTO_INCREMENT,
  name_ingredient varchar(255) not null,
  status_ingredient boolean not null,
  price float not null,
  CONSTRAINT PRK_ID_INGREDIENT PRIMARY KEY (id_ingredient)
);

CREATE TABLE IF NOT EXISTS product(
  id_product int not null auto_increment,
  name_product varchar(255) not null,
  final_price_product decimal(15,2) not null,
  weight_product float not null,
  status_product boolean not null,
  id_product_type int not null,
  CONSTRAINT PRK_ID_PRODUCT PRIMARY KEY (id_product),
  CONSTRAINT FRK_PRODUCT_TYPE FOREIGN KEY (id_product_type) REFERENCES product_type(id_product_type)
);

CREATE TABLE IF NOT EXISTS product_ingredient(
  id_product_ingredient int not null AUTO_INCREMENT,
  id_product int not null,
  id_ingredient int not null,
  CONSTRAINT PRK_ID_PRODUCT_INGREDIENT PRIMARY KEY (id_product_ingredient),
  CONSTRAINT FRK_INGREDIENT FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient),
  CONSTRAINT FRK_PRODUCT FOREIGN KEY (id_product) REFERENCES product(id_product)
);

CREATE TABLE IF NOT EXISTS sale(
  id_sale int not null AUTO_INCREMENT,
  id_user int not null,
  id_customer int not null,
  sale_time varchar(255),
  sale_date varchar(255),
  sale_time_estimate int not null,
  sale_total decimal(10,2),
  CONSTRAINT PRK_ID_sale PRIMARY KEY (id_sale), 
  CONSTRAINT FRK_ID_CUSTOMER_SALE FOREIGN KEY (id_customer) REFERENCES customer(id_person),
  CONSTRAINT FRK_ID_EMPLOYEE_SALE FOREIGN KEY (id_user) REFERENCES user(id_employee)
);

CREATE TABLE IF NOT EXISTS items_sale(
  id_items_sale int not null AUTO_INCREMENT,
  id_sale int not null,
  id_product int not null,
  CONSTRAINT PRK_ID_ITEMS_SALE PRIMARY KEY (id_items_sale),
  CONSTRAINT FRK_ID_ITEMS_SALE_SALE FOREIGN KEY (id_sale) REFERENCES sale(id_sale),
  CONSTRAINT FRK_ID_ITEMS_SALE_PRODUCT FOREIGN KEY (id_product) REFERENCES product(id_product)
);

insert into product_type values (1, "lanches");

insert into product_type values (2, "porcoes");

insert into product_type values (3, "mamitas"); 

insert into product values(1,"Lanche de carne", 10, 1000, 1,1);
insert into product values(2,"Lanche de frango",8, 500, 1,1);
insert into product values(3,"Lanche de peito de peru",8, 500, 1,1);
insert into product values(4,"Lanche de bife acebolado",8, 500, 1,1);

insert into product values(5,"Porcao de carne", 10, 1000, 1,2);
insert into product values(6,"Porcao frango",8, 500, 1,2);
insert into product values(7,"Porcao de batata", 10, 1000, 1,2);
insert into product values(8,"Porcao de contafilé",8, 500, 1,2);

insert into product values(9,"Marmita P",10, 500, 1,3);
insert into product values(10,"Marmita M", 15, 1000, 1,3);
insert into product values(11,"Marmita G",20, 500, 1,3);

insert into address values (null, 'Rua 1', 1, 'Bairro 1', '13848000');

insert into address values (null, 'Rua 2', 2, 'Bairro 2', '13848000');

insert into person values (null, 1, 'Lucas', true);

insert into person values(null, 2, 'Jorge Amancio', true);

insert into phone values (null, '1140028922', 2);

insert into customer values(2);

insert into employee values (null, 'CEO', 1);

insert into user values ('admin', 'admin', 1, true, 5);

