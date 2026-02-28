-- Create databases for all microservices
CREATE DATABASE IF NOT EXISTS user_db;
CREATE DATABASE IF NOT EXISTS catalog_db;
CREATE DATABASE IF NOT EXISTS inventoryService;
CREATE DATABASE IF NOT EXISTS order_db;
CREATE DATABASE IF NOT EXISTS cart_db;

-- Create users and grant privileges
CREATE USER IF NOT EXISTS 'userDbUser'@'%' IDENTIFIED BY 'password';
CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON user_db.* TO 'userDbUser'@'%';
GRANT ALL PRIVILEGES ON catalog_db.* TO 'product_user'@'%';
GRANT ALL PRIVILEGES ON inventoryService.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON order_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON cart_db.* TO 'root'@'%';

FLUSH PRIVILEGES;

