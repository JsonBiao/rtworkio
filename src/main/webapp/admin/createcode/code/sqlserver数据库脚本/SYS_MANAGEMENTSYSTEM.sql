GO
/****** FHQQ313596790 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE SYS_MANAGEMENTSYSTEM (
 		MANAGEMENTSYSTEM_ID  nvarchar(100) NOT NULL,
		NAME nvarchar(255) DEFAULT NULL,
		PATH nvarchar(255) DEFAULT NULL,
		CLASSIFICATION nvarchar(255) DEFAULT NULL,
		SUFFIX nvarchar(255) DEFAULT NULL,
		ISDOWM int NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[MANAGEMENTSYSTEM_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
