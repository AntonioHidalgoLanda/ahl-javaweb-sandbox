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
  id,
  name,
  pageUrl,
  planCode,
  contactEmail,
  contactPhone,
  contactName
);

CREATE TABLE reseller (
  id,
  name,
  pageurl,
  planCode,
  contactEmail,
  contactPhone,
  contactName
);

CREATE TABLE shoppingOnlineLink (
  id,
  url,
  productId,
  resellerId
);

CREATE TABLE tag (
  name,
  igotitId
);

CREATE TABLE photo (
  id,
  localpath,
  igotitId
);

CREATE TABLE product (
  id,
  sku,
  brandid,
  brandedIgotitId, -- reference post from the brand (has priority)
  name,
  brandLink -- see the product in brand page
);

CREATE TABLE store (
  id,
  resellerId,
  numberStreet,
  address1,
  address2,
  city,
  stateProvince,
  country,
  postCode
  contactEmail,
  contactPhone,
  contactName
);

CREATE TABLE storeProduct (
  productId,
  storeId
);
