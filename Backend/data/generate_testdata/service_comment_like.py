import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random service_comment_like records
service_comment_like_records = []
for _ in range(10000):
    service_comment_id = random.randint(1, 10000)  # Assuming service_comment_id ranges from 1 to 10000
    liker_id = random.randint(1, 10000)  # Assuming liker_id ranges from 1 to 10000

    service_comment_like_records.append(f"({service_comment_id}, {liker_id})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO service_comment_like (service_comment_id, liker_id) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_service_comment_like.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(service_comment_like_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(service_comment_like_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(service_comment_like_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_service_comment_like.sql")
