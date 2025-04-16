insert into user (id, email,first_name,last_name,roles,password) values (1,'yusuke@yuyuhakusho.com','Yusuke','Urameshi','USER','{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW');
insert into user (id, email,first_name,last_name,roles,password) values (2,'hiei@yuyuhakusho.com','Hiei','Dragon','USER','{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW');
insert into profile (id, description, name) values (1, 'Manages everything', 'Admin');
insert into user_profile (id, user_id, profile_id) values (1,1,1);
insert into user_profile (id, user_id, profile_id) values (2,2,1);