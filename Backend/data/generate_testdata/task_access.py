import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random task_access records
task_access_records = []
for _ in range(10000):
    task_id = random.randint(1, 10000)  # Assuming task_id ranges from 1 to 10000
    accessor_id = random.randint(1, 10000)  # Assuming accessor_id ranges from 1 to 10000
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
    status = random.randint(0, 2)  # Status ranges from 0 to 2

    task_access_records.append(f"({task_id}, {accessor_id}, '{created_at}', {status})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO task_access (task_id, accessor_id, created_at, status) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_task_access.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(task_access_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(task_access_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(task_access_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_task_access.sql")
