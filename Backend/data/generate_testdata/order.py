import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random order records
order_records = []
for _ in range(10000):
    order_type = random.randint(0, 1)  # Order type: 0 (任务订单) or 1 (服务订单)
    owner_id = random.randint(1, 10000)
    accessor_id = random.randint(1, 10000)
    # Ensure owner_id and accessor_id are not the same
    while accessor_id == owner_id:
        accessor_id = random.randint(1, 10000)
    status = random.randint(0, 4)
    cost = random.randint(1, 10000) * 100  # Cost stored as 100 times the actual value
    item_id = random.randint(1, 10000)

    order_records.append(f"({order_type}, {owner_id}, {accessor_id}, {status}, {cost}, {item_id})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO `order` (type, owner_id, accessor_id, status, cost, item_id) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_order.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(order_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(order_records[i:i+1000]) + ";\n"
        if i + 1000 < len(order_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_order.sql")
