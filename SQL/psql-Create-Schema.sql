CREATE TABLE enduser (
  id   SERIAL PRIMARY KEY,
  federationId varchar (255) NOT NULL UNIQUE,
  profileName varchar (255) NOT NULL,
  recoveryEmail varchar (255),
  avatarUrl varchar(255)
);

CREATE TABLE igotit (
  id   SERIAL PRIMARY KEY,
  publishdate timestamp not null default (now() at time zone 'utc'),
  enduserid int4 not null references enduser(id),
  visibility smallint not null default 0, -- 0 public; 1 private
  usercomment varchar (1024) not null default '', 
  coordinates varchar (255),
  rating smallint,
  accessLevel int4 not null default 0 -- 0 (draft/me), 1 (friends), 2 (followers), 3 (public)
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
  brandedIgotitId int4 references igotit(id), -- reference post from the brand (has priority)
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
  numberStreet varchar (255) not null,
  address1 varchar (255),
  address2 varchar (255),
  city varchar (255),
  stateProvince varchar (255),
  country varchar (255) not null,
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

CREATE TABLE friend (
  enduserid int4 not null references enduser(id),
  friendid int4 not null references enduser(id),
  relationship smallint not null default 1    -- 0 (follower), 1 (friend)
);

/* Accessibiliy Tables */
CREATE TABLE accessResource (
  id SERIAL PRIMARY KEY,
  tablename varchar (255) not null, -- CANNOT DO :'( references information_schema.tables(table_name)
  localid int4 not null
);

CREATE TABLE access (
  enduserid int4 not null references enduser(id),
  accessid int4 not null references accessResource(id),
  readonly BOOLEAN default false
);

