CREATE DATABASE IF NOT EXISTS dongbang;
USE dongbang;

CREATE TABLE `m_user` (
  `id` varchar(10) NOT NULL,
  `password` varchar(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `dept` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL,
  `insert_id` varchar(10) DEFAULT NULL,
  `insert_time` timestamp NULL DEFAULT NULL,
  `update_id` varchar(10) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO m_user (id, password, name, dept, title, role, insert_id, insert_time, update_id, update_time)
VALUES
  ('00001', 'password1', 'John Doe', 'Sales', 'Manager', 'admin', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00002', 'password2', 'Jane Smith', 'Marketing', 'Analyst', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00003', 'password3', 'Michael Johnson', 'Finance', 'Accountant', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00004', 'password4', 'Emily Davis', 'HR', 'Recruiter', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00005', 'password5', 'David Wilson', 'IT', 'Developer', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00006', 'password6', 'Sarah Anderson', 'Sales', 'Representative', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00007', 'password7', 'Robert Taylor', 'Marketing', 'Manager', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00008', 'password8', 'Olivia Brown', 'Finance', 'Analyst', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00009', 'password9', 'William Martinez', 'HR', 'Manager', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27'),
  ('00010', 'password10', 'Sophia Johnson', 'IT', 'System Administrator', 'user', 'admin', '2023-05-27', 'admin', '2023-05-27');
