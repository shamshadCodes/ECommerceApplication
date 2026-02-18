-- Create databases for all services
CREATE DATABASE IF NOT EXISTS user_db;
CREATE DATABASE IF NOT EXISTS catalog_db;
CREATE DATABASE IF NOT EXISTS inventoryService;

-- Create users and grant privileges
CREATE USER IF NOT EXISTS 'userDbUser'@'%' IDENTIFIED BY 'password';
CREATE USER IF NOT EXISTS 'product_user'@'%' IDENTIFIED BY 'password';

-- Grant privileges
GRANT ALL PRIVILEGES ON user_db.* TO 'userDbUser'@'%';
GRANT ALL PRIVILEGES ON catalog_db.* TO 'product_user'@'%';
GRANT ALL PRIVILEGES ON inventoryService.* TO 'root'@'%';

FLUSH PRIVILEGES;

