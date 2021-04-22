# Shopping Data
## Description

There is a CSV file that keeps a record of n order details for orders made at an online shopping website.The CSV has a .csv extension.

Each line contains a single record with the following columns in order:

Id of the order placed
Area where the order was delivered
Name of the product
Quantity of the product delivered in that order
Brand of the ordered product


Create two csv files named 0_input_file_name and 1_input_name where input file name is the name of the input file given including the CSV extension
Each file must contain r rows where r is the number of distinct products.
Data should be comma limited, and the row order does not matter.

The structure of each file is as follows

1. 0_input_file_name- In the first column, list the product name. 
   The second column should contain the average quantity of the product purchased per order.

2. 1_input_file_name - In the first column, list the product name.
   The second column should be the most popular brand for that product.
   Most popular brand is defined as the brand with the most total orders and not the quantity
