import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random service_collect records
service_collect_records = []
for _ in range(10000):
    service_id = random.randint(1, 10000)  # Assuming service_id ranges from 1 to 10000
    collector_id = random.randint(1, 10000)  # Assuming collector_id ranges from 1 to 10000
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')

    service_collect_records.append(f"({service_id}, {collector_id}, '{created_at}')")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO service_collect (service_id, collector_id, created_at) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_service_collect.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(service_collect_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(service_collect_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(service_collect_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_service_collect.sql")
