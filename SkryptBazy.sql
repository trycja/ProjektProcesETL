USE [master]
GO
/****** Object:  Database [Warehouse]    Script Date: 06/01/2018 19:03:25 ******/
CREATE DATABASE [Warehouse]
CONTAINMENT = NONE
ON  PRIMARY 
( NAME = N'Warehouse', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL13.MSSQLSERVER\MSSQL\DATA\Warehouse.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
LOG ON 
( NAME = N'Warehouse_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL13.MSSQLSERVER\MSSQL\DATA\Warehouse_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [Warehouse] SET COMPATIBILITY_LEVEL = 130
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Warehouse].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Warehouse] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Warehouse] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Warehouse] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Warehouse] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Warehouse] SET ARITHABORT OFF 
GO
ALTER DATABASE [Warehouse] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Warehouse] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Warehouse] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Warehouse] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Warehouse] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Warehouse] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Warehouse] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Warehouse] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Warehouse] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Warehouse] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Warehouse] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Warehouse] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Warehouse] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Warehouse] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Warehouse] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Warehouse] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Warehouse] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Warehouse] SET RECOVERY FULL 
GO
ALTER DATABASE [Warehouse] SET  MULTI_USER 
GO
ALTER DATABASE [Warehouse] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Warehouse] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Warehouse] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Warehouse] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Warehouse] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'Warehouse', N'ON'
GO
ALTER DATABASE [Warehouse] SET QUERY_STORE = OFF
GO
USE [Warehouse]
GO
ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
GO
USE [Warehouse]
GO
/****** Object:  Table [dbo].[Opinie]    Script Date: 06/01/2018 19:03:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Opinie](
       [ID] [nvarchar](50) NULL,
       [Autor] [nvarchar](100) NULL,
       [KodProduktu] [nvarchar](50) NULL,
       [Podsumowanie] [nvarchar](4000) NULL,
       [Zalety] [nvarchar](500) NULL,
       [Wady] [nvarchar](500) NULL,
       [Ocena] [nvarchar](50) NULL,
       [DataWystawienia] [nvarchar](50) NULL,
       [PolecamNiepolecam] [nvarchar](50) NULL,
       [Pomocne] [nvarchar](50) NULL,
       [Niepomocne] [nvarchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Produkty]    Script Date: 06/01/2018 19:03:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Produkty](
       [KodProduktu] [nvarchar](50) NULL,
       [Typ] [nvarchar](100) NULL,
       [Marka] [nvarchar](100) NULL,
       [Model] [nvarchar](500) NULL,
       [DodatkoweUwagi] [nvarchar](1000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Tabela]    Script Date: 06/01/2018 19:03:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Tabela](
       [typ] [nvarchar](100) NULL,
       [marka] [nvarchar](100) NULL,
       [model] [nvarchar](500) NULL,
       [DodatkoweUwagi] [nvarchar](1000) NULL,
       [KodProduktu] [nvarchar](50) NULL,
       [Autor] [nvarchar](100) NULL,
       [Podsumowanie] [nvarchar](4000) NULL,
       [Zalety] [nvarchar](500) NULL,
       [Wady] [nvarchar](500) NULL,
       [Ocena] [nvarchar](50) NULL,
       [DataWystawienia] [nvarchar](50) NULL,
       [PolecamNiepolecam] [nvarchar](50) NULL,
       [Pomocne] [nvarchar](50) NULL,
       [Niepomocne] [nvarchar](50) NULL
) ON [PRIMARY]
GO
USE [master]
GO
ALTER DATABASE [Warehouse] SET  READ_WRITE 
GO
