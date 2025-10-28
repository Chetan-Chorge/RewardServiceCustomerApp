# Retail Rewards API

## Project Overview
A RESTful API to calculate customer reward points based on transaction history.

## Tech Stack
- Java 8
- Spring Boot
- H2 Database
- JPA
- Postman
- JUnit & Mockito

## Reward Logic
- 2 points for every dollar spent over $100
- 1 point for every dollar spent between $50 and $100

 
## API Endpoints

1. Add Customer Transaction

POST http://localhost:2318/api/rewards/customer

**Valid Request(Request Example):**

`json
{
"customerId": "402",
"customerName": "James Bond",
"amount": 111,
"transactionDate": "2025-06-11"

}
```
**Successful Response:
''json

{
"message": "Transaction recorded successfully",
"rewardPoints": 72
}

```
**Invalid Request Examples:**
```json
{
"customerId": 401,
"startDate": "2025-10-01",
"endDate": "2025-09-01"
}
```
```json
{

"customerId": null,
"startDate": null,
"endDate": null
}
```

**Invalid Response:**
```json
{
"error": "Invalid request parameters"
}
```

2. Calculate Rewards for Customer



GET http://localhost:2318/api/rewards/calculate/{customerId}?startDate={startDate}&endDate={endDate}



**Examples 1:**
- Customer ID: 102
```
GET http://localhost:2318/api/rewards/calculate/102?startDate=2025-07-01&endDate=2025-09-10
```
**Response:**

```json
{
	   "customerId": 102,
	   "customerName": "Ramesh Tale",
	   "monthlyRewards": [

     {
	         "month": "July",
	         "rewardPoints": 90
     },
     {
	          "month": "August",
	          "rewardPoints": 25
     },

     {
	          "month": "September",
	         "rewardPoints": 250
      }

   ],
  "totalRewards": 365,
   "transactions": [
         {
              "transactionDate": "2025-07-15",
              "amount": 120.0,
              "rewardPoints": 90
         },
         {
              "transactionDate": "2025-08-10",
              "amount": 75.0,
              "rewardPoints": 25
         },
         {
               "transactionDate": "2025-09-05",
               "amount": 200.0,
               "rewardPoints": 250
          }

   ]
 }
```
**Examples 2:**
- Customer ID: 201
```
GET http://localhost:2318/api/rewards/calculate/201?startDate=2025-07-01&endDate=2025-09-15
```
{
				"customerId": 201,
				"customerName": "Gaytri Patil",
				"monthlyRewards": [
	      {
				"month": "July",
				"rewardPoints": 0
	      },

          {
				 "month": "August",	
				  "rewardPoints": 45
          },

          {
				"month": "September",
				"rewardPoints": 110
          }
    ],
   "totalRewards": 155,
   "transactions": [
           {
				"transactionDate": "2025-07-20",
				"amount": 45.0,
				"rewardPoints": 0
            },
            {
				"transactionDate": "2025-08-15",
				"amount": 95.0,			
				"rewardPoints": 45
            },

            {
				"transactionDate": "2025-09-10",				
				"amount": 130.0,				
	 			"rewardPoints": 110
            }
   ]
}


**Examples 3:**
- Customer ID: 301
```
GET http://localhost:2318/api/rewards/calculate/301?startDate=2025-07-01&endDate=2025-09-15

```
{
			"customerId": 301,				
			"customerName": "Charlie Moe",			
			"monthlyRewards": [

           {
					"month": "July",		
					"rewardPoints": 3
           },
           
					"month": "August",				
					"rewardPoints": 435
            }
            ],
		"totalRewards": 438,
		"transactions": [
            {
					"transactionDate": "2025-07-18",				
					"amount": 53.7,				
					"rewardPoints": 3
            },
            {
					"transactionDate": "2025-08-26",				
					"amount": 173.77,				
					"rewardPoints": 197
             },
             {
					"transactionDate": "2025-08-24",				
					"amount": 194.33,					
					"rewardPoints": 238
             }
  ]
}


Response Format
All responses are returned in JSON format.


Notes

Ensure the transaction date is in a valid format (yyyy-MM-dd).
Reward points are calculated per transaction based on the amount spent.
The API supports dynamic time frame filtering and pagination (if implemented).
Input validation and exception handling are enforced.
Logging and testing are integrated for maintainability and reliability.




