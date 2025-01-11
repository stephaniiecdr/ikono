-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 14, 2024 at 04:58 PM
-- Server version: 8.0.40-0ubuntu0.24.04.1
-- PHP Version: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pradita`
--

-- --------------------------------------------------------

--
-- Table structure for table `product_group`
--

CREATE TABLE `product_group` (
  `group_id` int NOT NULL,
  `group_name` varchar(100) NOT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product_group`
--

INSERT INTO `product_group` (`group_id`, `group_name`, `description`, `created_at`, `updated_at`) VALUES
(1, 'Electronics', 'Produk elektronik seperti laptop, ponsel, dan aksesori.', '2024-12-14 14:27:57', '2024-12-14 14:27:57'),
(2, 'Furniture', 'Produk perabot rumah seperti meja, kursi, dan lemari.', '2024-12-14 14:27:57', '2024-12-14 14:27:57'),
(3, 'Clothing', 'Pakaian pria, wanita, dan anak-anak.', '2024-12-14 14:27:57', '2024-12-14 14:27:57'),
(4, 'Food & Beverages', 'Makanan dan minuman untuk kebutuhan sehari-hari.', '2024-12-14 14:27:57', '2024-12-14 14:27:57'),
(5, 'Stationery', 'Alat tulis kantor dan kebutuhan sekolah.', '2024-12-14 14:27:57', '2024-12-14 14:27:57');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product_group`
--
ALTER TABLE `product_group`
  ADD PRIMARY KEY (`group_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `product_group`
--
ALTER TABLE `product_group`
  MODIFY `group_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
