DROP SCHEMA IF EXISTS RewardProgramCustomer_db;

CREATE SCHEMA RewardProgramCustomer_db;

USE RewardProgramCustomer_db;


CREATE TABLE transactions (

id INT AUTO_INCREMENT PRIMARY KEY,
customer_id LONG NOT NULL,
customer_name VARCHAR(255) NOT NULL,
transaction_date DATE NOT NULL,
amount DOUBLE NOT NULL

);



INSERT INTO transactions (customer_id, customer_name, transaction_date, amount) VALUES

(102, 'Ramesh Tale', '2025-07-15', 120.0),

(102, 'Ramesh Tale', '2025-08-10', 75.0),

(102, 'Ramesh Tale', '2025-09-05', 200.0),

(201, 'Gaytri Patil', '2025-07-20', 45.0),

(201, 'Gaytri Patil', '2025-08-15', 95.0),

(201, 'Gaytri Patil', '2025-09-10', 130.0),

(301, 'Charlie Moe', '2025-07-18', 53.70),

(301, 'Charlie Moe', '2025-08-26', 173.77),

(301, 'Charlie Moe', '2025-08-24', 194.33);



select * from transactions;
commit;

select * from transactions;















