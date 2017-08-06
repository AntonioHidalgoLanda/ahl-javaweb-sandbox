CREATE TABLE enduser (
  id   SERIAL PRIMARY KEY,
  federationId varchar (255) NOT NULL UNIQUE,
  profileName varchar (255) NOT NULL,
  recoveryEmail varchar (255) NOT NULL,
  avatarUrl varchar(255)
);

CREATE TABLE igotit (
  id   SERIAL PRIMARY KEY,
  publishdate timestamp not null default CURRENT_DATE,
  enduserid int4 not null references enduser(id),
  visibility smallint not null default 0, -- 0 public; 1 private
  usercomment varchar (1024) not null default '', 
  coordinates varchar (255),
  rating smallint
);

CREATE TABLE brand (
  id SERIAL PRIMARY KEY,
  name varchar (255) not null,
  pageUrl varchar (255),
  planCode varchar (10) not null default 'FREE', -- premium, or band-code of the service
  contactEmail varchar (255),
  contactPhone varchar (50),
  contactName varchar (255)
);

CREATE TABLE reseller (
  id SERIAL PRIMARY KEY,
  name varchar (255) not null,
  pageUrl varchar (255),
  planCode varchar (10) not null default 'FREE', -- premium, or band-code of the service
  contactEmail varchar (255),
  contactPhone varchar (50),
  contactName varchar (255)
);

CREATE TABLE tag (
  name varchar (255) not null,
  igotitId int4 not null references igotit (id)
);

CREATE TABLE photo (
  id SERIAL PRIMARY KEY,
  localpath varchar (255) not null,
  igotitId int4 not null references igotit (id)
);

CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  sku varchar (255) not null,
  brandid  int4 not null references brand (id),
  brandedIgotitId int4 not null references igotit(id), -- reference post from the brand (has priority)
  name varchar (255) not null,
  brandLink varchar (255) -- see the product in brand page
);


CREATE TABLE shoppingOnlineLink (
  id SERIAL PRIMARY KEY,
  url varchar (255),
  productId int4 not null references product(id),
  resellerId int4 not null references reseller(id)
);
CREATE TABLE store (
  id SERIAL PRIMARY KEY,
  resellerId int4 not null references reseller(id),
  numberStreet varchar (255),
  address1 varchar (255),
  address2 varchar (255),
  city varchar (255),
  stateProvince varchar (255),
  country varchar (255),
  postCode varchar (255) not null,
  contactEmail varchar (255),
  contactPhone varchar (255),
  contactName varchar (255)
);

CREATE TABLE storeProduct (
  productId int4 not null references product(id),
  storeId int4 not null references store(id)
);

CREATE TABLE igotitProduct (
  igotitId int4 not null references igotit (id),
  productId int4 not null references product(id)
);
