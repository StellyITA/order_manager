INSERT INTO CATEGORY(CATEGORY_NAME) VALUES('starter');
INSERT INTO MENU_ITEM(DISH_NAME, DISH_IMAGE, CATEGORY, PRICE, AVAILABLE) VALUES('Antipasto di terra', FILE_READ('classpath:images/antipasto-di-terra.png'), 1, 18.80, TRUE);
INSERT INTO MENU_ITEM(DISH_NAME, DISH_IMAGE, CATEGORY, PRICE, AVAILABLE) VALUES('Cozze alla marinara', FILE_READ('classpath:images/cozze-alla-marinara.png'), 1, 15.00, TRUE);
INSERT INTO MENU_ITEM(DISH_NAME, DISH_IMAGE, CATEGORY, PRICE, AVAILABLE) VALUES('Crudo e bufala', FILE_READ('classpath:images/crudo-e-bufala.png'), 1, 16.79, FALSE);