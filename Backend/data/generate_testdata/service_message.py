import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random service_message records
service_message_records = []
for _ in range(10000):
    service_id = random.randint(1, 10000)  # Assuming service_id ranges from 1 to 10000
    messager_id = random.randint(1, 10000)  # Assuming messager_id ranges from 1 to 10000
    content = fake.text(max_nb_chars=255).replace('"', '""')  # Ensure content does not exceed 255 characters
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
    liked_number = random.randint(0, 10000)

    service_message_records.append(f"({service_id}, {messager_id}, \"{content}\", '{created_at}', {liked_number})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO service_message (service_id, messager_id, content, created_at, liked_number) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_service_message.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(service_message_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(service_message_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(service_message_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_service_message.sql")