INSERT INTO enduser (federationId,profileName,recoveryEmail,avatarUrl)
    VALUES( 'test_federationId1','test_profileName2','test_recoveryEmail1','test_avatarUrl1');
INSERT INTO enduser (federationId,profileName,recoveryEmail,avatarUrl)
    VALUES( 'test_federationId2','test_profileName2','test_recoveryEmail2','test_avatarUrl2');
INSERT INTO enduser (federationId,profileName,recoveryEmail)
    VALUES( 'test_CompanyId1','test_BrandUser1','test_BrandEmailEmail1');
INSERT INTO enduser (federationId,profileName,recoveryEmail)
    VALUES( 'test_CompanyId2','test_BrandUser2','test_BrandEmailEmail2');

INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 1,'test_coordinates1',1 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 2,'test_coordinates2',2 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 1,'test_coordinates3',3 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 3,'test_coordinatesBrand1',5 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 3,'test_coordinatesBrand1',5 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 3,'test_coordinatesBrand1',5 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 4,'test_coordinatesBrand1',5 );
INSERT INTO igotit (  enduserid,coordinates,rating) VALUES ( 4,'test_coordinatesBrand1',5 );

INSERT INTO brand (name) VALUES ('test_brand1' );
INSERT INTO brand (name) VALUES ('test_brand2' );

INSERT INTO reseller (name) VALUES ('test_shop1' );
INSERT INTO reseller (name) VALUES ('test_shop2' );
INSERT INTO reseller (name) VALUES ('test_shop3' );
INSERT INTO reseller (name) VALUES ('test_shop4' );
INSERT INTO reseller (name) VALUES ('test_shop5' );

INSERT INTO tag (name,igotitId) VALUES ( 'test',1);
INSERT INTO tag (name,igotitId) VALUES ( ':love:',1);
INSERT INTO tag (name,igotitId) VALUES ( ':like:',1);
INSERT INTO tag (name,igotitId) VALUES ( ':)',2);
INSERT INTO tag (name,igotitId) VALUES ( 'great',2);
INSERT INTO tag (name,igotitId) VALUES ( 'test',3);
INSERT INTO tag (name,igotitId) VALUES ( '#hastagging',3);

INSERT INTO photo (localpath,igotitId) VALUES ('photos/coat-of-arms.png',3);

INSERT INTO product (sku,brandid,brandedIgotitId,name) VALUES ('test_SKU_X1',1,4,'product1-brand1' );
INSERT INTO product (sku,brandid,brandedIgotitId,name) VALUES ('test_SKU_X2',1,5,'product2-brand1' );
INSERT INTO product (sku,brandid,brandedIgotitId,name) VALUES ('test_SKU_X3',1,6,'product3-brand1' );
INSERT INTO product (sku,brandid,brandedIgotitId,name) VALUES ('test_SKU_A1',2,7,'product1-brand2' );
INSERT INTO product (sku,brandid,brandedIgotitId,name) VALUES ('test_SKU_A2',2,8,'product2-brand2' );
INSERT INTO product (sku,brandid,name) VALUES ('test_SKU_A3',2,'product3-brand2-unbranded' );

INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 1,1);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 1,2);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 2,1);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 3,2);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 4,4);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 5,3);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 6,5);
INSERT INTO shoppingOnlineLink (url,productId,resellerId) VALUES ('test_url r1 p1 ', 6,4);

INSERT INTO store (resellerId,numberStreet,country,postCode) VALUES ( 1,'test_street 1','GBR','AB1 2CD');
INSERT INTO store (resellerId,numberStreet,country,postCode) VALUES ( 1,'test_street 2','GBR','AB1 2CD"');
INSERT INTO store (resellerId,numberStreet,country,postCode) VALUES ( 4,'test_street 3','GBR','AB1 2CD');

INSERT INTO storeProduct (productId,storeId) VALUES (1,1 );
INSERT INTO storeProduct (productId,storeId) VALUES (2,1 );
INSERT INTO storeProduct (productId,storeId) VALUES (3,2 );
INSERT INTO storeProduct (productId,storeId) VALUES (4,3 );
INSERT INTO storeProduct (productId,storeId) VALUES (5,3 );

INSERT INTO igotitProduct (igotitId ,productId) VALUES (1, 1);
INSERT INTO igotitProduct (igotitId ,productId) VALUES (2, 1);
INSERT INTO igotitProduct (igotitId ,productId) VALUES (3, 2);

INSERT INTO friend (enduserid,friendid,relationship) VALUES (1,2,0 );
INSERT INTO friend (enduserid,friendid,relationship) VALUES (1,3,1 );