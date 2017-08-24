-- END USERS
INSERT INTO enduser (id,federationId,profileName)
    VALUES ( -1,'','all users');

INSERT INTO enduser (id,federationId,profileName,recoveryEmail,avatarUrl)
    VALUES ( 1,'test_federationId1','test_profileName2','test_recoveryEmail1','test_avatarUrl1');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 1, 'enduser', 1);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 1, 1, 'false');

INSERT INTO enduser (id,federationId,profileName,recoveryEmail,avatarUrl)
    VALUES( 2, 'test_federationId2','test_profileName2','test_recoveryEmail2','test_avatarUrl2');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 2, 'enduser', 2);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 2, 2, 'false');

INSERT INTO enduser (id,federationId,profileName,recoveryEmail)
    VALUES( 3,'test_CompanyId1','test_BrandUser1','test_BrandEmailEmail1');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 3, 'enduser', 3);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 3, 3, 'false');

INSERT INTO enduser (id,federationId,profileName,recoveryEmail)
    VALUES( 4,'test_CompanyId2','test_BrandUser2','test_BrandEmailEmail2');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 4, 'enduser', 4);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 4, 'false');

-- I GOT ITs
INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 1,1,'test_coordinates1',1 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 5, 'igotit', 1);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 1, 5, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 2,2,'test_coordinates2',2 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 6, 'igotit', 2);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 2, 6, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 3,1,'test_coordinates3',3 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 7, 'igotit', 3);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 1, 7, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 4,3,'test_coordinatesBrand1',5 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 8, 'igotit', 4);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 3, 8, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 5,3,'test_coordinatesBrand1',5 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 9, 'igotit', 5);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 3, 9, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 6,3,'test_coordinatesBrand1',5 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 10, 'igotit', 6);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 3, 10, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 7,4,'test_coordinatesBrand1',5 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 11, 'igotit', 7);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 11, 'false');

INSERT INTO igotit (  id,enduserid,coordinates,rating) VALUES ( 8,4,'test_coordinatesBrand1',5 );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 12, 'igotit', 8);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 12, 'false');


--
-- BRAND
--
INSERT INTO brand (id,name) VALUES (1,'test_brand1' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 13, 'brand', 8);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 13, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 13, 'true');

INSERT INTO brand (id,name) VALUES (2,'test_brand2' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 14, 'brand', 8);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 14, 'false');--all

--
-- RESELLER
--
INSERT INTO reseller (id,name) VALUES (1,'test_shop1' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 15, 'reseller', 1);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 15, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 15, 'true');

INSERT INTO reseller (id,name) VALUES (2,'test_shop2' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 16, 'reseller', 2);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 16, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 16, 'true');

INSERT INTO reseller (id,name) VALUES (3,'test_shop3' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 17, 'reseller', 3);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 17, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 17, 'true');

INSERT INTO reseller (id,name) VALUES (4,'test_shop4' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 18, 'reseller', 4);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 18, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 18, 'true');

INSERT INTO reseller (id,name) VALUES (5,'test_shop5' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 19, 'reseller', 5);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 19, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 19, 'true');


--
-- TAG
-- Igotit Accessibility
--
INSERT INTO tag (name,igotitId) VALUES ( 'test',1);
INSERT INTO tag (name,igotitId) VALUES ( ':love:',1);
INSERT INTO tag (name,igotitId) VALUES ( ':like:',1);
INSERT INTO tag (name,igotitId) VALUES ( ':)',2);
INSERT INTO tag (name,igotitId) VALUES ( 'great',2);
INSERT INTO tag (name,igotitId) VALUES ( 'test',3);
INSERT INTO tag (name,igotitId) VALUES ( '#hastagging',3);


--
-- PHOTO
-- Igotit Accessibility
--
INSERT INTO photo (id,localpath,igotitId) VALUES (1,'photos/coat-of-arms.png',3);


--
-- PRODUCT
--
INSERT INTO product (id,sku,brandid,brandedIgotitId,name) VALUES (1,'test_SKU_X1',1,4,'product1-brand1' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 20, 'product', 1);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 20, 'true');

INSERT INTO product (id,sku,brandid,brandedIgotitId,name) VALUES (2,'test_SKU_X2',1,5,'product2-brand1' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 21, 'product', 2);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 21, 'true');

INSERT INTO product (id,sku,brandid,brandedIgotitId,name) VALUES (3,'test_SKU_X3',1,6,'product3-brand1' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 22, 'product', 3);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 22, 'true');

INSERT INTO product (id,sku,brandid,brandedIgotitId,name) VALUES (4,'test_SKU_A1',2,7,'product1-brand2' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 23, 'product', 4);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 23, 'true');

INSERT INTO product (id,sku,brandid,brandedIgotitId,name) VALUES (5,'test_SKU_A2',2,8,'product2-brand2' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 24, 'product', 5);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 24, 'true');

INSERT INTO product (id,sku,brandid,name) VALUES (6,'test_SKU_A3',2,'product3-brand2-unbranded' );
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 25, 'product', 6);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 25, 'true');

--
-- SHOPPING ONLINE LINK
--
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (1,'test_url r1 p1 ', 1,1);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (2,'test_url r1 p1 ', 1,2);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (3,'test_url r1 p1 ', 2,1);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (4,'test_url r1 p1 ', 3,2);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (5,'test_url r1 p1 ', 4,4);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (6,'test_url r1 p1 ', 5,3);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (7,'test_url r1 p1 ', 6,5);
INSERT INTO shoppingOnlineLink (id,url,productId,resellerId) VALUES (8,'test_url r1 p1 ', 6,4);

--
-- STORE
-- Dependant on resellerId
--
INSERT INTO store (id,resellerId,numberStreet,country,postCode) VALUES (1, 1,'test_street 1','GBR','AB1 2CD');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 26, 'store', 1);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 26, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 26, 'true');

INSERT INTO store (id,resellerId,numberStreet,country,postCode) VALUES (2, 1,'test_street 2','GBR','AB1 2CD"');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 27, 'store', 2);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 27, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 27, 'true');

INSERT INTO store (id,resellerId,numberStreet,country,postCode) VALUES (3, 4,'test_street 3','GBR','AB1 2CD');
INSERT INTO accessResource (id, tablename, localid)
    VALUES ( 28, 'store', 3);
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( 4, 28, 'false');
INSERT INTO access (enduserid, accessid, readonly)
    VALUES ( -1, 28, 'true');



--
-- STORE PRODUCT
-- Store Accessibility
--
INSERT INTO storeProduct (productId,storeId) VALUES (1,1 );
INSERT INTO storeProduct (productId,storeId) VALUES (2,1 );
INSERT INTO storeProduct (productId,storeId) VALUES (3,2 );
INSERT INTO storeProduct (productId,storeId) VALUES (4,3 );
INSERT INTO storeProduct (productId,storeId) VALUES (5,3 );

--
-- IGOTIT PRODUCT
-- Igotit Accessibility
--
INSERT INTO igotitProduct (igotitId ,productId) VALUES (1, 1);
INSERT INTO igotitProduct (igotitId ,productId) VALUES (2, 1);
INSERT INTO igotitProduct (igotitId ,productId) VALUES (3, 2);

--
-- FRIEND
-- Enduser Accessibility
--
INSERT INTO friend (enduserid,friendid,relationship) VALUES (1,2,0 );
INSERT INTO friend (enduserid,friendid,relationship) VALUES (1,3,1 );